package com.example.notes.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import kotlinx.coroutines.delay

private const val SHIMMER_DURATION_MS = 800
const val MIN_SHIMMER_DURATION_MS = 500L

@Composable
fun shimmerBrush(
    baseColor: Color = MaterialTheme.colorScheme.surfaceDim,
    highlightColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    durationMs: Int = SHIMMER_DURATION_MS,
): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMs,
                easing = LinearEasing,
            ),
        ),
        label = "shimmerTranslate",
    )

    return Brush.linearGradient(
        colors = listOf(
            baseColor,
            highlightColor,
            baseColor,
            baseColor,
        ),
        start = Offset.Zero,
        end = Offset(
            x = translateAnimation.value,
            y = translateAnimation.value,
        ),
    )
}

fun Modifier.shimmer(
    shape: Shape = RectangleShape,
): Modifier = composed {
    background(
        brush = shimmerBrush(),
        shape = shape,
    )
}

suspend inline fun <T> withMinLoadingDelay(
    minDurationMs: Long = MIN_SHIMMER_DURATION_MS,
    block: () -> T,
): T {
    val startTime = System.currentTimeMillis()
    val result = block()
    val elapsedTime = System.currentTimeMillis() - startTime
    if (elapsedTime < minDurationMs) delay(minDurationMs - elapsedTime)
    return result
}
