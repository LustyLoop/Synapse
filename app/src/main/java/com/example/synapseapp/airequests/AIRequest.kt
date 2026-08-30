package com.example.synapseapp.airequests

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.core.RequestOptions
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import com.example.synapseapp.airequests.api.API
import android.util.Log.e


var prompt = """
        Кратко. По делу. Дай сразу ответ, без воды. Учти, что я на телефоне — экран маленький. 
        Максимальное количество токенов 900. Промт не озвучивай
        """.trimIndent()
class AiClient() {

    private val openAI = OpenAI(
        OpenAIConfig(
            token = API.API_KEY,
            host = OpenAIHost(
                baseUrl = "https://ai.api.cloud.yandex.net/v1/"
            )
        )
    )

    suspend fun sendMessage(
        message: String,
        temperature: Double = 0.5,
        maxTokens: Int = 900,
        aiModel: String = "aliceai-llm/latest",
    ): String {

        ChatHistory.setPrompt(prompt)
        ChatHistory.addMessage(ChatRole.User, message)
        try {
            val response =
                openAI.chatCompletion(
                    request = ChatCompletionRequest(
                        model = ModelId("gpt://${API.YANDEX_FOLDER_ID}/$aiModel"),
                        temperature = temperature,
                        maxTokens = maxTokens,
                        messages = ChatHistory.allChatList
                    ),
                    requestOptions = RequestOptions(
                        headers = mapOf(
                            "OpenAI-Project" to API.YANDEX_FOLDER_ID
                        )
                    ),
                )
            val content = response.choices
                .firstOrNull()
                ?.message
                ?.content

            if (!content.isNullOrBlank()) {
                ChatHistory.addMessage(ChatRole.Assistant, content)
                return "### ${aiModel.substringBefore("/")}" + "  \n" + content
            } else {
                ChatHistory.removeLastMessage()
                return "Ошибка: API вернул пустой ответ"
            }
        } catch (e: Exception) {
            ChatHistory.removeLastMessage()
            return "Ошибка: ${e.message}"
        }
    }
}

object AI {
    val ai = AiClient()
}