package com.sriram.skillgap.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.ui.components.GlassCard
import com.sriram.skillgap.ui.components.PremiumGlassCard
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.viewmodel.SkillViewModel
import kotlin.math.sin
import androidx.compose.animation.core.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapScreen(navController: NavController, viewModel: SkillViewModel) {
    val analysis by viewModel.analysisResult.collectAsState()
    val user by viewModel.user.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mastery Roadmap", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepDarkBlue)
            )
        },
        containerColor = DeepDarkBlue
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Role: ${user?.dreamJob ?: "Future Leader"}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonPurple
                        )
                        Text(
                            text = "Stage: ${if (analysis.roadmapPhases.isNotEmpty()) analysis.roadmapPhases.first().difficulty else "Core Prep"}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonBlue.copy(0.1f))
                            .border(1.dp, NeonBlue.copy(0.3f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "COMPATIBILITY: ${analysis.matchPercentage}%",
                            color = NeonBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Interactive Skill Dependency Graph Section
            item {
                Text(
                    text = "Skill Dependency Graph",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "High-fidelity local model visualizing learning dependencies. Follow arrows to clear gaps.",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            item {
                val graphSkills = analysis.missingSkills.take(4).ifEmpty {
                    listOf("Advanced Core Architecture", "High Availability Design", "Continuous Integration Pipeline", "Performance Analytics Monitoring")
                }
                SkillDependencyGraph(skills = graphSkills)
            }

            item {
                Text(
                    text = "Sprints Schedule & Phases",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            if (analysis.roadmapPhases.isEmpty()) {
                item { EmptyRoadmapView() }
            } else {
                analysis.roadmapPhases.forEach { phase ->
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                        ) {
                            Text(
                                text = phase.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                            if (phase.isCompleted) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.Green.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("✓ COMPLETED", color = Color(0xFF69F0AE), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(NeonPurple.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("ACTIVE PHASE", color = NeonPurple, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    items(phase.items) { item ->
                        RoadmapStepItem(item, phase.title)
                    }
                }
            }

            item {
                PremiumGlassCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null, tint = NeonBlue)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Adaptive Scaling Active", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                "Your roadmap phases automatically update and shift difficulty as you clear resume gaps.",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun SkillDependencyGraph(skills: List<String>) {
    val infiniteTransition = rememberInfiniteTransition(label = "dashFlow")
    val phaseOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phaseOffset"
    )

    PremiumGlassCard(modifier = Modifier.fillMaxWidth().height(260.dp)) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Draw dependency connector arrows behind the nodes
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                
                // Define coordinates for 4 nodes in a zig-zag progression
                val n1 = Offset(w * 0.2f, h * 0.22f)
                val n2 = Offset(w * 0.78f, h * 0.42f)
                val n3 = Offset(w * 0.22f, h * 0.65f)
                val n4 = Offset(w * 0.75f, h * 0.82f)

                val gradientBrush = Brush.linearGradient(
                    colors = listOf(NeonPurple, NeonBlue, HotPink),
                    start = Offset(0f, 0f),
                    end = Offset(w, h)
                )

                // 1 -> 2 connection
                drawBezierConnection(this, n1, n2, gradientBrush, phaseOffset)
                // 2 -> 3 connection
                if (skills.size > 2) drawBezierConnection(this, n2, n3, gradientBrush, phaseOffset)
                // 3 -> 4 connection
                if (skills.size > 3) drawBezierConnection(this, n3, n4, gradientBrush, phaseOffset)
            }

            // Overlay Node Capsules
            val coordinates = listOf(
                BiasAlignment(-0.6f, -0.6f),
                BiasAlignment(0.6f, -0.2f),
                BiasAlignment(-0.5f, 0.35f),
                BiasAlignment(0.5f, 0.75f)
            )

            skills.take(4).forEachIndexed { index, skillName ->
                val alignment = coordinates.getOrElse(index) { Alignment.Center }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = alignment
                ) {
                    DependencyNodeCapsule(name = skillName, step = index + 1)
                }
            }
        }
    }
}

private fun drawBezierConnection(
    drawScope: androidx.compose.ui.graphics.drawscope.DrawScope,
    start: Offset,
    end: Offset,
    brush: Brush,
    phaseOffset: Float
) {
    val controlX = (start.x + end.x) / 2
    val controlY = start.y + (end.y - start.y) * 0.15f
    
    val path = Path().apply {
        moveTo(start.x, start.y)
        quadraticBezierTo(controlX, controlY, end.x, end.y)
    }

    drawScope.drawPath(
        path = path,
        brush = brush,
        style = Stroke(
            width = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), phaseOffset)
        )
    )
}

@Composable
fun DependencyNodeCapsule(name: String, step: Int) {
    val accentColor = when(step) {
        1 -> NeonPurple
        2 -> NeonBlue
        3 -> HotPink
        else -> WarningYellow
    }

    var animationPlayed by remember { mutableStateOf(false) }
    val animatedScale by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            delayMillis = (step - 1) * 150, // staggered delay
            easing = FastOutSlowInEasing
        ),
        label = "nodeScale"
    )
    
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SurfaceDark.copy(alpha = 0.85f),
        border = borderStroke(1.dp, accentColor.copy(alpha = 0.6f)),
        shadowElevation = 4.dp,
        modifier = Modifier.graphicsLayer {
            scaleX = animatedScale
            scaleY = animatedScale
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$step",
                    color = DeepDarkBlue,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (name.length > 18) name.take(16) + ".." else name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun RoadmapPhaseHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun RoadmapStepItem(skill: String, phase: String) {
    val color = if (phase.contains("Foundation")) NeonPurple else if (phase.contains("Technical")) NeonBlue else HotPink
    
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 4.dp)) {
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
            Box(modifier = Modifier.width(1.dp).height(74.dp).background(color.copy(alpha = 0.25f)))
        }
        Spacer(modifier = Modifier.width(16.dp))
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = skill, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Hub, contentDescription = null, tint = color.copy(0.7f), modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(text = "Practice-based modular sprint item.", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun EmptyRoadmapView() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Flag, contentDescription = null, modifier = Modifier.size(64.dp), tint = NeonPurple.copy(alpha = 0.3f))
        Text("You're a Rockstar!", fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(top = 16.dp))
        Text("No skill gaps detected.", color = Color.Gray, fontSize = 12.sp)
    }
}

private fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = 
    androidx.compose.foundation.BorderStroke(width, color)
