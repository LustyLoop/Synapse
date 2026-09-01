package com.example.synapseapp.ui.theme.screens


import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.synapseapp.ui.theme.fargments.SampleScreen
import compose.icons.TablerIcons
import compose.icons.tablericons.Microphone
import compose.icons.tablericons.MicrophoneOff
import compose.icons.tablericons.Video
import compose.icons.tablericons.X
import data.AiAnswerClass
import data.AllShaders
import data.Shaders
import data.UserChatMessages
import data.UserTextStateHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import viewModel.GadgetInfo

@Composable
fun SpeakScreen(
    navController: NavController,
    gadget: GadgetInfo
) {
    val scope = rememberCoroutineScope()
    Shaders.currentShader = AllShaders.Ball
    SampleScreen(
        navController = navController,
        BottomBox = ::SpeakBottomBox,
        gadget = gadget,
        drawerState = null
    )
    BackHandler(enabled = true) {
        ShaderFlags.transitionFlag = false
        scope.launch {
            delay(240)
            navController.popBackStack()

        }
    }

}

@Composable
fun SpeakBottomBox(
    modifier: Modifier,
    navController: NavController,
    userChatMessages: UserChatMessages,
    aiAnswer: AiAnswerClass
) {
    val state = UserTextStateHolder.objectOfUserTextState
    val keyboardController = LocalSoftwareKeyboardController.current
    var backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val padding = 3.dp

    DisposableEffect(Unit) {
        onDispose {
            backDispatcher = null
        }
    }

    Box(
        modifier = modifier
            .background(color = Color.Transparent)
            .height(65.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxHeight()
                    .size(30.dp)
                    .weight(1f)
                    .padding(
                        start = padding,
                        end = padding
                    ),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = TablerIcons.Video,
                    contentDescription = "Меню",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxHeight()
                    .size(30.dp)
                    .weight(1f)
                    .padding(
                        start = padding,
                        end = padding
                    ),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Меню",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            var currentMicIcon by remember { mutableStateOf(TablerIcons.Microphone) }
            Button(
                onClick = {
                    currentMicIcon =
                        if (currentMicIcon == TablerIcons.Microphone) TablerIcons.MicrophoneOff
                        else TablerIcons.Microphone
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .size(30.dp)
                    .weight(1f)
                    .padding(
                        start = padding,
                        end = padding
                    ),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = currentMicIcon,
                    contentDescription = "Меню",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Button(
                onClick = { backDispatcher?.onBackPressed() },
                modifier = Modifier
                    .fillMaxHeight()
                    .size(30.dp)
                    .weight(1f)
                    .padding(
                        start = padding,
                        end = padding
                    ),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = TablerIcons.X,
                    contentDescription = "Меню",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }


        }
    }
}