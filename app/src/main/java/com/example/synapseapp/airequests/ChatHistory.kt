package com.example.synapseapp.airequests


import androidx.compose.runtime.mutableStateListOf
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.chat.ChatMessage



object ChatHistory {
    private val _allChatList =  mutableStateListOf<ChatMessage>(
        ChatMessage(role = ChatRole.System, content = PROMPT)
    )
    val allChatList = _allChatList

    fun addMessage(role: ChatRole,message: String?) {
        _allChatList.add(ChatMessage(role = role, content = message))
    }

    fun clear() {
        _allChatList.clear()
    }

    fun removeMessage(message: ChatMessage) {
        _allChatList.remove(message)

    }
    fun setPrompt(prompt: String){
        _allChatList[0] = ChatMessage(role = ChatRole.System, content = prompt)
    }
}

