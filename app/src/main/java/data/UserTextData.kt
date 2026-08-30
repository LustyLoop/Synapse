package data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UserTextClass(): ViewModel() {
    val userMessages = mutableStateListOf<String>()
    var userText by mutableStateOf("")
    var hideShaderFlag = mutableStateOf<Boolean>(true)
}
object UserTextStateHolder{
    val objectOfUserTextState = UserTextClass()
}