package com.example.synapseapp.ui.theme.screens


import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import data.AllShaders
import data.Shaders
import com.example.synapseapp.ui.theme.fargments.SampleScreen
import com.example.synapseapp.ui.theme.fargments.TextFieldBottomBox
import viewModel.GadgetInfo


@Composable
fun MainScreen(
    navController: NavController,
    gadget: GadgetInfo,
    drawerState: DrawerState
) {
    Shaders.currentShader = AllShaders.Wave
    SampleScreen(
        navController = navController,
        BottomBox = ::TextFieldBottomBox,
        gadget = gadget,
        drawerState = drawerState
    )
}
