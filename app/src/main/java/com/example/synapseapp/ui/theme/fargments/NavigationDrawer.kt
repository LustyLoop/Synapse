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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.synapseapp.R
import dev.seyfarth.tablericons.TablerIcons
import dev.seyfarth.tablericons.outlined.LibraryPhoto
import dev.seyfarth.tablericons.outlined.MessageCirclePlus
import dev.seyfarth.tablericons.outlined.Settings
import dev.seyfarth.tablericons.outlined.X
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
        modifier = Modifier.padding(start = 0.dp),
        scrimColor = MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.background,
                drawerContentColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 5.dp,
                            end = 5.dp,
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
                                imageVector = TablerIcons.Outlined.X,
                                contentDescription = "Закрыть меню",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    ShowNavigationDrawerItem(
                        labelText = "Новый чат",
                        iconId = TablerIcons.Outlined.MessageCirclePlus,
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
                    menuItems.forEach { item ->
                        NavigationDrawerItem(
                            modifier = Modifier.height(45.dp),
                            label = { Text(
                                modifier = Modifier.padding(start = 4.dp),
                                text =
                                    if(item.title.length > 26)item.title.substring(0, 26).dropLast(1) + "…"
                                    else item.title,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            ) },
                            selected = false,
                            onClick = {}
                        )
                    }

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
            } else 0
            if (iconId != null) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 3.dp),
                    imageVector = iconId,
                    contentDescription = null
                )
            }
            else null
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
data class DrawerItemsOfChats(
    val title: String
)


val menuItems = listOf(
    DrawerItemsOfChats("Как можно вырасти на 6 смvvvv?"),
    DrawerItemsOfChats("Профиль"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки"),
    DrawerItemsOfChats("Настройки")
)