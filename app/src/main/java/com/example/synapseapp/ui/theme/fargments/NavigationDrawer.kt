package com.example.synapseapp.ui.theme.fargments

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.synapseapp.R
import com.example.synapseapp.room.RoomViewModel
import data.DrawerItemsOfChats
import data.UserAndAiMessages
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.DotsVertical
import dev.seyfarth.tablericons.outlined.Edit
import dev.seyfarth.tablericons.outlined.LibraryPhoto
import dev.seyfarth.tablericons.outlined.Settings
import dev.seyfarth.tablericons.outlined.X
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import viewModel.GadgetInfo


@Composable
fun SampleOfNavigationDrawer(
    scope: CoroutineScope,
    drawerState: DrawerState,
    drawerItemsOfChats: DrawerItemsOfChats,
    roomViewModel: RoomViewModel
) {
    LazyColumn(
        modifier = Modifier
            .padding(
                start = 5.dp,
                end = 5.dp,
                top = 16.dp,
                bottom = 16.dp
            )
        //.verticalScroll(rememberScrollState())
    ) {
        item {
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
                    .size(48.dp)
            )
            {
                Icon(
                    imageVector = TablerIcons.Outlined.X,
                    contentDescription = "Закрыть меню",
                    tint = MaterialTheme.colorScheme.onBackground
                )

            }
        }
        ShowNavigationDrawerItem(
            labelText = "Новый чат",
            iconId = TablerIcons.Outlined.Edit,
            textStartPaddingValue = 6.dp,
            onClick = {
                UserAndAiMessages.createNewChat()
                scope.launch {
                    drawerState.close()
                }
            }
        )
        ShowNavigationDrawerItem(
            labelText = "Выбор модели ИИ",
            iconId = Icons.Rounded.AutoAwesome,
            textStartPaddingValue = 6.dp
        )
        ShowNavigationDrawerItem(
            labelText = "Камера",
            imageId = R.drawable.glasses_camera_ic,
            textStartPaddingValue = 4.dp
        )
        ShowNavigationDrawerItem(
            labelText = "Галерея",
            iconId = TablerIcons.Outlined.LibraryPhoto,
            badgeText = "20",
            textStartPaddingValue = 6.dp
        )
        ShowNavigationDrawerItem(
            labelText = "Настройки",
            iconId = TablerIcons.Outlined.Settings,
            textStartPaddingValue = 6.dp
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "История чатов",
            modifier = Modifier.padding(start = 20.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(6.dp))
            }
            items(
                items = drawerItemsOfChats.allChats,
                key = { item -> item.id }
            ) { item ->
                NavigationDrawerItem(
                    modifier = Modifier.height(50.dp),
                    label = {
                        Text(
                            text = item.title,
                            modifier = Modifier.padding(start = 4.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    selected = false,
                    onClick = {
                        drawerItemsOfChats.showSelectedChat(item.id, roomViewModel)
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    badge = {
                        IconButton(onClick = {}) {
                            TriplePoint()
                        }
                    }
                )
            }
    }
}

@Composable
fun TriplePoint() {
    Icon(
        imageVector = TablerIcons.Outlined.DotsVertical,
        contentDescription = "",
        modifier = Modifier.size(18.dp),
        tint = MaterialTheme.colorScheme.onBackground
    )

}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    localMainScreen: @Composable (
        NavController,
        GadgetInfo,
        DrawerState,
        Boolean,
        DrawerItemsOfChats,
        RoomViewModel
    ) -> Unit,
    navController: NavController,
    gadget: GadgetInfo,
    wideScreenFlag: Boolean,
    drawerItemsOfChats: DrawerItemsOfChats = viewModel(),
    roomViewModel: RoomViewModel = viewModel(factory = RoomViewModel.factory)
) {

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()

    if (wideScreenFlag) {
        val drawerWidth by animateDpAsState(
            targetValue = if (drawerState.isOpen) 300.dp else 0.dp,
            label = "drawerWidth"
        )
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            if (drawerWidth > 0.dp) {
                DismissibleDrawerSheet(
                    drawerContainerColor = MaterialTheme.colorScheme.background,
                    drawerContentColor = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .width(drawerWidth)
                        .fillMaxHeight()
                ) {
                    SampleOfNavigationDrawer(
                        scope,
                        drawerState,
                        drawerItemsOfChats,
                        roomViewModel
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                localMainScreen(
                    navController,
                    gadget,
                    drawerState,
                    wideScreenFlag,
                    drawerItemsOfChats,
                    roomViewModel
                )
            }
        }
        BackHandler(enabled = drawerState.isOpen) {
            scope.launch {
                drawerState.close()
            }
        }

    } else {
        ModalNavigationDrawer(
            modifier = Modifier.padding(start = 0.dp),
            scrimColor = MaterialTheme.colorScheme.background.copy(
                alpha = 0.4f
            ),
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = MaterialTheme.colorScheme.background,
                    drawerContentColor = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.width(300.dp)
                ) {
                    SampleOfNavigationDrawer(
                        scope,
                        drawerState,
                        drawerItemsOfChats,
                        roomViewModel
                    )
                }
            },
            drawerState = drawerState
        ) {
            localMainScreen(
                navController,
                gadget,
                drawerState,
                wideScreenFlag,
                drawerItemsOfChats,
                roomViewModel
            )
        }

        BackHandler(enabled = drawerState.isOpen) {
            scope.launch {
                drawerState.close()
            }
        }
    }
}


@Composable
fun ShowNavigationDrawerItem(
    labelText: String,
    imageId: Int = 0,
    iconId: ImageVector? = null,
    badgeText: String? = null,
    textStartPaddingValue: Dp = 0.dp,
    onClick: () -> Unit = {}
) {
    NavigationDrawerItem(
        modifier = Modifier.padding(horizontal = 0.dp),
        label = {
            Text(
                text = labelText,
                modifier = Modifier.padding(start = textStartPaddingValue)
            )
        },
        selected = false,
        icon = {
            if (imageId != 0) {
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(top = 4.dp),
                    painter = painterResource(imageId),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }
            if (iconId != null) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 3.dp),
                    imageVector = iconId,
                    contentDescription = null
                )
            }
        },
        badge = { Text(badgeText ?: "") },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
            unselectedTextColor = MaterialTheme.colorScheme.onBackground,
            unselectedBadgeColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = onClick
    )
}
