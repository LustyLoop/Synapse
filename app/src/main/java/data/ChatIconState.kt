package data

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Stop

enum class ChatIconState(val imageVector: ImageVector, val contentDescription: String) {
    SPEAK(Icons.Rounded.GraphicEq, "Режим разговора"),
    SEND(Icons.Rounded.ArrowUpward, "Отправить сообщение"),
    STOP(Icons.Rounded.Stop, "Стоп")
}
