package com.sriram.skillgap.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.sriram.skillgap.ui.components.GlassCard
import com.sriram.skillgap.ui.theme.*
import com.sriram.skillgap.viewmodel.SkillViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: SkillViewModel) {
    val user by viewModel.user.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var hasClickedStart by remember { mutableStateOf(false) }
    var isCheckingAuth by remember { mutableStateOf(true) }

    // Check if user is already logged in on app startup
    LaunchedEffect(Unit) {
        val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            hasClickedStart = true
        } else {
            viewModel.logout()
        }
        isCheckingAuth = false
    }

    // Navigation listener
    LaunchedEffect(user) {
        if (user != null && hasClickedStart) {
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    // Screen State
    var isSignUp by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Input States
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Validation Errors
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Floating Custom Toast State
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                isLoading = true
                viewModel.signInWithGoogle(idToken) { success, error ->
                    isLoading = false
                    if (success) {
                        toastMessage = "Signed in with Google successfully!"
                        showToast = true
                        hasClickedStart = true
                        coroutineScope.launch {
                            delay(1500)
                            showToast = false
                        }
                    } else {
                        toastMessage = error ?: "Google authentication failed"
                        showToast = true
                        coroutineScope.launch {
                            delay(3000)
                            showToast = false
                        }
                    }
                }
            } else {
                isLoading = false
                toastMessage = "Google Sign-In failed: ID Token is null"
                showToast = true
                coroutineScope.launch {
                    delay(3000)
                    showToast = false
                }
            }
        } catch (e: ApiException) {
            isLoading = false
            toastMessage = "Google Sign-In failed: ${e.localizedMessage}"
            showToast = true
            coroutineScope.launch {
                delay(3000)
                showToast = false
            }
        }
    }

    // Email Syntax Helper
    fun isValidEmail(emailStr: String): Boolean {
        return emailStr.contains("@") && emailStr.substringAfter("@").contains(".")
    }

    // Dynamic animations for decorative elements
    val infiniteTransition = rememberInfiniteTransition(label = "auroraGlow")
    val pulseLogoScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoPulse"
    )

    val backgroundAnimOffset by infiniteTransition.animateFloat(
        initialValue = -50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bgMovement"
    )

    if (isCheckingAuth) {
        // Splendid Loading Splash Screen while checking login status
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(DeepDarkBlue, SurfaceDark))),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    color = NeonBlue,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Initializing SkillGap AI Engine...",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DeepDarkBlue, SurfaceDark)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // --- 1. PREMIUM BACKGROUND AURORA GLOWS ---
            Box(
                modifier = Modifier
                    .size(320.dp)
                    .align(Alignment.TopStart)
                    .offset(x = (-80 + backgroundAnimOffset / 2).dp, y = (-80 + backgroundAnimOffset / 3).dp)
                    .blur(110.dp)
                    .background(NeonPurple.copy(alpha = 0.15f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(340.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = (100 - backgroundAnimOffset / 2).dp, y = (100 - backgroundAnimOffset / 3).dp)
                    .blur(120.dp)
                    .background(NeonBlue.copy(alpha = 0.12f), CircleShape)
            )

            // --- 2. FLOATING PREMIUM NOTIFICATION TOAST ---
            AnimatedVisibility(
                visible = showToast,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 24.dp)
                    .zIndex(10f)
            ) {
                Surface(
                    color = SurfaceDark.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonPurple),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .shadow(12.dp, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (toastMessage.contains("Success", ignoreCase = true) || toastMessage.contains("created", ignoreCase = true) || toastMessage.contains("Signed in", ignoreCase = true)) 
                                Icons.Default.CheckCircle else Icons.Default.Info,
                            contentDescription = null,
                            tint = if (toastMessage.contains("Success", ignoreCase = true) || toastMessage.contains("created", ignoreCase = true) || toastMessage.contains("Signed in", ignoreCase = true)) 
                                SuccessGreen else WarningYellow,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = toastMessage,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // --- 3. MAIN CARD CONTAINER ---
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Interactive Logo Header
                Box(
                    modifier = Modifier
                        .size(90.dp * pulseLogoScale)
                        .shadow(16.dp, RoundedCornerShape(26.dp), ambientColor = NeonPurple, spotColor = NeonPurple)
                        .clip(RoundedCornerShape(26.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(NeonPurple.copy(alpha = 0.25f), NeonBlue.copy(alpha = 0.1f))
                            )
                        )
                        .border(1.5.dp, Brush.linearGradient(listOf(NeonPurple, NeonBlue)), RoundedCornerShape(26.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "AI",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.shadow(4.dp, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "SkillGap AI",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Professional Placement Intelligence Engine",
                    fontSize = 13.sp,
                    color = NeonBlue.copy(alpha = 0.9f),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp),
                    textAlign = TextAlign.Center
                )

                // Glassmorphic Card containing Interactive Form
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(24.dp, RoundedCornerShape(24.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Dynamic Sliding Selector for Sign In vs Create Account
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { 
                                        isSignUp = false
                                        nameError = null
                                        emailError = null
                                        passwordError = null
                                    }
                                    .background(if (!isSignUp) NeonPurple.copy(alpha = 0.2f) else Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Sign In", 
                                    color = if (!isSignUp) Color.White else Color.Gray, 
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { 
                                        isSignUp = true
                                        nameError = null
                                        emailError = null
                                        passwordError = null
                                    }
                                    .background(if (isSignUp) NeonPurple.copy(alpha = 0.2f) else Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Create Account", 
                                    color = if (isSignUp) Color.White else Color.Gray, 
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Text(
                            text = if (isSignUp) "Create Your Account" else "Welcome Back",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        )

                        // Full Name Input (Only shows up in Sign Up mode)
                        AnimatedVisibility(
                            visible = isSignUp,
                            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
                        ) {
                            PremiumTextField(
                                value = name,
                                onValueChange = {
                                    name = it
                                    nameError = if (it.isNotEmpty() && it.length < 2) "Name is too short" else null
                                },
                                label = "Full Name",
                                leadingIcon = Icons.Rounded.Person,
                                isError = nameError != null,
                                helperText = nameError
                            )
                        }

                        // Email Address Input
                        PremiumTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                emailError = if (it.isNotEmpty() && !isValidEmail(it)) "Invalid email address" else null
                            },
                            label = "Email Address",
                            leadingIcon = Icons.Rounded.Email,
                            isError = emailError != null,
                            helperText = emailError,
                            trailingIcon = {
                                if (email.isNotEmpty() && isValidEmail(email) && emailError == null) {
                                    Icon(Icons.Default.Check, contentDescription = "Valid", tint = SuccessGreen)
                                }
                            }
                        )

                        // Password Input
                        PremiumTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                passwordError = if (it.isNotEmpty() && it.length < 6) "Password must be at least 6 characters" else null
                            },
                            label = "Password",
                            leadingIcon = Icons.Rounded.Lock,
                            isPassword = true,
                            isError = passwordError != null,
                            helperText = passwordError,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint = NeonBlue.copy(alpha = 0.8f),
                                    modifier = Modifier
                                        .clickable { passwordVisible = !passwordVisible }
                                        .size(20.dp)
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // SUBMIT ACTION BUTTON
                        Button(
                            onClick = {
                                if (isLoading) return@Button

                                // Perform Field Validations
                                var hasErr = false
                                if (isSignUp && name.isBlank()) {
                                    nameError = "Full Name is required"
                                    hasErr = true
                                }
                                if (email.isBlank()) {
                                    emailError = "Email is required"
                                    hasErr = true
                                } else if (!isValidEmail(email)) {
                                    emailError = "Invalid email address"
                                    hasErr = true
                                }
                                if (password.isBlank()) {
                                    passwordError = "Password is required"
                                    hasErr = true
                                } else if (password.length < 6) {
                                    passwordError = "Password must be at least 6 characters"
                                    hasErr = true
                                }

                                if (!hasErr) {
                                    isLoading = true
                                    if (isSignUp) {
                                        viewModel.signUpWithFirebase(name, email, password) { success, error ->
                                            isLoading = false
                                            if (success) {
                                                toastMessage = "Account created successfully!"
                                                showToast = true
                                                hasClickedStart = true
                                                coroutineScope.launch {
                                                    delay(1500)
                                                    showToast = false
                                                }
                                            } else {
                                                toastMessage = error ?: "Registration failed"
                                                showToast = true
                                                coroutineScope.launch {
                                                    delay(3000)
                                                    showToast = false
                                                }
                                            }
                                        }
                                    } else {
                                        viewModel.signInWithFirebase(email, password) { success, error ->
                                            isLoading = false
                                            if (success) {
                                                toastMessage = "Signed in successfully!"
                                                showToast = true
                                                hasClickedStart = true
                                                coroutineScope.launch {
                                                    delay(1500)
                                                    showToast = false
                                                }
                                            } else {
                                                toastMessage = error ?: "Authentication failed"
                                                showToast = true
                                                coroutineScope.launch {
                                                    delay(3000)
                                                    showToast = false
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .shadow(8.dp, RoundedCornerShape(16.dp), ambientColor = NeonPurple, spotColor = NeonPurple),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(NeonPurple, GlowBlue)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = DeepDarkBlue,
                                        strokeWidth = 2.5.dp
                                    )
                                } else {
                                    Text(
                                        text = if (isSignUp) "Create Account" else "Sign In",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepDarkBlue,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // OR separator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color.White.copy(alpha = 0.1f)))
                            Text(
                                text = "OR",
                                color = Color.Gray,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Box(modifier = Modifier.weight(1f).height(1.dp).background(Color.White.copy(alpha = 0.1f)))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Google Sign-In Button
                        OutlinedButton(
                            onClick = {
                                if (isLoading) return@OutlinedButton
                                isLoading = true
                                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken("570682841937-crn1lmdfhek5obcjl47o03rqtn93ve4h.apps.googleusercontent.com")
                                    .requestEmail()
                                    .build()
                                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                                googleSignInClient.signOut().addOnCompleteListener {
                                    val signInIntent = googleSignInClient.signInIntent
                                    googleSignInLauncher.launch(signInIntent)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White.copy(alpha = 0.05f)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = "Google Icon",
                                    tint = NeonBlue,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Continue with Google",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // SOCIAL LOGINS SECTION
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(1.dp)
                                        .background(Color.White.copy(alpha = 0.15f))
                                )
                                Text(
                                    text = "QUICK START SESSIONS",
                                    color = Color.Gray,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    letterSpacing = 1.sp
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(1.dp)
                                        .background(Color.White.copy(alpha = 0.15f))
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SocialLoginButton(
                                    icon = Icons.Rounded.School,
                                    label = "Google EDU ID"
                                ) {
                                    hasClickedStart = true
                                    coroutineScope.launch {
                                        toastMessage = "Connecting Google Scholar..."
                                        showToast = true
                                        delay(500)
                                        viewModel.login("Sriram (Google Scholar)", "sriram.edu@gmail.com")
                                    }
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                SocialLoginButton(
                                    icon = Icons.Rounded.Terminal,
                                    label = "GitHub Developer SSO"
                                ) {
                                    hasClickedStart = true
                                    coroutineScope.launch {
                                        toastMessage = "Linking GitHub..."
                                        showToast = true
                                        delay(500)
                                        viewModel.login("Sriram (GitHub)", "sriram.dev@github.com")
                                    }
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                SocialLoginButton(
                                    icon = Icons.Rounded.Shield,
                                    label = "Corporate Auth Identity"
                                ) {
                                    hasClickedStart = true
                                    coroutineScope.launch {
                                        toastMessage = "Verifying Corporate ID..."
                                        showToast = true
                                        delay(500)
                                        viewModel.login("Sriram (Recruiter)", "sriram.recruiter@skillgap.ai")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- CUSTOM EXTENDED PREMIUM COMPOSABLES ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    isPassword: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    isError: Boolean = false,
    helperText: String? = null,
    focusedColor: Color = NeonPurple,
    unfocusedColor: Color = Color.Gray.copy(alpha = 0.3f),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = { 
                Icon(
                    imageVector = leadingIcon, 
                    contentDescription = null, 
                    tint = if (isError) ErrorRed else NeonBlue,
                    modifier = Modifier.size(20.dp)
                ) 
            },
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            isError = isError,
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) ErrorRed else focusedColor,
                unfocusedBorderColor = if (isError) ErrorRed.copy(alpha = 0.6f) else unfocusedColor,
                focusedLabelColor = if (isError) ErrorRed else focusedColor,
                unfocusedLabelColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = SurfaceDark.copy(alpha = 0.45f),
                unfocusedContainerColor = SurfaceDark.copy(alpha = 0.45f)
            )
        )
        if (isError && helperText != null) {
            Text(
                text = helperText,
                color = ErrorRed,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun SocialLoginButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.5.dp, Color.White.copy(alpha = 0.12f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = NeonBlue,
            modifier = Modifier.size(24.dp)
        )
    }
}

