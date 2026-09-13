package data

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.synapseapp.airequests.AI
import com.example.synapseapp.navigation.Routes
import com.example.synapseapp.room.RoomViewModel
import com.example.synapseapp.ui.theme.screens.ShaderFlags
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.jvm.java

@SuppressLint("MutableCollectionMutableState")
object UserAndAiMessages: ViewModel() {
    val userAndAiMessages = mutableStateListOf<ChatMessageBox>()


    private val shaderState = UserTextStateHolder.objectOfUserTextState.hideShaderFlag
    fun createNewChat(){
        userAndAiMessages.clear()
        shaderState.value = false
    }
}

class UserChatMessages: ViewModel() {
    private val state = UserTextStateHolder.objectOfUserTextState
    var currentControlIcon by mutableStateOf(ChatIconState.SPEAK)


    fun chatHandler(
        aiAnswer: AiAnswerClass,
        userChatMessages: UserChatMessages,
        navController: NavController,
        drawerItemsOfChats: DrawerItemsOfChats?,
        roomViewModel: RoomViewModel?
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            when (currentControlIcon) {
                ChatIconState.SEND -> {
                    val dialog = UserAndAiMessages.userAndAiMessages
                    state.hideShaderFlag.value = true
                    dialog.add(
                        0,
                        ChatMessageBox(
                            id = UUID.randomUUID().toString(),
                            role = IdentifyRole.USER.name,
                            type = IdentifyTypeMessage.TEXT.name,
                            text = state.userText
                        )
                    )
                    if (dialog.size == 1) {
                        drawerItemsOfChats?.loadTitleForDrawer()
                    }

                    aiAnswer.aiAnswerHandler(
                        viewModelObj = userChatMessages,
                        state = state,
                        drawerItemsOfChats = drawerItemsOfChats,
                        roomViewModel = roomViewModel
                        )
                    drawerItemsOfChats?.updateWholeChat(roomViewModel)
                }

                ChatIconState.SPEAK -> {
                    ShaderFlags.transitionFlag = true
                    delay(240)
                    navController.navigate(Routes.SPEAK_SCREEN)
                }
                ChatIconState.STOP -> {
                    //"Реализовать стоп"
                }
            }
        }
    }


    @Composable
    fun IconHandler() {
        currentControlIcon = if (state.userText.isEmpty()) {
            ChatIconState.SPEAK
        } else {
            ChatIconState.SEND
        }
    }
}

class AiAnswerClass : ViewModel() {
    private fun indexHandler(
        messageId: String,
        aiMessage: ChatMessageBox
    ){
        val index = UserAndAiMessages.userAndAiMessages
            .indexOfFirst { it.id == messageId }

        if (index != -1) {
            UserAndAiMessages.userAndAiMessages[index] = aiMessage
        }
    }

    fun aiAnswerHandler(
        viewModelObj: UserChatMessages,
        state: UserTextClass,
        drawerItemsOfChats: DrawerItemsOfChats?,
        roomViewModel: RoomViewModel?
    ) {
        viewModelScope.launch {
            val messageId = UUID.randomUUID().toString()
            var aiMessage = ChatMessageBox(
                id = messageId,
                role = IdentifyRole.AI.name,
                type = IdentifyTypeMessage.TEXT.name,
                text = "",
                errorFlag = false
            )
            UserAndAiMessages.userAndAiMessages.add(0, aiMessage)
            if (aiMessage.text.isEmpty()) {
                launch {

                }
            }
            val localRequest = state.userText
            state.userText = ""
            val result = AI.ai.sendMessage(
                message = localRequest,
                drawerItemsOfChats = drawerItemsOfChats,
                roomViewModel = roomViewModel
            )
            { text ->

                aiMessage = aiMessage.copy(text = text)

                indexHandler(messageId, aiMessage)
            }
            if (result.isFailure) {
                aiMessage = aiMessage.copy(
                    text = "Ошибка: ${result.exceptionOrNull()?.message}",
                    errorFlag = true
                )
                indexHandler(messageId, aiMessage)
            }
        }
    }
}


