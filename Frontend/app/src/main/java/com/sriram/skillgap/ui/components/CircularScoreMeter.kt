package com.sriram.skillgap.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sriram.skillgap.ui.theme.NeonPurple

@Composable
fun CircularScoreMeter(
    score: Int,
    label: String,
    modifier: Modifier = Modifier,
    color: Color = NeonPurple,
    isLocked: Boolean = false
) {
    var animationPlayed by remember { mutableStateOf(false) }
    
    val animatedProgress by animateFloatAsState(
        targetValue = if (isLocked || !animationPlayed) 0f else score / 100f,
        animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
        label = "scoreProgress"
    )

    val animatedScoreText by animateFloatAsState(
        targetValue = if (isLocked || !animationPlayed) 0f else score.toFloat(),
        animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
        label = "scoreText"
    )

    LaunchedEffect(key1 = score, key2 = isLocked) {
        animationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(100.dp)
    ) {
        CircularProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 8.dp,
            color = if (isLocked) Color.White.copy(alpha = 0.15f) else color,
            trackColor = Color.White.copy(alpha = 0.05f)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (isLocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.Gray,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text(
                    text = "${animatedScoreText.toInt()}%",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}
