package com.sriram.skillgap.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.sriram.skillgap.ui.components.BottomNavigationBar
import com.sriram.skillgap.ui.components.GlassCard
import com.sriram.skillgap.ui.components.PremiumGlassCard
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.utils.PrepData
import com.sriram.skillgap.utils.PrepQuestion
import com.sriram.skillgap.viewmodel.SkillViewModel

data class ChatMessage(val text: String, val isUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, viewModel: SkillViewModel) {
    val user by viewModel.user.collectAsState()
    val targetJob = user?.dreamJob
    
    var isChatMode by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = DeepDarkBlue,
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            text = if (isChatMode) "Career Assistant" else "AI Mock Interview", 
                            fontWeight = FontWeight.Bold, 
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        if (targetJob != null && !isChatMode) {
                            Text(
                                text = "Preparing for: $targetJob", 
                                color = NeonPurple, 
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepDarkBlue),
                actions = {
                    // Quick Reset / Toggle Info Help
                    IconButton(onClick = { isChatMode = !isChatMode }) {
                        Icon(
                            imageVector = if (isChatMode) Icons.Default.Psychology else Icons.Default.ChatBubble, 
                            contentDescription = "Toggle Mode", 
                            tint = NeonPurple
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DeepDarkBlue, Color(0xFF0D0F2E))
                    )
                )
        ) {
            // Top Mode Toggle Control
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceDark)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isChatMode) NeonPurple.copy(alpha = 0.2f) else Color.Transparent)
                        .border(
                            1.dp,
                            if (isChatMode) NeonPurple else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { isChatMode = true }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.ChatBubble, 
                            contentDescription = null, 
                            tint = if (isChatMode) Color.White else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "AI Consultant",
                            color = if (isChatMode) Color.White else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (!isChatMode) NeonBlue.copy(alpha = 0.2f) else Color.Transparent)
                        .border(
                            1.dp,
                            if (!isChatMode) NeonBlue else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { isChatMode = false }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Psychology, 
                            contentDescription = null, 
                            tint = if (!isChatMode) Color.White else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Mock Interview",
                            color = if (!isChatMode) Color.White else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Main Contents based on selection
            Box(modifier = Modifier.weight(1f)) {
                if (isChatMode) {
                    ChatInterface(viewModel)
                } else {
                    MockInterviewSimulator(targetJob = targetJob, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun ChatInterface(viewModel: SkillViewModel) {
    var inputText by remember { mutableStateOf("") }
    val messages = remember { 
        mutableStateListOf(
            ChatMessage("Hello! I am your AI Career Assistant. Ask me about coding projects, dynamic roadmaps, or technical placement tips.", false)
        ) 
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
        }

        Surface(
            color = SurfaceDark,
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .navigationBarsPadding()
                    .imePadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ask something about projects or roadmap...", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = NeonPurple,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true
                )
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            messages.add(ChatMessage(inputText, true))
                            val response = viewModel.getChatResponse(inputText)
                            messages.add(ChatMessage(response, false))
                            inputText = ""
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = NeonPurple)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = DeepDarkBlue)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            color = if (message.isUser) NeonPurple else SurfaceDark,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 0.dp,
                bottomEnd = if (message.isUser) 0.dp else 16.dp
            ),
            modifier = Modifier.widthIn(max = 290.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = if (message.isUser) DeepDarkBlue else Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun MockInterviewSimulator(targetJob: String?, viewModel: SkillViewModel) {
    if (targetJob.isNullOrBlank()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            GlassCard {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Error, 
                        contentDescription = null, 
                        tint = HotPink,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Career Target Unset",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        "Please go to your Career Target on the Home screen to select a career target first! The AI needs a target job role to generate technical placement mock questions.",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    } else {
        val roleDetails = remember(targetJob) { PrepData.getRoleDetails(targetJob) }
        val analysis by viewModel.analysisResult.collectAsState()
        val interviewQuestions = remember(roleDetails, analysis) {
            val missingLower = analysis.missingSkills.map { it.lowercase() }
            val targeted = roleDetails.interviewQuestions.filter { pq ->
                missingLower.any { skill ->
                    pq.question.contains(skill, ignoreCase = true) || 
                    pq.answer.contains(skill, ignoreCase = true)
                }
            }
            if (targeted.isNotEmpty()) {
                (targeted.shuffled() + roleDetails.interviewQuestions.filter { it !in targeted }.shuffled())
                    .take(5)
            } else {
                roleDetails.interviewQuestions.shuffled().take(5)
            }
        }

        var isInterviewActive by remember { mutableStateOf(false) }
        var currentQuestionIndex by remember { mutableStateOf(0) }
        var answerText by remember { mutableStateOf("") }
        
        var showEvaluation by remember { mutableStateOf(false) }
        var evalScore by remember { mutableStateOf(0) }
        var evalStrengths by remember { mutableStateOf(listOf<String>()) }
        var evalGaps by remember { mutableStateOf(listOf<String>()) }
        
        var completedRoundsScore by remember { mutableStateOf(mutableListOf<Int>()) }

        val scrollState = rememberScrollState()

        if (!isInterviewActive) {
            // Landing screen of interview simulator
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PremiumGlassCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(NeonBlue.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = null, tint = NeonBlue)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("Interactive Simulator", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Real-time local answer evaluation", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                        
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "This system selects 5 random technical and scenario questions from our recruiter-verified database for the $targetJob role. Provide your answers, and receive detailed scores, compliance checks, and gaps.",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                    }
                }

                GlassCard {
                    Text("Session Features", color = NeonPurple, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(Modifier.height(12.dp))
                    
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 4.dp)) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Realistic placement questions curated by MNC engineers.", color = Color.Gray, fontSize = 12.sp)
                    }
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 4.dp)) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Dynamic match score checking technical vocabulary.", color = Color.Gray, fontSize = 12.sp)
                    }
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 4.dp)) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonPurple, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Detailed suggestions listing target industry concepts.", color = Color.Gray, fontSize = 12.sp)
                    }
                }

                Button(
                    onClick = {
                        isInterviewActive = true
                        currentQuestionIndex = 0
                        answerText = ""
                        showEvaluation = false
                        completedRoundsScore.clear()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonBlue)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Launch Mock Interview Session", color = DeepDarkBlue, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = DeepDarkBlue)
                    }
                }
            }
        } else if (currentQuestionIndex < interviewQuestions.size) {
            // Interactive Question Workspace
            val activeQuestion = interviewQuestions[currentQuestionIndex]
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header progress row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "QUESTION ${currentQuestionIndex + 1} OF ${interviewQuestions.size}",
                        color = NeonBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    TextButton(onClick = { isInterviewActive = false }) {
                        Text("Quit Interview", color = HotPink, fontSize = 12.sp)
                    }
                }

                // Question Card
                PremiumGlassCard {
                    Text(
                        text = activeQuestion.question,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )
                }

                if (!showEvaluation) {
                    // Input Workspace
                    Text(
                        "Type your response to the question below:",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )

                    OutlinedTextField(
                        value = answerText,
                        onValueChange = { answerText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceDark),
                        placeholder = { Text("Write your detailed explanation here...", color = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        maxLines = 10
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (answerText.isNotBlank()) {
                                val result = evaluateAnswer(answerText, activeQuestion.answer)
                                evalScore = result.first
                                evalStrengths = result.second
                                evalGaps = result.third
                                completedRoundsScore.add(result.first)
                                showEvaluation = true
                            }
                        },
                        enabled = answerText.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                    ) {
                        Text("Analyze & Evaluate Answer", color = DeepDarkBlue, fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Evaluation Feedback Workspace
                    PremiumGlassCard {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("AI Evaluation Report", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Surface(
                                    color = if (evalScore >= 75) Color(0xFF4CAF50).copy(0.15f) else HotPink.copy(0.15f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp, 
                                        if (evalScore >= 75) Color(0xFF4CAF50) else HotPink
                                    )
                                ) {
                                    Text(
                                        text = "$evalScore/100", 
                                        color = if (evalScore >= 75) Color(0xFF4CAF50) else HotPink,
                                        fontWeight = FontWeight.Bold, 
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            
                            Spacer(Modifier.height(16.dp))
                            
                            Text("Strengths Detected", color = NeonBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            evalStrengths.forEach { str ->
                                Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 3.dp)) {
                                    Text("✓", color = NeonBlue, fontWeight = FontWeight.Bold, modifier = Modifier.width(16.dp))
                                    Text(str, color = Color.White.copy(0.85f), fontSize = 12.sp)
                                }
                            }

                            Spacer(Modifier.height(16.dp))

                            Text("Recommended Improvements", color = HotPink, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            if (evalGaps.isEmpty()) {
                                Text("No missing terminology detected! Exceptionally complete answer.", color = Color(0xFF4CAF50), fontSize = 12.sp)
                            } else {
                                evalGaps.forEach { gap ->
                                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 3.dp)) {
                                        Text("!", color = HotPink, fontWeight = FontWeight.Bold, modifier = Modifier.width(16.dp))
                                        Text(gap, color = Color.White.copy(0.85f), fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }

                    GlassCard {
                        Text("Recruiter Recommended Answer:", color = NeonPurple, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = activeQuestion.answer,
                            color = Color.Gray,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }

                    Button(
                        onClick = {
                            currentQuestionIndex++
                            answerText = ""
                            showEvaluation = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonBlue)
                    ) {
                        Text(
                            text = if (currentQuestionIndex + 1 < interviewQuestions.size) "Next Interview Question" else "Finish Session",
                            color = DeepDarkBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // End of Interview summary card
            val avgScore = completedRoundsScore.average().toInt()
            val scoreLevel = when {
                avgScore >= 80 -> "Gold Placement Ready"
                avgScore >= 60 -> "Silver Placement Level"
                else -> "Bronze Foundation Stage"
            }
            val levelColor = when {
                avgScore >= 80 -> Color(0xFFFFD700)
                avgScore >= 60 -> Color(0xFFC0C0C0)
                else -> Color(0xFFCD7F32)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))
                
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(levelColor.copy(0.1f), CircleShape)
                        .border(2.dp, levelColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.MilitaryTech, 
                        contentDescription = null, 
                        tint = levelColor,
                        modifier = Modifier.size(54.dp)
                    )
                }

                Text(
                    "Interview Practice Complete!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                PremiumGlassCard {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Session Scorecard", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            "$avgScore / 100", 
                            color = levelColor, 
                            fontWeight = FontWeight.Bold, 
                            fontSize = 32.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            scoreLevel, 
                            color = Color.White, 
                            fontWeight = FontWeight.Bold, 
                            fontSize = 16.sp
                        )
                        
                        Spacer(Modifier.height(16.dp))
                        Divider(color = Color.White.copy(0.1f))
                        Spacer(Modifier.height(16.dp))

                        Text(
                            "Based on your dynamic evaluation answers, you show an exceptionally strong vocabulary. Focus on adding structural project references in future sessions to reach the gold category.",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                Button(
                    onClick = { isInterviewActive = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                ) {
                    Text("Return to Simulator Hub", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Local dynamic answer evaluator
fun evaluateAnswer(userAnswer: String, correctAnswer: String): Triple<Int, List<String>, List<String>> {
    val userClean = userAnswer.lowercase()
    
    // Tokenize correct answer and filter significant terms for evaluation
    val ignoreList = setOf("about", "their", "there", "which", "would", "should", "could", "these", "those")
    val words = correctAnswer.lowercase()
        .split(Regex("[^a-zA-Z]"))
        .filter { it.length > 4 && it !in ignoreList }
        .distinct()
    
    // Check keyword inclusion
    val keyTerms = words.take(6)
    val matchedTerms = keyTerms.filter { userClean.contains(it) }
    val missingTerms = keyTerms.filter { !userClean.contains(it) }
    
    val baseScore = 40 + (matchedTerms.size * 10)
    val score = baseScore.coerceAtMost(100)
    
    val strengths = mutableListOf<String>()
    val improvements = mutableListOf<String>()
    
    if (userClean.length < 15) {
        strengths.add("Kept the response highly concise.")
        improvements.add("Answer is extremely brief. Standard technical placement answers should be at least 2 sentences.")
    } else {
        strengths.add("Provided a detailed written explanation showing confidence.")
    }
    
    matchedTerms.forEach { term ->
        strengths.add("Correctly included key industry terminology: '$term'.")
    }
    
    if (missingTerms.isNotEmpty()) {
        missingTerms.forEach { term ->
            improvements.add("Consider incorporating the key concept: '$term' to make your answer fully compliant.")
        }
    } else {
        strengths.add("Demonstrated compliance with all recruiter vocabulary points!")
    }
    
    return Triple(score, strengths.distinct(), improvements.distinct())
}
