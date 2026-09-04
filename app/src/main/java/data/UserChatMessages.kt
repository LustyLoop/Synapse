package data

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
import com.example.synapseapp.ui.theme.screens.ShaderFlags
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class UserChatMessages() : ViewModel() {
    var currentControlIcon by mutableStateOf(ChatIconState.SPEAK)
    val state = UserTextStateHolder.objectOfUserTextState
    val userAndAiMessages = mutableStateListOf<ChatMessageBox>()

    fun chatHandler(
        aiAnswer: AiAnswerClass,
        userChatMessages: UserChatMessages,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            when (currentControlIcon) {
                ChatIconState.SEND -> {
                    state.hideShaderFlag.value = true
                    state.userMessages.add(state.userText)
                    userAndAiMessages.add(
                        0,
                        ChatMessageBox(
                            id = UUID.randomUUID().toString(),
                            role = IdentifyRole.USER,
                            type = IdentifyTypeMessage.TEXT,
                            text = state.userMessages.last()
                        )
                    )

                    state.userText = ""
                    aiAnswer.aiAnswerHandler(userChatMessages)
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

class AiAnswerClass() : ViewModel() {


    fun aiAnswerHandler(viewModelObj: UserChatMessages) {
        viewModelScope.launch {

            val messageId = UUID.randomUUID().toString()
            var aiMessage = ChatMessageBox(
                id = messageId,
                role = IdentifyRole.AI,
                type = IdentifyTypeMessage.TEXT,
                text = "",
                errorFlag = false
            )
            viewModelObj.userAndAiMessages.add(0, aiMessage)
            if (aiMessage.text.isEmpty()) {
                launch {

                }
            }
            val result = AI.ai.sendMessage(
                message = UserTextStateHolder.objectOfUserTextState.userMessages.last()
            ) { text ->

                aiMessage = aiMessage.copy(text = text)

                val index = viewModelObj.userAndAiMessages
                    .indexOfFirst { it.id == messageId }

                if (index != -1) {
                    viewModelObj.userAndAiMessages[index] = aiMessage
                }
            }

            if (result.isFailure) {
                aiMessage = aiMessage.copy(
                    text = "Ошибка: ${result.exceptionOrNull()?.message}",
                    errorFlag = true
                )

                val index = viewModelObj.userAndAiMessages
                    .indexOfFirst { it.id == messageId }

                if (index != -1) {
                    viewModelObj.userAndAiMessages[index] = aiMessage
                }
            }
        }
    }
}

