package com.example.synapseapp.ui.theme.fargments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import data.AiAnswerClass
import data.UserChatMessages
import data.UserTextStateHolder


@Composable
fun TextFieldBottomBox(
    modifier: Modifier,
    navController: NavController,
    userChatMessages: UserChatMessages,
    aiAnswer: AiAnswerClass
){
    val state = UserTextStateHolder.objectOfUserTextState
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(modifier = modifier
        .background(
            color = MaterialTheme.colorScheme.surface,
            RoundedCornerShape(40.dp))
        .height(60.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(5.dp))
            IconButton(
                onClick = {}
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
                        userChatMessages.chatHandler(
                            aiAnswer,
                            userChatMessages,
                            navController
                        )
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
}