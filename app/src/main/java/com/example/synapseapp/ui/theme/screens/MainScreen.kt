package com.example.synapseapp.ui.theme.screens


import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.synapseapp.room.RoomViewModel
import com.example.synapseapp.ui.theme.fargments.SampleScreen
import com.example.synapseapp.ui.theme.fargments.TextFieldBottomBox
import data.DrawerItemsOfChats
import viewModel.GadgetInfo


@Composable
fun MainScreen(
    navController: NavController,
    gadget: GadgetInfo,
    drawerState: DrawerState,
    wideScreenFlag: Boolean,
    drawerItemsOfChats: DrawerItemsOfChats,
    roomViewModel: RoomViewModel
) {
    drawerItemsOfChats.setAllChats(roomViewModel)
    SampleScreen(
        navController = navController,
        BottomBox = ::TextFieldBottomBox,
        gadget = gadget,
        drawerState = drawerState,
        wideScreenFlag = wideScreenFlag,
        drawerItemsOfChats = drawerItemsOfChats,
        roomViewModel = roomViewModel
    )
}
