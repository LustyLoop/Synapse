package com.example.synapseapp.ui.theme.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import data.AllShaders
import data.Shaders
import data.UserTextStateHolder


@Composable
fun SpeakScreen(
    navController: NavController
){

    val state =  UserTextStateHolder.objectOfUserTextState
    Shaders.currentShader = AllShaders.Ball
    SharedScreen(navController, showTextInputField = false)

    DisposableEffect(Unit) {
        onDispose {
            if (state.userMessages.isEmpty()) {
                Shaders.currentShader = AllShaders.Wave
                state.hideShaderFlag.value = false
            }
        }
    }


}