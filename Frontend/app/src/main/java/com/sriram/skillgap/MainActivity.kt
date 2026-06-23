package com.sriram.skillgap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sriram.skillgap.ui.screens.*
import com.sriram.skillgap.ui.theme.SkillGapAITheme
import com.sriram.skillgap.viewmodel.SkillViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkillGapAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: SkillViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login",
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(400))
        }
    ) {
        composable("login") { LoginScreen(navController, viewModel) }
        composable("dashboard") { DashboardScreen(navController, viewModel) }
        composable("analysis") { AnalysisScreen(navController, viewModel) }
        composable("roadmap") { RoadmapScreen(navController, viewModel) }
        composable("prep") { PlacementPrepScreen(navController, viewModel) }
        composable("chat") { ChatScreen(navController, viewModel) }
        composable("analytics") { AnalyticsScreen(navController, viewModel) }
        
        // Advanced Modules Route Integrations
        composable("placement_predictor") { PlacementPredictorScreen(navController, viewModel) }
        composable("career_twin") { CareerTwinScreen(navController, viewModel) }
        composable("mock_interview") { MockInterviewScreen(navController, viewModel) }
        composable("career_success_simulator") { SuccessSimulatorScreen(navController, viewModel) }
        composable("recruiter_view") { RecruiterViewScreen(navController, viewModel) }
        composable("coding_prep") { CodingPrepScreen(navController, viewModel) }
        composable("company_intelligence") { CompanyHubScreen(navController, viewModel) }
        composable("patent_innovation") { PatentInnovationScreen(navController, viewModel) }
        composable("resume_upload") { ResumeUploadScreen(navController, viewModel) }
        composable("resume_results") { ResumeResultsScreen(navController, viewModel) }
        composable("job_matcher") { JobMatcherScreen(navController, viewModel) }
        composable("profile") { ProfileScreen(navController, viewModel) }
    }
}
