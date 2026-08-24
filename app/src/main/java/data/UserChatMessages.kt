package data

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.glassesapp.navigation.Routes


class UserChatMessages(): ViewModel(){
    var currentControlIcon by mutableStateOf(ChatIconState.SPEAK)
    val state =  UserTextStateHolder.objectOfUserTextState
    val userAndAiMessages = mutableStateListOf<ChatMessageBox>()


    fun chatHandler(viewModelObj: UserChatMessages, navController: NavController){
        when (currentControlIcon) {
            ChatIconState.SEND -> {
                state.hideShaderFlag.value = true
                state.userMessages.add(state.userText)
                userAndAiMessages.add(0,
                    ChatMessageBox(
                        role = IdentifyRole.USER,
                        type = IdentifyTypeMessage.TEXT,
                        text = state.userMessages.last()
                    )
                )

                state.userText = ""
                Log.i("chatHandler", "userText: $state.userText")
                Log.i("chatHandler", "userMessages: ${state.userMessages}")
                Log.i("chatHandler", "userAndAiMessages: $userAndAiMessages")
            }
            ChatIconState.SPEAK -> {
                state.hideShaderFlag.value = true
                Handler(Looper.getMainLooper()).postDelayed({
                    navController.navigate(Routes.SPEAK_SCREEN)
                }, 300)
            }
            ChatIconState.STOP -> {
                //"Реализовать стоп"
            }
        }
    }

    @Composable
    fun iconHandler(){
        currentControlIcon = if(state.userText.isEmpty()){
            ChatIconState.SPEAK
        } else{
            ChatIconState.SEND
        }
    }
}


