package data

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatRole
import com.example.synapseapp.airequests.ChatHistory
import com.example.synapseapp.room.RoomViewModel
import kotlinx.coroutines.launch

data class DrawerItemsType(
    val id: String,
    val title: String
)

@SuppressLint("MutableCollectionMutableState")
class DrawerItemsOfChats : ViewModel() {

    val allChats = mutableStateListOf<DrawerItemsType>()
    fun setAllChats(roomViewModel: RoomViewModel? = null) {
        roomViewModel?.getShortItems(allChats)
    }
    private var currentChatId: String? = null
    private val shaderState = UserTextStateHolder.objectOfUserTextState.hideShaderFlag

    @SuppressLint("SuspiciousIndentation")
    fun showSelectedChat(id: String, roomViewModel: RoomViewModel) {
        if (!shaderState.value) {
            shaderState.value = true
        }
        currentChatId = id
        viewModelScope.launch {
            //val targetChat = allChats.find { it.id == id }
            UserAndAiMessages.userAndAiMessages.clear()
            ChatHistory.clear()
            val currentWholeChat: List<ChatMessageBox> = roomViewModel.getWholeChatById(id)

            if (currentWholeChat.isNotEmpty()) {
                UserAndAiMessages.userAndAiMessages.addAll(currentWholeChat)
            } else {
                UserAndAiMessages.userAndAiMessages.add(
                    ChatMessageBox(
                        id = "",
                        role = IdentifyRole.AI.name,
                        type = IdentifyTypeMessage.TEXT.name,
                        text = "ERROR",
                        errorFlag = true
                    )
                )
            }
        }
    }

    fun loadTitleForDrawer() {
        val getId = UserAndAiMessages.userAndAiMessages.lastOrNull()?.id
            ?: return
        val getMessage = UserAndAiMessages.userAndAiMessages.lastOrNull()?.text
            ?: "___"
        currentChatId = getId
        ChatHistory.clear()

        val wholeCurrentChat = UserAndAiMessages.userAndAiMessages.map { it.role to it.text }
        val newChat = DrawerItemsType(
            id = getId,
            title = getMessage
        )
        wholeCurrentChat.forEach { pair ->
            ChatHistory.addMessage(
                role =
                    when (pair.first) {
                        "USER" -> ChatRole.User
                        "AI" -> ChatRole.Assistant
                        "SYSTEM" -> ChatRole.System
                        else -> ChatRole.User
                    }, message = pair.second
            )
        }
        val existingIndex = allChats.indexOfFirst { it.id == getId }
        if (existingIndex != -1) {
            allChats[existingIndex] = newChat
        } else {
            allChats.add(newChat)
        }
    }

    private var lastWriteTimestamp: Long = 0L
    fun updateWholeChat(roomViewModel: RoomViewModel? = null) {
        val activeId = currentChatId ?: return
        val index = allChats.indexOfFirst { it.id == activeId }
        val currentMessages = UserAndAiMessages.userAndAiMessages.toList()

        if (index != -1) {
            val title = allChats[index].title
            roomViewModel?.insertDrawerItem(activeId, title, currentMessages)
        }

        val currentTime = System.currentTimeMillis()
        if (lastWriteTimestamp != 0L) {
            val timePassedMs = currentTime - lastWriteTimestamp
            val timePassedSec = timePassedMs / 1000.0
            Log.d(
                "RoomLogging",
                "Запись в базу. Прошло времени с прошлой записи: $timePassedSec сек ($timePassedMs мс)"
            )
        } else {
            Log.d("RoomLogging", "Первая запись в базу в этой сессии.")
        }
        lastWriteTimestamp = currentTime
    }


}

