package data

import kotlinx.serialization.Serializable


enum class IdentifyRole{
    USER,
    AI,
    NULL
}

enum class IdentifyTypeMessage{
    IMAGE,
    TEXT,
    NULL
}

data class ChatMessageBox(
    var id: String = "",
    val role: String = IdentifyRole.NULL.name,
    val type: String = IdentifyTypeMessage.NULL.name,
    val text: String = "",
    val imageSrc: Int? = null,
    var errorFlag: Boolean = false
)
