package com.example.synapseapp.ui.theme.fargments

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.synapseapp.R
import com.example.synapseapp.navigation.Routes
import com.example.synapseapp.room.RoomViewModel
import com.example.synapseapp.ui.theme.AIMessageBackgroundColor
import com.example.synapseapp.ui.theme.AiMessageMarkdown
import data.AiAnswerClass
import data.DrawerItemsOfChats
import data.IdentifyRole
import data.UserAndAiMessages
import data.UserChatMessages
import kotlinx.coroutines.launch
import viewModel.GadgetInfo


//@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleScreen(
    navController: NavController,
    userChatMessages: UserChatMessages = viewModel(),
    aiAnswer: AiAnswerClass = viewModel(),
    gadget: GadgetInfo,
    drawerState: DrawerState?,
    wideScreenFlag: Boolean,
    drawerItemsOfChats: DrawerItemsOfChats? = null,
    roomViewModel: RoomViewModel? = null,
    BottomBox: @Composable (
        modifier: Modifier,
        navController: NavController,
        userChatMessages: UserChatMessages,
        aiAnswer: AiAnswerClass,
        wideScreenFlag: Boolean,
        drawerItemsOfChats: DrawerItemsOfChats?,
        roomViewModel: RoomViewModel?
    ) -> Unit
) {
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    var isModelPickerVisible by remember { mutableStateOf(false) }
    userChatMessages.IconHandler()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(color = Color.Transparent)
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
                    top = 13.dp,
                    end = 12.dp
                )
                .height(52.dp)
                .width(104.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "${gadget.batteryCharge}%",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 46.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        /*-----------------------------------------------------------------------------*/

        Button(
            onClick = {
                //gadget.batteryCharge += 1
                scope.launch {
                    keyboardController?.hide()
                    drawerState?.open()
                }

            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 13.dp,
                    start = 12.dp
                )
                .size(52.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface)
        ) {
            Image(
                modifier = Modifier
                    .size(27.dp),
                painter = painterResource(R.drawable.menu_ic),
                contentDescription = "Меню",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
            )
        }
        Box {
            TextButton(
                onClick = { isModelPickerVisible = !isModelPickerVisible },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .padding(start = (52 + 14).dp, top = 13.dp)
                    .height(52.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Выбор модели",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 6.dp)
                )
                AIModelPicker(
                    expanded = isModelPickerVisible,
                    onDismiss = { isModelPickerVisible = false },
                    onItemClick = { println("lool") }
                )
            }
        }

        /*-----------------------------------------------------------------------------*/

        Button(
            onClick = {},
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 13.dp,
                    end = 66.dp
                )
                .size(52.dp),
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

        if (navController.currentBackStackEntry?.destination?.route == Routes.MAIN_SCREEN) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .imePadding()
                    .zIndex(-1f),
                contentPadding = PaddingValues(
                    top = 70.dp,
                    bottom = 80.dp,
                    start = 5.dp
                ),
                reverseLayout = true
            ) {
                items(
                    items = UserAndAiMessages.userAndAiMessages,
                    key = { it.id }
                ) { box ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when (box.role) {
                            IdentifyRole.USER.name -> {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(
                                            top = 15.dp,
                                            end = 5.dp,
                                            start = 100.dp
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

                            IdentifyRole.AI.name -> {
                                Box(
                                    modifier = Modifier
                                        .then(
                                            if (box.errorFlag) {
                                                Modifier
                                                    .align(Alignment.BottomCenter)
                                                    .padding(top = 5.dp, start = 20.dp, end = 20.dp)
                                                    .background(
                                                        color = Color.Transparent
                                                    )

                                            } else {
                                                Modifier
                                                    .align(Alignment.BottomStart)
                                                    .padding(top = 5.dp, start = 0.dp, end = 5.dp)
                                                    .background(
                                                        color = AIMessageBackgroundColor,
                                                        RoundedCornerShape(0.dp)
                                                    )
                                            }
                                        )
                                ) {
                                    SelectionContainer {
                                        AiMessageMarkdown(box)
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
        }
        BottomBox(
            Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .imePadding()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 10.dp
                ),
            navController,
            userChatMessages,
            aiAnswer,
            wideScreenFlag,
            drawerItemsOfChats,
            roomViewModel
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(110.dp)
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