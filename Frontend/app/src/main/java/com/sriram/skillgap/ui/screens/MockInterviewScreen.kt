package com.sriram.skillgap.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.ui.components.PremiumGlassCard
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.utils.AdvancedEngineData
import com.sriram.skillgap.utils.MockInterviewQuestion
import com.sriram.skillgap.utils.MockInterviewEvaluation
import com.sriram.skillgap.viewmodel.SkillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockInterviewScreen(navController: NavController, viewModel: SkillViewModel) {
    var activeModule by remember { mutableStateOf("Technical") } // "Technical", "HR", "Coding", "Company-Specific"
    
    // Filter questions based on module selection
    val activeQuestions = remember(activeModule) {
        AdvancedEngineData.interviewQuestionsDatabase.filter { 
            it.type.equals(activeModule, ignoreCase = true) 
        }.ifEmpty { AdvancedEngineData.interviewQuestionsDatabase }
    }

    var questionIndex by remember { mutableStateOf(0) }
    var userAnswer by remember { mutableStateOf("") }
    
    var isEvaluating by remember { mutableStateOf(false) }
    var evaluationResult by remember { mutableStateOf<MockInterviewEvaluation?>(null) }

    val currentQuestion = activeQuestions.getOrNull(questionIndex) ?: activeQuestions.first()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎤 AI Mock Interview Simulator", fontWeight = FontWeight.Bold, color = Color.White) },
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
                .background(Brush.verticalGradient(listOf(DeepDarkBlue, Color(0xFF140F35))))
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Select Interview Practice Channel",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Horizontal Module Tabs
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val tabs = listOf("Technical", "HR", "Coding")
                    tabs.forEach { tab ->
                        val isSel = tab == activeModule
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) NeonPurple else SurfaceDark)
                                .clickable { 
                                    activeModule = tab
                                    questionIndex = 0
                                    userAnswer = ""
                                    evaluationResult = null
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                color = if (isSel) DeepDarkBlue else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Question Card
            item {
                PremiumGlassCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Question ${questionIndex + 1} of ${activeQuestions.size}", color = NeonPurple, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(HotPink.copy(0.1f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(currentQuestion.difficulty, color = HotPink, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = currentQuestion.question,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // User Answer Input Field
            if (evaluationResult == null) {
                item {
                    Text("Type Your Response", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                item {
                    OutlinedTextField(
                        value = userAnswer,
                        onValueChange = { userAnswer = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        placeholder = { Text("State your technical solution, framework configurations, or STAR story response here...", color = Color.Gray, fontSize = 13.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = Color.White.copy(0.1f),
                            focusedContainerColor = SurfaceDark,
                            unfocusedContainerColor = SurfaceDark
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }

                item {
                    Button(
                        onClick = {
                            if (userAnswer.isNotBlank()) {
                                isEvaluating = true
                                // Calculate scores mock-algorithmically based on keyword matches
                                val ansLower = userAnswer.lowercase()
                                val scoreBonus = if (ansLower.contains("lifecycle") || ansLower.contains("deadlock") || ansLower.contains("star") || ansLower.contains("acid")) 25 else 5
                                val acc = (60 + scoreBonus).coerceAtMost(98)
                                val conf = (65 + (userAnswer.length / 25)).coerceAtMost(95)
                                val overall = ((acc * 0.6) + (conf * 0.4)).toInt()
                                
                                evaluationResult = MockInterviewEvaluation(
                                    overallScore = overall,
                                    confidenceScore = conf,
                                    accuracyScore = acc,
                                    generatedFeedback = "Good structured outline. You accurately highlighted core abstract components. To perfect this response, ensure you elaborate further on memory allocations, clean logging protocols, and concrete project edge cases.",
                                    improvementAreas = listOf(
                                        "Elaborate on JVM memory allocation states (stack vs heap space).",
                                        "Structure conflict solutions explicitly using the STAR model (Situation, Task, Action, Result)."
                                    )
                                )
                                isEvaluating = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                    ) {
                        if (isEvaluating) {
                            CircularProgressIndicator(color = DeepDarkBlue, modifier = Modifier.size(20.dp))
                        } else {
                            Text("Submit & Evaluate Response", color = DeepDarkBlue, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Evaluation Results Display
            evaluationResult?.let { eval ->
                item {
                    Text("AI Evaluation Scorecard", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                item {
                    PremiumGlassCard {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Interview Performance rating", color = Color.Gray, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(110.dp)
                            ) {
                                CircularProgressIndicator(
                                    progress = eval.overallScore / 100f,
                                    modifier = Modifier.fillMaxSize(),
                                    strokeWidth = 8.dp,
                                    color = NeonBlue
                                )
                                Text("${eval.overallScore}%", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            }
                            
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Accuracy", color = Color.Gray, fontSize = 11.sp)
                                    Text("${eval.accuracyScore}/100", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Confidence", color = Color.Gray, fontSize = 11.sp)
                                    Text("${eval.confidenceScore}/100", color = NeonPurple, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item {
                    PremiumGlassCard {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text("Expert AI Feedback", color = WarningYellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(eval.generatedFeedback, color = Color.White.copy(0.85f), fontSize = 13.sp)
                        }
                    }
                }

                item {
                    Text("Recommended Improvement Focus", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                items(eval.improvementAreas) { area ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Yellow.copy(0.05f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarningYellow.copy(0.2f))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = WarningYellow, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(area, color = Color.White.copy(0.85f), fontSize = 12.sp)
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                userAnswer = ""
                                evaluationResult = null
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark)
                        ) {
                            Text("Retry", color = Color.White)
                        }
                        
                        Button(
                            onClick = {
                                userAnswer = ""
                                evaluationResult = null
                                questionIndex = (questionIndex + 1) % activeQuestions.size
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                        ) {
                            Text("Next Question", color = DeepDarkBlue, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
