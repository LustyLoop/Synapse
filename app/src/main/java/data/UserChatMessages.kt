package data

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.synapseapp.navigation.Routes
import com.mikepenz.markdown.model.rememberMarkdownState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserChatMessages() : ViewModel() {
    var currentControlIcon by mutableStateOf(ChatIconState.SPEAK)
    val state = UserTextStateHolder.objectOfUserTextState
    val userAndAiMessages = mutableStateListOf<ChatMessageBox>()

    var currentId by mutableIntStateOf(0)


    fun chatHandler(navController: NavController) {
        viewModelScope.launch(Dispatchers.IO) {
            when (currentControlIcon) {
                ChatIconState.SEND -> {
                    state.hideShaderFlag.value = true
                    state.userMessages.add(state.userText)
                    userAndAiMessages.add(
                        0,
                        ChatMessageBox(
                            role = IdentifyRole.USER,
                            type = IdentifyTypeMessage.TEXT,
                            text = state.userMessages.last()
                        )
                    )
                    state.userText = ""
                    //Log.i("size", "userAndAiMessages.size: ${userAndAiMessages.size}")

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
    val testAii = """
# Пример Markdown-документа

Это демонстрационный файл, который содержит различные элементы разметки и блоки кода для проверки работы рендерера.

## Списки и форматирование

Вы можете использовать **жирный текст** или *курсив*, а также создавать списки:
* Первое важное правило оптимизации.
* Второе правило — уводить вычисления в фон.
* Поддержка `inline-кода` прямо внутри строки.

---

## Примеры исходного кода

Ниже представлены блоки кода с указанием языка для корректной подсветки синтаксиса.

### 1. Корутина в Jetpack Compose (Kotlin)

Этот код демонстрирует, как правильно запускать фоновые вычисления, чтобы они на 100% не блокировали основной UI-поток приложения:

```kotlin
// Запуск корутины на фоновом диспетчере
LaunchedEffect(Unit) {
    withContext(Dispatchers.Default) {
        val startNanos = System.nanoTime()
        while (isActive) {
            val currentNanos = System.nanoTime()
            // Безопасный расчет времени для шейдера
            time = (currentNanos - startNanos) / 1_000_000_000f
            delay(16) // Ограничение ~60 FPS
        }
    }
}
```

### 2. Код Android Runtime Shader (AGSL)

А это пример простого шейдера, который принимает время и координаты, а затем плавно меняет цвет пикселей на GPU:

```glsl
uniform float2 iResolution;
uniform float iTime;

half4 main(in float2 fragCoord) {
    // Нормализация координат от 0.0 до 1.0
    float2 uv = fragCoord / iResolution.xy;
    
    // Плавная анимация цвета на основе синусоиды от времени
    float red = 0.5 + 0.5 * sin(iTime + uv.x * 5.0);
    float green = 0.5 + 0.5 * cos(iTime + uv.y * 5.0);
    
    return half4(red, green, 0.7, 1.0);
}
```

> **Важное примечание:** Блоки кода выделяются тройными обратными апострофами (`` ` ``). Сразу после первых трех апострофов указывается имя языка (например, `kotlin` или `glsl`), чтобы рендерер применил правильные правила цветовой схемы.

    """.trimIndent()

    fun aiAnswerHandler(viewModelObj: UserChatMessages) {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelObj.userAndAiMessages.add(
                0,
                ChatMessageBox(
                    role = IdentifyRole.AI,
                    type = IdentifyTypeMessage.TEXT,
                    text = testAii
                )
            )
        }
    }
}

