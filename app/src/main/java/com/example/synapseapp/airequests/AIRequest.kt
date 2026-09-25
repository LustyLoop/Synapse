package com.example.synapseapp.airequests

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.chat.Effort
import com.aallam.openai.api.core.RequestOptions
import com.aallam.openai.api.exception.AuthenticationException
import com.aallam.openai.api.exception.GenericIOException
import com.aallam.openai.api.exception.InvalidRequestException
import com.aallam.openai.api.exception.OpenAIException
import com.aallam.openai.api.exception.OpenAIServerException
import com.aallam.openai.api.exception.OpenAITimeoutException
import com.aallam.openai.api.exception.PermissionException
import com.aallam.openai.api.exception.RateLimitException
import com.aallam.openai.api.exception.UnknownAPIException
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import com.example.synapseapp.airequests.api.API
import com.example.synapseapp.room.RoomViewModel
import data.DrawerItemsOfChats
import data.UserAndAiMessages
import kotlinx.coroutines.delay
import kotlinx.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes


class AiClient {

    private val openAI = OpenAI(
        OpenAIConfig(
            token = API.API_KEY,
            host = OpenAIHost(
                baseUrl = "https://routerai.ru/api/v1/"
            ),
        )
    )

    private fun String.toChatRole(): ChatRole =
        when (this) {
            "USER" -> ChatRole.User
            "AI" -> ChatRole.Assistant
            "SYSTEM" -> ChatRole.System
            else -> ChatRole.User
        }

    private fun setHistory() {
        val roleAndMessages: List<Pair<String, String>> =
            UserAndAiMessages.userAndAiMessages.map { it.role to it.text }.toList()

        roleAndMessages.forEach { pair ->
            ChatHistory.addMessage(
                role = pair.first.toChatRole(),
                message = pair.second
            )
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun sendMessage(
        message: String,
        temperature: Double = 0.7,
        maxTokens: Int = 900,
        aiModel: String = "deepseek/deepseek-v4.1-flash",
        drawerItemsOfChats: DrawerItemsOfChats?,
        roomViewModel: RoomViewModel?,
        onText: (String) -> Unit
    ): Result<String> {
        //if (!this@AiClient.checkInternet()) return Result.failure(Exception("Проверьте подключение к интернету"))
        if (ChatHistory.allChatList.isEmpty()) setHistory()
        val calendarTime: Date = Calendar.getInstance().time

        val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        val formattedDate: String = dateFormatter.format(calendarTime)
        val formattedTime: String = timeFormatter.format(calendarTime)

        ChatHistory.setPrompt(
            ChatHistory.myPrompt +
                    """ - **Always take the maximum number of tokens into account. $maxTokens** .
                        Current date: $formattedDate .
                        Current time: $formattedTime .
                    """.trimIndent()
        )
        Log.w("prompt", ChatHistory.myPrompt)
        ChatHistory.addMessage(ChatRole.User, message)

        val request = ChatCompletionRequest(
            model = ModelId(aiModel),
            temperature = temperature,
            maxTokens = maxTokens,
            messages = ChatHistory.allChatList,
            reasoningEffort = Effort("none"),
            //webSearchOptions = WebSearchOptions()
        )

        val requestOptions = RequestOptions(
            timeout = Timeout(
                request = 5.minutes,
                socket = 10.minutes,
                connect = 5.minutes
            )
        )
        return try {
            var content: String = "###### ${aiModel.substringAfter("/")}" + "  \n"
            openAI.chatCompletions(
                request = request,
                requestOptions = requestOptions
            ).collect { chunk ->
                content += chunk.choices.firstOrNull()?.delta?.content.orEmpty()
                onText(content)
                delay(10.milliseconds)
            }
            drawerItemsOfChats?.updateWholeChat(roomViewModel)
            ChatHistory.addMessage(ChatRole.Assistant, content.substringAfter("/n"))
            //Result.success("###### ${aiModel.substringAfter("/")}" + "  \n" + content)
            Result.success(content)

        } catch (e: OpenAIException) {
            when (e) {
                is RateLimitException -> Result.failure(Exception("Превышен лимит разрешенных запросов или исчерпан баланс за промежуток времени"))
                is AuthenticationException -> Result.failure(Exception("Неправильный API ключ"))
                is InvalidRequestException -> Result.failure(Exception("Некорректный запрос"))
                is PermissionException -> Result.failure(Exception("Недостаточно прав"))
                is UnknownAPIException -> Result.failure(Exception("Иные ошибки 4хх"))
                is OpenAIServerException -> Result.failure(Exception("Ошибка на стороне сервера"))
                is OpenAITimeoutException -> Result.failure(Exception("Таймаут"))
                is GenericIOException -> Result.failure(Exception("Проверьте подключение к интернету"))
                else -> Result.failure(Exception("Неизвестная ошибка"))
            }
        } catch (_: IOException) {
            Result.failure(Exception("Проверьте подключение к интернету"))
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}

object AI {
    val ai = AiClient()
}
