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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.synapseapp.R
import com.example.synapseapp.ui.theme.AIMessageBackgroundColor
import com.example.synapseapp.ui.theme.AiMessageMarkdown
import data.AiAnswerClass
import data.IdentifyRole
import data.UserChatMessages
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    BottomBox: @Composable (
        modifier: Modifier,
        navController: NavController,
        userChatMessages: UserChatMessages,
        aiAnswer: AiAnswerClass
    ) -> Unit
) {
    val scope = rememberCoroutineScope()
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
                //gadget.batteryCharge += 1
                scope.launch {
                    drawerState?.open()
                }

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
            Image(
                modifier = Modifier
                    .size(27.dp),
                painter = painterResource(R.drawable.menu_ic),
                contentDescription = "Меню",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
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
                items = userChatMessages.userAndAiMessages,
                key = { it.id }
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
                                        top = 15.dp,
                                        end = 5.dp,
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
                                    .then(
                                        if (box.errorFlag) {
                                            Modifier
                                                .align(Alignment.BottomCenter)
                                                .padding(top = 5.dp, start = 5.dp, end = 5.dp)
                                                .background(
                                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                                    RoundedCornerShape(20.dp)
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
        BottomBox(
            Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .imePadding()
                .fillMaxWidth()
                .padding(
                    bottom = 10.dp,
                    start = 20.dp,
                    end = 20.dp
                ),
            navController,
            userChatMessages,
            aiAnswer
        )
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

