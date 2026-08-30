package data

import android.os.Handler
import android.os.Looper
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
import data.AiAnswerClass

class UserChatMessages() : ViewModel() {
    var currentControlIcon by mutableStateOf(ChatIconState.SPEAK)
    val state = UserTextStateHolder.objectOfUserTextState
    val userAndAiMessages = mutableStateListOf<ChatMessageBox>()



    fun chatHandler(aiAnswer :AiAnswerClass, userChatMessages: UserChatMessages,navController: NavController) {
        viewModelScope.launch(Dispatchers.IO) {
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
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate(Routes.SPEAK_SCREEN)
                    }, 300)
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
        viewModelScope.launch(Dispatchers.IO) {
            val state = UserTextStateHolder.objectOfUserTextState
            val answer = AI.ai.sendMessage(state.userMessages.last())
            viewModelObj.userAndAiMessages.add(
                0,
                ChatMessageBox(
                    id = UUID.randomUUID().toString(),
                    role = IdentifyRole.AI,
                    type = IdentifyTypeMessage.TEXT,
                    text = answer
                )
            )
        }
    }
}

