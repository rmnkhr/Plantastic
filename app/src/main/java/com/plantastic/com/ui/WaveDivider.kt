package com.plantastic.com.ui


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WaveDivider(
    modifier: Modifier = Modifier,
    bottomColor: Color,
    waveHeight: Dp = 40.dp,
    waveLength: Dp = 200.dp
) {
    val waveHeightPx = with(LocalDensity.current) { waveHeight.toPx() }
    val waveLengthPx = with(LocalDensity.current) { waveLength.toPx() }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(0f, height - waveHeightPx)

                var x = 0f
                while (x < width + waveLengthPx) {
                    quadraticTo(
                        x + waveLengthPx / 4,
                        height - waveHeightPx * 2,
                        x + waveLengthPx / 2,
                        height - waveHeightPx
                    )
                    x += waveLengthPx / 2
                }

                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(path = path, color = bottomColor)
        }
    }
}