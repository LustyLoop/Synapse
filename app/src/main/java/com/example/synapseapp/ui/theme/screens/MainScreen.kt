package com.example.synapseapp.ui.theme.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.synapseapp.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.zIndex
import data.IdentifyRole
import data.UserChatMessages
import data.UserTextStateHolder
import shaders.DarkShaderBackground
import kotlin.random.Random
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.synapseapp.ui.theme.AIMessageBackgroundColor
import com.mikepenz.markdown.m3.Markdown
import data.AllShaders
import data.Shaders
import shaders.ballShader
import shaders.LightShaderBackground
import viewModel.GadgetInfo
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.rememberMarkdownState
import data.AiAnswerClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedScreen(
    navController: NavController,
    userChatMessages: UserChatMessages = viewModel(),
    aiAnswer: AiAnswerClass = viewModel(),
    gadget: GadgetInfo = viewModel(),
    showTextInputField: Boolean,
) {
    val state = UserTextStateHolder.objectOfUserTextState
    val hideAndShowProgress by animateFloatAsState(
        targetValue = if (state.hideShaderFlag.value) {
            0.5f
        } else {
            0f
        },
        animationSpec = tween(durationMillis = 1000),
        label = "HideAnimation"
    )

    val ballShader = remember { ballShader }
    var time by remember { mutableFloatStateOf(0f) }
    val rand = remember { Random.nextFloat() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val shaderBackground =
        if (isSystemInDarkTheme()) remember { DarkShaderBackground }
        else remember { LightShaderBackground }
    LaunchedEffect(Unit) {
        val start = withFrameNanos { it }
        while (true) {
            withFrameNanos { frameTime ->
                time = (frameTime - start) / 1_000_000_000f
            }
        }
    }

    userChatMessages.IconHandler()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .then(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (Shaders.currentShader == AllShaders.Wave) {
                        Modifier.drawWithCache {
                            shaderBackground?.setFloatUniform(
                                "resolution",
                                size.width,
                                size.height
                            )
                            shaderBackground?.setFloatUniform("rand", rand)
                            shaderBackground?.setFloatUniform(
                                "hideAndShowProgress",
                                hideAndShowProgress
                            )
                            onDrawBehind {
                                shaderBackground?.setFloatUniform("time", time)
                                drawRect(brush = ShaderBrush(shaderBackground as Shader))
                            }
                        }
                    } else {
                        Modifier.drawWithCache {
                            ballShader?.setFloatUniform(
                                "resolution",
                                size.width,
                                size.height
                            )
                            onDrawBehind {
                                ballShader?.setFloatUniform("time", time)
                                drawRect(brush = ShaderBrush(ballShader as Shader))
                            }
                        }
                    }
                } else {
                    Modifier.background(MaterialTheme.colorScheme.onPrimary)
                }

            )

    ) {

        /*-----------------------------------------------------------------------------*/
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.onPrimary,
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0f)
                        )
                    )
                )
        )
        Button(
            onClick = {},
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 16.dp,
                    end = 10.dp
                )
                .height(55.dp)
                .width(110.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "${gadget.batteryCharge}%",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 49.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        /*-----------------------------------------------------------------------------*/
        Button(
            onClick = {
                gadget.batteryCharge += 1
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 16.dp,
                    start = 20.dp
                )
                .size(55.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface)
        ) {
            Icon(
                imageVector = Icons.Rounded.Menu,
                contentDescription = "Меню",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        /*-----------------------------------------------------------------------------*/

        Button(
            onClick = {},
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 16.dp,
                    end = 66.dp
                )
                .size(55.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
        ) {
            Image(
                painter = painterResource(R.drawable.glasses_button_ic),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background)
            )
        }
        /*-----------------------------------------------------------------------------*/


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(-1f),
            contentPadding = PaddingValues(
                top = 70.dp,
                bottom = 105.dp,
                start = 5.dp
            ),
            reverseLayout = true
        ) {
            items(
                items = userChatMessages.userAndAiMessages,
                key = {
                    Log.i("id", "System.identityHashCode(it): ${System.identityHashCode(it)}")
                    System.identityHashCode(it)
                }
            ) { box ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (box.role) {
                        IdentifyRole.USER -> {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(
                                        top = 8.dp,
                                        end = 15.dp,
                                        start = 40.dp
                                    )
                                    .background(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        RoundedCornerShape(20.dp)
                                    )
                            ) {
                                SelectionContainer {
                                    Text(
                                        modifier = Modifier.padding(15.dp),
                                        text = box.text,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        IdentifyRole.AI -> {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(top = 5.dp, start = 0.dp, end = 5.dp)
                                    .background(
                                        color = AIMessageBackgroundColor,
                                        RoundedCornerShape(20.dp)
                                    )
                            ) {

                                SelectionContainer {
                                    val markdownState = rememberMarkdownState(
                                        content = box.text,
                                        retainState = true,
                                        immediate = true // Парсим сразу, убирая белые экраны
                                    )

                                    Markdown(
                                        modifier = Modifier.padding(5.dp),
                                        markdownState = markdownState, // Оставляем ТОЛЬКО стейт
                                        colors = markdownColor(
                                            text = MaterialTheme.colorScheme.onSurface,
                                            codeBackground = Color(0xFF2D2D2D),
                                            dividerColor = Color.Gray,
                                        ),
                                        typography = markdownTypography(
                                            h1 = MaterialTheme.typography.headlineLarge.copy(
                                                fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                                            ),
                                            h2 = MaterialTheme.typography.headlineMedium.copy(
                                                fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                                            ),
                                            h3 = MaterialTheme.typography.headlineMedium.copy(
                                                fontFamily = MaterialTheme.typography.titleSmall.fontFamily
                                            ),
                                            text = MaterialTheme.typography.bodyLarge.copy(
                                                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily
                                            ),
                                            inlineCode = MaterialTheme.typography.bodyMedium.copy(
                                                fontFamily = FontFamily.Monospace,
                                                color = Color.White
                                            ),
                                            quote = MaterialTheme.typography.bodyMedium.copy(
                                                fontStyle = FontStyle.Italic
                                            ),
                                            code = MaterialTheme.typography.bodyMedium.copy(
                                                fontStyle = FontStyle.Italic,
                                                color = Color.White
                                            ),
                                        )
                                    )

                                }
                            }
                        }

                        else -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                                    .background(
                                        Color.White,
                                        RoundedCornerShape(20.dp)
                                    )
                            ) {
                                Text(
                                    modifier = Modifier.padding(15.dp),
                                    text = "Нет информации",
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }

        }


        if (showTextInputField)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .imePadding()
                    .fillMaxWidth()
                    .padding(
                        bottom = 10.dp,
                        start = 20.dp,
                        end = 20.dp
                    )
                    .height(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(40.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically

                ) {

                    Spacer(modifier = Modifier.width(5.dp))
                    IconButton(
                        onClick = {
                            aiAnswer.aiAnswerHandler(userChatMessages)

                            //gadget.batteryCharge += 1
                        }
                    )
                    {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Прикрепить файл",
                            modifier = Modifier
                                .size(30.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.width(3.dp))

                    BasicTextField(
                        value = state.userText,
                        onValueChange = { state.userText = it },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .weight(1f),
                        decorationBox = { innerTextField ->
                            Box {
                                if (state.userText.isEmpty()) {
                                    Text(
                                        text = "Задайте ваш вопрос...",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(13.dp))
                    Button(
                        onClick = {
                            keyboardController?.hide()
                            userChatMessages.chatHandler(navController)
                        },
                        modifier = Modifier
                            .size(35.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    )
                    {
                        Icon(
                            imageVector = userChatMessages.currentControlIcon.imageVector,
                            contentDescription = userChatMessages.currentControlIcon.contentDescription,
                            modifier = Modifier
                                .size(27.dp),
                            tint = MaterialTheme.colorScheme.background
                        )
                    }
                    Spacer(modifier = Modifier.width(17.dp))
                }
            }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(120.dp)
                .zIndex(-1f)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0f),
                            MaterialTheme.colorScheme.onPrimary
                        )
                    )
                )

        )
    }
}

@Composable
fun MainScreen(navController: NavController) {
    Shaders.currentShader = AllShaders.Wave
    SharedScreen(navController, showTextInputField = true)
}