package com.sriram.skillgap.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.sriram.skillgap.ui.theme.NeonBlue
import com.sriram.skillgap.ui.theme.NeonPurple
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadarChart(
    scores: List<Float>, // 0.0 to 1.0
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animationScale by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "radarChartAnim"
    )
    LaunchedEffect(key1 = scores) {
        animationPlayed = true
    }

    Box(modifier = modifier.size(240.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width / 2 * 0.8f
            val numPoints = scores.size
            val angleStep = (2 * Math.PI / numPoints).toFloat()

            // Draw background polygons
            for (i in 1..4) {
                val r = radius * (i / 4f)
                val path = Path()
                for (j in 0 until numPoints) {
                    val angle = j * angleStep - Math.PI.toFloat() / 2
                    val x = center.x + r * cos(angle)
                    val y = center.y + r * sin(angle)
                    if (j == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()
                drawPath(path, color = Color.White.copy(alpha = 0.1f), style = Stroke(width = 1.dp.toPx()))
            }

            // Draw axis lines
            for (j in 0 until numPoints) {
                val angle = j * angleStep - Math.PI.toFloat() / 2
                val x = center.x + radius * cos(angle)
                val y = center.y + radius * sin(angle)
                drawLine(
                    color = Color.White.copy(alpha = 0.1f),
                    start = center,
                    end = Offset(x, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            // Draw data path
            val dataPath = Path()
            for (j in 0 until numPoints) {
                val angle = j * angleStep - Math.PI.toFloat() / 2
                val r = radius * scores[j] * animationScale
                val x = center.x + r * cos(angle)
                val y = center.y + r * sin(angle)
                if (j == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
            }
            dataPath.close()
            drawPath(dataPath, color = NeonPurple.copy(alpha = 0.4f))
            drawPath(dataPath, color = NeonPurple, style = Stroke(width = 2.dp.toPx()))
        }
    }
}

@Composable
fun ThreeDBarChart(
    matchingCount: Int,
    missingCount: Int,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animationScale by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "barChartAnim"
    )
    LaunchedEffect(key1 = matchingCount, key2 = missingCount) {
        animationPlayed = true
    }

    Canvas(modifier = modifier) {
        val total = (matchingCount + missingCount).toFloat().coerceAtLeast(1f)
        val maxVal = maxOf(matchingCount, missingCount).toFloat().coerceAtLeast(5f)
        
        val width = size.width
        val height = size.height
        
        val baselineY = height * 0.8f
        val maxH = height * 0.55f
        
        // Bar configuration
        val barW = width * 0.22f
        val x1 = width * 0.2f // Matching Bar X
        val x2 = width * 0.58f // Missing Bar X
        
        // Isometric 3D depth offsets
        val dx = 16.dp.toPx()
        val dy = 10.dp.toPx()
        
        // Heights
        val h1 = maxH * (matchingCount.toFloat() / maxVal) * animationScale
        val h2 = maxH * (missingCount.toFloat() / maxVal) * animationScale
        
        // Draw 3D floor grid lines for isometric perspective
        drawLine(
            color = Color.White.copy(alpha = 0.15f),
            start = Offset(0f, baselineY),
            end = Offset(width, baselineY),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            color = Color.White.copy(alpha = 0.1f),
            start = Offset(0f, baselineY - dy),
            end = Offset(width, baselineY - dy),
            strokeWidth = 1.dp.toPx()
        )
        
        // --- 1. DRAW MATCHING BAR (3D Prism in Neon Blue) ---
        if (matchingCount > 0) {
            // Front Face Path
            val frontPath = Path().apply {
                moveTo(x1, baselineY)
                lineTo(x1, baselineY - h1)
                lineTo(x1 + barW, baselineY - h1)
                lineTo(x1 + barW, baselineY)
                close()
            }
            drawPath(
                path = frontPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF00E5FF), Color(0xFF0077C2))
                )
            )
            
            // Top Face Path
            val topPath = Path().apply {
                moveTo(x1, baselineY - h1)
                lineTo(x1 + dx, baselineY - h1 - dy)
                lineTo(x1 + barW + dx, baselineY - h1 - dy)
                lineTo(x1 + barW, baselineY - h1)
                close()
            }
            drawPath(
                path = topPath,
                color = Color(0xFF80F3FF)
            )
            
            // Right Side Face Path
            val rightPath = Path().apply {
                moveTo(x1 + barW, baselineY)
                lineTo(x1 + barW, baselineY - h1)
                lineTo(x1 + barW + dx, baselineY - h1 - dy)
                lineTo(x1 + barW + dx, baselineY - dy)
                close()
            }
            drawPath(
                path = rightPath,
                color = Color(0xFF004BA0)
            )
        }
        
        // --- 2. DRAW MISSING BAR (3D Prism in Hot Pink) ---
        if (missingCount > 0) {
            // Front Face Path
            val frontPath2 = Path().apply {
                moveTo(x2, baselineY)
                lineTo(x2, baselineY - h2)
                lineTo(x2 + barW, baselineY - h2)
                lineTo(x2 + barW, baselineY)
                close()
            }
            drawPath(
                path = frontPath2,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFF4081), Color(0xFFC2185B))
                )
            )
            
            // Top Face Path
            val topPath2 = Path().apply {
                moveTo(x2, baselineY - h2)
                lineTo(x2 + dx, baselineY - h2 - dy)
                lineTo(x2 + barW + dx, baselineY - h2 - dy)
                lineTo(x2 + barW, baselineY - h2)
                close()
            }
            drawPath(
                path = topPath2,
                color = Color(0xFFFF80AB)
            )
            
            // Right Side Face Path
            val rightPath2 = Path().apply {
                moveTo(x2 + barW, baselineY)
                lineTo(x2 + barW, baselineY - h2)
                lineTo(x2 + barW + dx, baselineY - h2 - dy)
                lineTo(x2 + barW + dx, baselineY - dy)
                close()
            }
            drawPath(
                path = rightPath2,
                color = Color(0xFF880E4F)
            )
        }
    }
}
