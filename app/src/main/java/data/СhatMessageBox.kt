package data

import androidx.lifecycle.ViewModel

enum class IdentifyRole(){
    USER,
    AI,
    NULL
}
enum class IdentifyTypeMessage(){
    IMAGE,
    TEXT,
    NULL
}

data class ChatMessageBox(
    var id: String = "",
    val role: IdentifyRole = IdentifyRole.NULL,
    val type: IdentifyTypeMessage = IdentifyTypeMessage.NULL,
    val text: String,
    val imageSrc: Int? = null,
    var errorFlag: Boolean = false
) : ViewModel()
