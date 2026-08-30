package com.example.synapseapp.ui.theme.screens


import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import data.AllShaders
import data.Shaders
import com.example.synapseapp.ui.theme.fargments.SampleScreen
import com.example.synapseapp.ui.theme.fargments.TextFieldBottomBox


@Composable
fun MainScreen(navController: NavController) {
    Shaders.currentShader = AllShaders.Wave
    SampleScreen(
        navController = navController,
        BottomBox = ::TextFieldBottomBox
    )
}
