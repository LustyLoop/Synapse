package com.example.synapseapp.ui.theme.fargments

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.synapseapp.R
import compose.icons.TablerIcons
import compose.icons.tablericons.Photo
import compose.icons.tablericons.X
import kotlinx.coroutines.launch
import viewModel.GadgetInfo

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    localMainScreen: @Composable (NavController, GadgetInfo, DrawerState) -> Unit,
    navController: NavController,
    gadget: GadgetInfo
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        scrimColor = MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.background,
                drawerContentColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = 16.dp,
                            bottom = 16.dp
                        )
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        Text(
                            "Synapse",
                            modifier = Modifier
                                .padding(
                                    start = 12.dp,
                                    end = 12.dp,
                                    top = 8.dp,
                                    bottom = 12.dp
                                )
                                .weight(1f),
                            style = MaterialTheme.typography.titleLarge
                        )
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            modifier = Modifier
                                //.padding(top = 8.dp)
                                .size(48.dp)
                        )
                        {
                            Icon(
                                imageVector = TablerIcons.X,
                                contentDescription = "Закрыть меню",
                                tint = Color.White
                            )
                        }
                    }
                    //Spacer(Modifier.height(12.dp))
                    NavigationDrawerItem(
                        label = {
                            Text(
                                modifier = Modifier
                                    .padding(start = 4.dp),
                                text = "Settings"
                            )
                        },
                        selected = false,
                        icon = {
                            Icon(
                                modifier = Modifier
                                    .padding(start = 4.dp),
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = null
                            )
                        },
                        onClick = { /* Handle click */ }
                    )
                    NavigationDrawerItem(
                        label = { Text("Gallery") },
                        selected = false,
                        icon = {
                            Image(
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(top = 4.dp),
                                painter = painterResource(R.drawable.glasses_camera_ic),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                            )
                        },
                        onClick = { /* Handle click */ }
                    )
                    NavigationDrawerItem(
                        label = {
                            Text(
                                modifier = Modifier
                                    .padding(start = 4.dp),
                                text = "Camera"
                            )
                        },
                        selected = false,
                        icon = {
                            Icon(
                                modifier = Modifier
                                    .padding(start = 4.dp),
                                imageVector = TablerIcons.Photo, contentDescription = null
                            )
                        },
                        badge = { Text("20") },
                        onClick = { }
                    )
                    NavigationDrawerItem(
                        label = {
                            Text(
                                modifier = Modifier
                                    .padding(start = 4.dp),
                                text = "Help and feedback"
                            )
                        },
                        selected = false,
                        icon = {
                            Icon(
                                modifier = Modifier
                                    .padding(start = 4.dp),
                                imageVector = Icons.AutoMirrored.Outlined.Help,
                                contentDescription = null
                            )
                        },
                        onClick = { },
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        "Section 2",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    NavigationDrawerItem(
                        label = { Text("Item 1") },
                        selected = false,
                        onClick = { /* Handle click */ }
                    )
                    NavigationDrawerItem(
                        label = { Text("Item 2") },
                        selected = false,
                        onClick = { /* Handle click */ }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        },
        drawerState = drawerState
    ) {
        localMainScreen(
            navController,
            gadget,
            drawerState
        )
//        scope.launch {
//            if (drawerState.isClosed) {
//                drawerState.open()
//            } else {
//                drawerState.close()
//            }
//        }

    }
}