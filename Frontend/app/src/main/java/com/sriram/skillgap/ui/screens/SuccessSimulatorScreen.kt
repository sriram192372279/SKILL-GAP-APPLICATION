package com.sriram.skillgap.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.ui.components.PremiumGlassCard
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.utils.AdvancedEngineData
import com.sriram.skillgap.utils.SuccessSimulationResult
import com.sriram.skillgap.utils.RecommendedProject
import com.sriram.skillgap.viewmodel.SkillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessSimulatorScreen(navController: NavController, viewModel: SkillViewModel) {
    val analysis by viewModel.analysisResult.collectAsState()

    var targetCompany by remember { mutableStateOf("Google") }
    var targetRole by remember { mutableStateOf("Java Developer") }
    var dailyStudyHours by remember { mutableStateOf(3f) }
    var currentSkillLevel by remember { mutableStateOf("Intermediate") } // "Beginner", "Intermediate", "Advanced"

    var simulationResult by remember { mutableStateOf<SuccessSimulationResult?>(null) }
    var isSimulating by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⏳ Career Success Simulator", fontWeight = FontWeight.Bold, color = Color.White) },
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
                .background(Brush.verticalGradient(listOf(DeepDarkBlue, Color(0xFF100F35))))
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Configure Career Target Parameters",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Map study allocations and capability baselines to calculate structural readiness.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            // Target inputs card
            item {
                PremiumGlassCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Company select dropdown (simplified horizontal chips)
                        Text("Target Company", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Google", "Amazon", "Microsoft", "TCS").forEach { comp ->
                                val isSel = comp == targetCompany
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) NeonPurple else SurfaceDark)
                                        .clickable { targetCompany = comp }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(comp, color = if (isSel) DeepDarkBlue else Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Role select dropdown
                        Text("Target Specialization", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Java Developer", "Data Analyst", "AI Engineer").forEach { role ->
                                val isSel = role == targetRole
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) NeonBlue else SurfaceDark)
                                        .clickable { targetRole = role }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(role, color = if (isSel) DeepDarkBlue else Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Hours slider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Study Allocation Per Day", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("${dailyStudyHours.toInt()} Hours", color = NeonPurple, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Slider(
                            value = dailyStudyHours,
                            onValueChange = { dailyStudyHours = it },
                            valueRange = 1f..10f,
                            colors = SliderDefaults.colors(
                                thumbColor = NeonPurple,
                                activeTrackColor = NeonPurple
                            )
                        )

                        // Skill Level selection
                        Text("Current Skill baseline", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Beginner", "Intermediate", "Advanced").forEach { lvl ->
                                val isSel = lvl == currentSkillLevel
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) HotPink else SurfaceDark)
                                        .clickable { currentSkillLevel = lvl }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(lvl, color = if (isSel) DeepDarkBlue else Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Simulate trigger button
            item {
                Button(
                    onClick = {
                        isSimulating = true
                        simulationResult = AdvancedEngineData.runSuccessSimulation(
                            targetCompany = targetCompany,
                            targetRole = targetRole,
                            studyHoursPerDay = dailyStudyHours,
                            skillLevel = currentSkillLevel,
                            missingSkills = analysis.missingSkills
                        )
                        isSimulating = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                ) {
                    if (isSimulating) {
                        CircularProgressIndicator(color = DeepDarkBlue, modifier = Modifier.size(20.dp))
                    } else {
                        Text("Run Success Simulation", color = DeepDarkBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Results Display
            simulationResult?.let { res ->
                item {
                    Text("Simulation Projections", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                item {
                    var animationPlayed by remember { mutableStateOf(false) }
                    val animatedProbability by animateFloatAsState(
                        targetValue = if (animationPlayed) res.successProbability.toFloat() else 0f,
                        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                        label = "probAnim"
                    )
                    val animatedWeeks by animateFloatAsState(
                        targetValue = if (animationPlayed) res.timeToReadyWeeks.toFloat() else 0f,
                        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                        label = "weeksAnim"
                    )
                    LaunchedEffect(key1 = res) {
                        animationPlayed = false
                        kotlinx.coroutines.delay(10)
                        animationPlayed = true
                    }

                    PremiumGlassCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Success Probability", color = Color.Gray, fontSize = 11.sp)
                                Text("${animatedProbability.toInt()}%", color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Estimated Timeline", color = Color.Gray, fontSize = 11.sp)
                                Text("${animatedWeeks.toInt()} Weeks", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                            }
                        }
                    }
                }

                // Path Timeline list
                item {
                    Text("Required Learning Path", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                items(res.learningPath) { step ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceDark,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(0.05f))
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Timeline, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(step, color = Color.White.copy(0.85f), fontSize = 13.sp)
                        }
                    }
                }

                // Monthly Forecast Chart / Progress
                item {
                    Text("Readiness Forecast Progression", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                item {
                    PremiumGlassCard {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text("Incremental Monthly Growth", color = Color.Gray, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            res.timelineProgress.forEach { (month, valPercent) ->
                                TimelineProgressBar(month = month, valPercent = valPercent)
                            }
                        }
                    }
                }

                // Suggested projects
                item {
                    Text("Recommended Portfolio Projects", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                items(res.recommendedProjects) { proj ->
                    PremiumGlassCard {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(proj.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(NeonPurple.copy(0.15f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(proj.difficulty, color = NeonPurple, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Technologies: ${proj.technologies.joinToString(", ")} • Duration: ${proj.duration}", color = NeonBlue, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(proj.learningOutcome, color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun TimelineProgressBar(month: String, valPercent: Int) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) valPercent / 100f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "timelineBarProg"
    )
    LaunchedEffect(key1 = valPercent) {
        animationPlayed = true
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(month, color = Color.White, modifier = Modifier.width(65.dp), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.width(10.dp))
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = NeonBlue,
            trackColor = Color.White.copy(0.05f)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text("$valPercent%", color = NeonBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
