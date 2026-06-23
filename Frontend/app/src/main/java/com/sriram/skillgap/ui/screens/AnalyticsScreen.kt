package com.sriram.skillgap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.ui.components.BottomNavigationBar
import com.sriram.skillgap.ui.components.RadarChart
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.utils.ReportGenerator
import com.sriram.skillgap.viewmodel.SkillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController, viewModel: SkillViewModel) {
    val user by viewModel.user.collectAsState()
    val analysis by viewModel.analysisResult.collectAsState()
    val history by viewModel.resumeHistory.collectAsState()
    val context = LocalContext.current

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = DeepDarkBlue
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DeepDarkBlue, Color(0xFF0F1225))
                    )
                )
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Intelligence Dashboard",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonPurple
                )
            }

            item {
                CareerReadinessCard(
                    score = user?.employabilityScore ?: 0,
                    streak = user?.learningStreak ?: 0
                )
            }

            item {
                Text(text = "Market Competency Radar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    RadarChart(
                        scores = listOf(
                            (user?.technicalScore ?: 0) / 100f,
                            (user?.atsScore ?: 0) / 100f,
                            0.75f, // Communication
                            (user?.experienceScore ?: 0) / 100f,
                            0.82f   // Logic/Aptitude
                        ),
                        labels = listOf("Technical", "ATS", "Soft Skills", "Experience", "Aptitude")
                    )
                }
            }

            // --- Resume Evolution Tracking Timeline ---
            item {
                Text(text = "Resume Evolution Tracking", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = "Track your historical resume audit score progression over time.", color = Color.Gray, fontSize = 11.sp)
            }

            if (history.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = SurfaceDark,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, tint = Color.Gray.copy(alpha = 0.4f), modifier = Modifier.size(42.dp))
                            Spacer(Modifier.height(10.dp))
                            Text("No Audit History Yet", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Your uploaded resume sessions will appear here to track your progress.", color = Color.Gray, fontSize = 11.sp)
                        }
                    }
                }
            } else {
                item {
                    val improvement = if (history.size >= 2) {
                        history.last().compatibilityScore - history.first().compatibilityScore
                    } else 0

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (history.size >= 2 && improvement > 0) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Green.copy(0.1f))
                                    .border(1.dp, Color.Green.copy(0.25f), RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF69F0AE), modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "Overall Score Evolution: +$improvement% Compatibility Increase!",
                                        color = Color(0xFF69F0AE),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        history.forEachIndexed { idx, entry ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = SurfaceDark,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(NeonPurple.copy(0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "v${idx + 1}",
                                            color = NeonPurple,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }
                                    Spacer(Modifier.width(16.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        val sdf = java.text.SimpleDateFormat("MMM dd, yyyy - HH:mm", java.util.Locale.getDefault())
                                        val dateStr = sdf.format(java.util.Date(entry.timestamp))
                                        Text(entry.fileName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(dateStr, color = Color.Gray, fontSize = 10.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("${entry.compatibilityScore}% Match", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("ATS: ${entry.atsScore}/100", color = Color.Gray, fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(text = "Career Progression", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatBox(Modifier.weight(1f), "Current Level", "Lvl ${user?.level ?: 1}", Icons.Default.TrendingUp, NeonPurple)
                    StatBox(Modifier.weight(1f), "Exp Points", "${user?.expPoints ?: 0}", Icons.Default.Bolt, NeonBlue)
                }
            }

            item {
                val totalSkills = (analysis.matchingSkills.size + analysis.missingSkills.size).coerceAtLeast(1)
                ProgressSection(
                    title = "Target Role Readiness",
                    current = analysis.matchingSkills.size,
                    total = totalSkills
                )
            }

            item {
                BadgeSection(user?.level ?: 1)
            }

            item {
                Button(
                    onClick = { 
                        user?.let { u ->
                            ReportGenerator.generateCareerReport(context, u, analysis)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple)
                ) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = NeonPurple)
                    Spacer(Modifier.width(12.dp))
                    Text("Export Industry Readiness Report", color = NeonPurple, fontWeight = FontWeight.Bold)
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun BadgeSection(level: Int) {
    Column {
        Text(text = "Unlocked Achievements", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            val badges = listOf(
                "Explorer" to true,
                "Skill Hunter" to (level >= 2),
                "Job Ready" to (level >= 3),
                "Elite Profile" to (level >= 4)
            )
            items(badges) { (name, earned) ->
                BadgeItem(name, earned)
            }
        }
    }
}

@Composable
fun BadgeItem(name: String, earned: Boolean) {
    Surface(
        color = if (earned) NeonPurple.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.05f),
        shape = RoundedCornerShape(16.dp),
        border = if (earned) androidx.compose.foundation.BorderStroke(1.dp, NeonPurple) else null
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = if (earned) NeonPurple else Color.Gray,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (earned) Color.White else Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun CareerReadinessCard(score: Int, streak: Int) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) score / 100f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "readinessProgress"
    )
    LaunchedEffect(key1 = score) {
        animationPlayed = true
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(Color(0xFF6200EE), Color(0xFF03DAC5))))
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Readiness Index", color = Color.Black.copy(alpha = 0.6f), fontSize = 14.sp)
                        Text(text = "$score.0", color = Color.Black, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Surface(
                        color = Color.Black.copy(0.1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color.Black, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("$streak Day Streak", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                    color = Color.Black,
                    trackColor = Color.Black.copy(alpha = 0.1f)
                )
                
                Text(
                    text = if (score >= 80) "Placement Probability: HIGH" else "Keep bridging gaps to reach Elite status.",
                    color = Color.Black.copy(0.8f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

@Composable
fun StatBox(modifier: Modifier, label: String, value: String, icon: ImageVector, color: Color) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = SurfaceDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ProgressSection(title: String, current: Int, total: Int) {
    var animationPlayed by remember { mutableStateOf(false) }
    val target = current.toFloat() / total.coerceAtLeast(1)
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationPlayed) target else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "progressSectionAnim"
    )
    LaunchedEffect(key1 = current, key2 = total) {
        animationPlayed = true
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = SurfaceDark,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = title, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = "$current/$total", color = NeonPurple, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = NeonPurple,
                trackColor = Color.White.copy(alpha = 0.1f)
            )
        }
    }
}
