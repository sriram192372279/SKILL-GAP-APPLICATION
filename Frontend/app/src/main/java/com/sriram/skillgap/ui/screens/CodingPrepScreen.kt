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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sriram.skillgap.ui.components.PremiumGlassCard
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.utils.AdvancedEngineData
import com.sriram.skillgap.utils.CodingPrepQuestion
import com.sriram.skillgap.viewmodel.SkillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodingPrepScreen(navController: NavController, viewModel: SkillViewModel) {
    var activeCategory by remember { mutableStateOf("Java") } // "Java", "SQL", "DSA", "OOPS", "Aptitude"

    val activeQuestions = remember(activeCategory) {
        AdvancedEngineData.codingPrepQuestions.filter { 
            it.category.equals(activeCategory, ignoreCase = true) 
        }.ifEmpty { AdvancedEngineData.codingPrepQuestions }
    }

    var questionIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var hasSubmitted by remember { mutableStateOf(false) }

    // Stats tracking
    var correctAnswersCount by remember { mutableStateOf(4) }
    var totalQuestionsSolved by remember { mutableStateOf(6) }

    val currentQuestion = activeQuestions.getOrNull(questionIndex) ?: activeQuestions.first()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💻 Personalized Coding Prep", fontWeight = FontWeight.Bold, color = Color.White) },
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
                .background(Brush.verticalGradient(listOf(DeepDarkBlue, Color(0xFF130E36))))
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Select Prep Domain",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Category scroller
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val domains = listOf("Java", "SQL", "DSA", "OOPS", "Aptitude")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceDark)
                            .padding(8.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            item {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    domains.forEach { cat ->
                                        val isSel = cat == activeCategory
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSel) NeonPurple else GlassWhite)
                                                .clickable { 
                                                    activeCategory = cat
                                                    questionIndex = 0
                                                    selectedOption = null
                                                    hasSubmitted = false
                                                }
                                                .padding(horizontal = 14.dp, vertical = 6.dp)
                                        ) {
                                            Text(cat, color = if (isSel) DeepDarkBlue else Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Stats tracker panel
            item {
                PremiumGlassCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Solved Challenges", color = Color.Gray, fontSize = 11.sp)
                            Text("$totalQuestionsSolved Problems", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Accuracy Index", color = Color.Gray, fontSize = 11.sp)
                            val acc = if (totalQuestionsSolved > 0) (correctAnswersCount * 100) / totalQuestionsSolved else 0
                            Text("$acc%", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }

            // Question content card
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
                                    .background(NeonBlue.copy(0.1f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(currentQuestion.difficulty, color = NeonBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = currentQuestion.question,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // Options List
            items(currentQuestion.options.size) { index ->
                val optionText = currentQuestion.options[index]
                val isSelected = index == selectedOption
                val isCorrect = index == currentQuestion.correctAnswer

                val cardBgColor = when {
                    hasSubmitted && isCorrect -> Color.Green.copy(0.08f)
                    hasSubmitted && isSelected && !isCorrect -> Color.Red.copy(0.08f)
                    isSelected -> NeonPurple.copy(0.1f)
                    else -> SurfaceDark
                }

                val cardBorderColor = when {
                    hasSubmitted && isCorrect -> SuccessGreen
                    hasSubmitted && isSelected && !isCorrect -> HotPink
                    isSelected -> NeonPurple
                    else -> Color.White.copy(0.05f)
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !hasSubmitted) { selectedOption = index },
                    shape = RoundedCornerShape(12.dp),
                    color = cardBgColor,
                    border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(1.dp, if (isSelected) NeonPurple else Color.Gray, CircleShape)
                                .background(if (isSelected) NeonPurple else Color.Transparent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = DeepDarkBlue, modifier = Modifier.size(14.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(optionText, color = Color.White.copy(0.85f), fontSize = 13.sp)
                    }
                }
            }

            // Submit / Next button
            item {
                if (!hasSubmitted) {
                    Button(
                        onClick = {
                            if (selectedOption != null) {
                                hasSubmitted = true
                                totalQuestionsSolved++
                                if (selectedOption == currentQuestion.correctAnswer) {
                                    correctAnswersCount++
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                        enabled = selectedOption != null
                    ) {
                        Text("Validate Answer", color = DeepDarkBlue, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Explanation panel
                        PremiumGlassCard {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text("AI Explanation Summary", color = WarningYellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(currentQuestion.explanation, color = Color.White.copy(0.85f), fontSize = 13.sp)
                            }
                        }

                        // Next button
                        Button(
                            onClick = {
                                selectedOption = null
                                hasSubmitted = false
                                questionIndex = (questionIndex + 1) % activeQuestions.size
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonBlue)
                        ) {
                            Text("Next Challenge", color = DeepDarkBlue, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
