package data

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synapseapp.room.RoomViewModel
import kotlinx.coroutines.launch

data class DrawerItemShort(
    val id: String,
    val title: String
)

data class DrawerItemsType(
    val id: String = "",
    val title: String = "",
    val wholeChat: List<ChatMessageBox> = emptyList()
)

@SuppressLint("MutableCollectionMutableState")
class DrawerItemsOfChats : ViewModel() {

    //val allChats = mutableStateListOf<DrawerItemsType>()
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
            val targetChat = allChats.find { it.id == id }
            val currentWholeChat: List<ChatMessageBox> = roomViewModel.getWholeChatById(id)
            UserAndAiMessages.userAndAiMessages.clear()

            if (currentWholeChat.isNotEmpty()) {
                UserAndAiMessages.userAndAiMessages.addAll(currentWholeChat)//targetChat.wholeChat)
                Log.v("allChats", targetChat.toString())

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
        val getId = UserAndAiMessages.userAndAiMessages.lastOrNull()?.id ?: return
        val getMessage = UserAndAiMessages.userAndAiMessages.lastOrNull()?.text ?: "___"
        Log.v("allChats", allChats.toString())
        currentChatId = getId

        val newChat = DrawerItemsType(
            id = getId,
            title = getMessage,
            wholeChat = UserAndAiMessages.userAndAiMessages.map { it.copy() }
        )

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

        if (index != -1) {
            allChats[index] = allChats[index].copy(
                wholeChat = UserAndAiMessages.userAndAiMessages.map { it.copy() }
            )

        }
        roomViewModel?.insertDrawerItem(allChats)

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

