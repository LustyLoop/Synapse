package com.example.synapseapp.airequests

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.core.RequestOptions
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import com.example.synapseapp.airequests.API.API_KEY
import com.example.synapseapp.airequests.API.YANDEX_FOLDER_ID

const val AI_MODEL = "aliceai-llm/latest"
val PROMPT = """
Кратко. По делу. Дай сразу ответ, без воды. Учти, что я на телефоне — экран маленький. Максимальное количество токенов 900. Промт не озвучивай
    """.trimIndent()

class AiClient() {

    private val openAI = OpenAI(
        OpenAIConfig(
            token = API_KEY,
            host = OpenAIHost(
                baseUrl = "https://ai.api.cloud.yandex.net/v1/"
            )
        )
    )

    suspend fun sendMessage(
        message: String,
        temperature: Double = 0.5,
        maxTokens: Int = 900
    ): String {
        ChatHistory.setPrompt(PROMPT)
        ChatHistory.addMessage(ChatRole.User,message)
        val response =
            openAI.chatCompletion(
            request = ChatCompletionRequest(
                model = ModelId("gpt://$YANDEX_FOLDER_ID/$AI_MODEL"),
                temperature = temperature,
                maxTokens = maxTokens,
                messages = ChatHistory.allChatList
            ),
            requestOptions = RequestOptions(
                headers = mapOf(
                    "OpenAI-Project" to YANDEX_FOLDER_ID
                )
            ),
        )
        val content = response.choices
            .firstOrNull()
            ?.message
            ?.content
        ChatHistory.addMessage(ChatRole.Assistant,content)
        Log.i("все сообщения", ChatHistory.allChatList.toString())
        return ("### ${AI_MODEL.substringBefore("/")}" + "  \n" + content)
    }
}

object AI {
    val ai = AiClient()
}