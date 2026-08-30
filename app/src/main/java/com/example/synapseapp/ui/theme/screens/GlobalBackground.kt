package com.example.synapseapp.ui.theme.screens

import android.os.Build
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.lifecycle.ViewModel
import com.example.synapseapp.ui.theme.screens.ShaderFlags.inDarkTheme
import com.example.synapseapp.ui.theme.screens.ShaderFlags.transitionFlag
import data.UserTextStateHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import shaders.ShaderBackground
import kotlin.random.Random


object ShaderFlags: ViewModel(){
    var time by mutableFloatStateOf(0f)
    var transitionFlag by mutableStateOf(false)
    var inDarkTheme by mutableStateOf(0f)

}

@Composable
fun GlobalBackground(){
    inDarkTheme = if(isSystemInDarkTheme()) 1f else 0f
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            val startNanos = System.nanoTime()
            while (isActive) {
                val currentNanos = System.nanoTime()
                ShaderFlags.time = (currentNanos - startNanos) / 1_000_000_000f
                delay(10)
            }
        }
    }
    val state = UserTextStateHolder.objectOfUserTextState
    val hideAndShowProgress by animateFloatAsState(
        targetValue = if (state.hideShaderFlag.value) 1.0f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "HideAnimation"
    )
    LaunchedEffect(Unit) {
        state.hideShaderFlag.value = false
    }
    val transitionProgress by animateFloatAsState(
        targetValue = if (transitionFlag)  1.0f else 0f,
        animationSpec = tween(durationMillis = 2100),
        label = "transitionProgressAnimation"
    )
    val rand = remember { Random.nextFloat() }
    val shaderBackground = remember { ShaderBackground }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .then(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Modifier.drawWithCache {
                        shaderBackground?.setFloatUniform(
                            "resolution",
                            size.width,
                            size.height
                        )
                        shaderBackground?.setFloatUniform("rand", rand)
                        shaderBackground?.setFloatUniform("transitionProgress", transitionProgress)
                        shaderBackground?.setFloatUniform("inDarkTheme", inDarkTheme)
                            shaderBackground?.setFloatUniform(
                                "hideAndShowProgress",
                                hideAndShowProgress
                            )
                        onDrawBehind {
                            shaderBackground?.setFloatUniform("time", ShaderFlags.time)
                            drawRect(brush = ShaderBrush(shaderBackground as Shader))
                        }
                    }
                } else {
                    Modifier.background(MaterialTheme.colorScheme.onPrimary)
                }

            )

    )
}