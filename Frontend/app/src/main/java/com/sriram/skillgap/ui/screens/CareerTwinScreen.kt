package com.sriram.skillgap.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.sriram.skillgap.utils.CareerTwinProjection
import com.sriram.skillgap.viewmodel.SkillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareerTwinScreen(navController: NavController, viewModel: SkillViewModel) {
    val user by viewModel.user.collectAsState()
    val analysis by viewModel.analysisResult.collectAsState()

    val twinProfile = remember(user, analysis) {
        val u = user ?: com.sriram.skillgap.data.model.User(id = "default_user", name = "Candidate", email = "candidate@skillgap.ai", dreamJob = "Software Engineer", technicalScore = 60, atsScore = 65, employabilityScore = 55)
        AdvancedEngineData.generateCareerTwin(u, analysis.matchingSkills, analysis.missingSkills)
    }

    var selectedTimelineIndex by remember { mutableStateOf(1) } // Default: 6 Months (index 1)
    val activeProjection = twinProfile.projections[selectedTimelineIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🧬 Career Twin Engine", fontWeight = FontWeight.Bold, color = Color.White) },
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
                .background(Brush.verticalGradient(listOf(DeepDarkBlue, Color(0xFF15113A))))
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Your Digital Twin Profile",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "A patented simulation mapping your skills growth and employability yield over a 2-year cycle.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            // Timeline slider
            item {
                PremiumGlassCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Simulate Timeline: ${activeProjection.timeline}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Slider(
                            value = selectedTimelineIndex.toFloat(),
                            onValueChange = { selectedTimelineIndex = it.toInt().coerceIn(0, 3) },
                            valueRange = 0f..3f,
                            steps = 2,
                            colors = SliderDefaults.colors(
                                thumbColor = NeonPurple,
                                activeTrackColor = NeonPurple,
                                inactiveTrackColor = Color.Gray.copy(0.3f)
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("3 Months", color = if (selectedTimelineIndex == 0) NeonPurple else Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("6 Months", color = if (selectedTimelineIndex == 1) NeonPurple else Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("1 Year", color = if (selectedTimelineIndex == 2) NeonPurple else Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("2 Years", color = if (selectedTimelineIndex == 3) NeonPurple else Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Projected Career Target State Card
            item {
                PremiumGlassCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(NeonBlue.copy(0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Psychology, contentDescription = null, tint = NeonBlue)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(activeProjection.position, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Projected Target Role State", color = NeonBlue, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.White.copy(0.08f))
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Salary Package potential", color = Color.Gray, fontSize = 11.sp)
                                Text(activeProjection.predictedSalary, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Employability score", color = Color.Gray, fontSize = 11.sp)
                                Text("${activeProjection.employabilityScore}%", color = NeonPurple, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }

            // Skill Profile (Current vs Future)
            item {
                Text(
                    text = "Projected Skill Expansion",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            item {
                PremiumGlassCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Expected Unlocked Stack", color = NeonPurple, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        activeProjection.skillGrowth.forEach { skill ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonBlue, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(skill, color = Color.White.copy(0.85f), fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            // Growth Forecast
            item {
                Text(
                    text = "Employability Growth Forecast",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            item {
                PremiumGlassCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = SuccessGreen)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = twinProfile.growthForecast,
                                color = Color.White.copy(0.85f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Career progress timeline
            item {
                Text(
                    text = "Structured Growth Timeline",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            items(twinProfile.progressTimeline) { step ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .size(8.dp)
                            .background(NeonPurple, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = step,
                        color = Color.White.copy(0.8f),
                        fontSize = 13.sp
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
