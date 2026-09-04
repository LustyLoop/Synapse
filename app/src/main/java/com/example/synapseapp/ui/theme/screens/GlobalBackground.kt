package com.example.synapseapp.ui.theme.screens

import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
import kotlin.math.sin
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Paint as AndroidPaint
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import android.graphics.BlurMaskFilter
import androidx.core.graphics.createBitmap

object ShaderFlags: ViewModel(){
    var time by mutableFloatStateOf(0f)
    var transitionFlag by mutableStateOf(false)
    var inDarkTheme by mutableFloatStateOf(0f)

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
        animationSpec = tween(durationMillis = 1800),
        label = "transitionProgressAnimation"
    )
    val rand = remember { Random.nextFloat() }
    val shaderBackground = remember { ShaderBackground }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .then(
                        Modifier.drawWithCache {
                            shaderBackground?.setFloatUniform(
                                "resolution",
                                size.width,
                                size.height
                            )
                            shaderBackground?.setFloatUniform("rand", rand)
                            shaderBackground?.setFloatUniform(
                                "transitionProgress",
                                transitionProgress
                            )
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
                )
        )
    } else{
        WaveBackgroundForOldDevice()
    }
}


@Composable
fun WaveBackgroundForOldDevice(
    modifier: Modifier = Modifier,
    speedMultiplier: Float = 1f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "blue_wave")
    val baseDuration = 60000
    val adjustedDuration = (baseDuration / speedMultiplier).toInt()

    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = adjustedDuration, easing = LinearEasing)
        ),
        label = "time"
    )

    val seeds = remember { FloatArray(2) { (Math.random() * 100).toFloat() } }

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val width = size.width
        val height = size.height

        if (width <= 0f || height <= 0f) return@Canvas


        val scale = 0.15f
        val bitmapWidth = (width * scale).toInt().coerceAtLeast(1)
        val bitmapHeight = (height * scale).toInt().coerceAtLeast(1)

        val softwareBitmap = createBitmap(bitmapWidth, bitmapHeight)
        val androidCanvas = AndroidCanvas(softwareBitmap)

        val t = time * 0.45f
        val baseY = bitmapHeight * 0.5f
        val step = 6f

        val wavePath = Path()
        var x = 0f
        var isFirst = true

        while (x <= bitmapWidth + step) {
            val progress = x / bitmapWidth
            val noise = (sin(progress * 3.0f + t + seeds[0]) * 0.5f +
                    sin(progress * 1.5f - t * 0.7f + seeds[1]) * 0.3f +
                    sin(progress * 5.0f + t * 1.4f) * 0.2f)

            val amplitude = bitmapHeight * 0.2f
            val y = baseY + (noise * amplitude)

            if (isFirst) {
                wavePath.moveTo(x, y)
                isFirst = false
            } else {
                wavePath.lineTo(x, y)
            }
            x += step
        }
        wavePath.lineTo(bitmapWidth.toFloat(), bitmapHeight.toFloat())
        wavePath.lineTo(0f, bitmapHeight.toFloat())
        wavePath.close()

        val paint = AndroidPaint().apply {
            isAntiAlias = true
            isDither = true

            shader = android.graphics.LinearGradient(
                0f, baseY - bitmapHeight * 0.2f,
                0f, bitmapHeight.toFloat(),
                intArrayOf(
                    Color(0xFF3B82F6).toArgb(),
                    Color(0xFF1D4ED8).toArgb(),
                    Color(0xFF1E3A8A).toArgb()
                ),
                null, android.graphics.Shader.TileMode.CLAMP
            )

            maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.NORMAL)
        }
        androidCanvas.drawPath(wavePath.asAndroidPath(), paint)

        // Рисуем на основном Canvas с аппаратным сглаживанием
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawBitmap(
                softwareBitmap,
                null,
                android.graphics.RectF(0f, 0f, width, height),
                AndroidPaint().apply {
                    isFilterBitmap = true
                    isAntiAlias = true
                }
            )
        }

        softwareBitmap.recycle()
    }
}

