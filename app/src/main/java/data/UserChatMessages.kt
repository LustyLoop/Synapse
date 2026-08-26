package data

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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


class UserChatMessages(): ViewModel(){
    var currentControlIcon by mutableStateOf(ChatIconState.SPEAK)
    val state =  UserTextStateHolder.objectOfUserTextState
    val userAndAiMessages = mutableStateListOf<ChatMessageBox>()


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
    }


    @Composable
    fun IconHandler(){
        currentControlIcon = if(state.userText.isEmpty()){
            ChatIconState.SPEAK
        } else{
            ChatIconState.SEND
        }
    }
}

fun aiAnswer(viewModelObj: UserChatMessages){
    val testAii = """
# Заголовок первого уровня (h1)

## Заголовок второго уровня (h2)

### Заголовок третьего уровня (h3)

#### Заголовок четвёртого уровня (h4)

##### Заголовок пятого уровня (h5)

###### Заголовок шестого уровня (h6)

---

## Обычный текст и форматирование

Это обычный параграф текста. Он демонстрирует, как выглядит стандартный текст в вашей теме.

**Жирный текст** и *курсивный текст*. Можно их комбинировать: ***жирный курсив***.

~~Зачёркнутый текст~~ (поддерживается GFM).

Это `встроенный код` в предложении.

## Ссылки

[Обычная ссылка на GitHub](https://github.com/mikepenz/multiplatform-markdown-renderer)

[Ссылка с заголовком](https://github.com "Подсказка при наведении")

## Списки

### Нумерованный список
1. Первый пункт
2. Второй пункт
   1. Вложенный пункт 2.1
   2. Вложенный пункт 2.2
3. Третий пункт

### Маркированный список
- Пункт 1
- Пункт 2
  - Вложенный пункт 2.1
  - Вложенный пункт 2.2
- Пункт 3

### Чекбоксы (GFM Task Lists)
- [x] Выполненная задача
- [ ] Невыполненная задача
- [ ] Ещё одна задача

## Цитаты

> Это обычная цитата.
> Она может занимать несколько строк.

> [!NOTE]
> Это GitHub Alert типа NOTE. Полезная информация.

> [!TIP]
> Это GitHub Alert типа TIP. Совет, как сделать лучше.

> [!IMPORTANT]
> Это GitHub Alert типа IMPORTANT. Ключевая информация.

> [!WARNING]
> Это GitHub Alert типа WARNING. Срочное предупреждение.

> [!CAUTION]
> Это GitHub Alert типа CAUTION. Осторожно, возможны проблемы.

## Код

### Встроенный код
Для вывода переменной используйте `println("Hello")`.

### Блок кода (без подсветки)
    """.trimIndent()

    viewModelObj.userAndAiMessages.add(0,
        ChatMessageBox(
            role = IdentifyRole.AI,
            type = IdentifyTypeMessage.TEXT,
            text = testAii
        )
    )
}

