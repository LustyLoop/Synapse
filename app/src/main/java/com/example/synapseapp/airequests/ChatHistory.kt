package com.example.synapseapp.airequests


import androidx.compose.runtime.mutableStateListOf
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.chat.ChatMessage


object ChatHistory {

    private val _allChatList = mutableStateListOf<ChatMessage>(
    )
    val allChatList = _allChatList

    fun addMessage(role: ChatRole, message: String?) {
        _allChatList.add(ChatMessage(role = role, content = message))
    }

    fun clear() {
        _allChatList.clear()
    }

    fun removeMessage(message: ChatMessage) {
        _allChatList.remove(message)

    }

    fun removeLastMessage() {
        if (_allChatList.isNotEmpty()) {
            _allChatList.removeAt(_allChatList.lastIndex)
        }
    }

    fun setPrompt(prompt: String) {
        val systemMessage = ChatMessage(
            role = ChatRole.System,
            content = prompt
        )
        AI.myPrompt = prompt

        if (_allChatList.isEmpty()) {
            _allChatList.add(systemMessage)
        } else {
            _allChatList[0] = systemMessage
        }
    }
}

