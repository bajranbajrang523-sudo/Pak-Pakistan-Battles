package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ui.BattleViewModel
import com.example.ui.PlayerTab
import com.example.ui.Screen
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.screens.AboutUsScreen
import com.example.ui.screens.AnnouncementsScreen
import com.example.ui.screens.ForgotPasswordScreen
import com.example.ui.screens.GroupChatScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MatchHistoryScreen
import com.example.ui.screens.PlayerDashboardScreen
import com.example.ui.screens.PrivacyPolicyScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.RulesScreen
import com.example.ui.screens.SupportScreen
import com.example.ui.screens.TeamRegistrationScreen
import com.example.ui.screens.TermsConditionsScreen
import com.example.ui.theme.EsportsBackground
import com.example.ui.theme.ProGeniusBattleTheme
import kotlinx.coroutines.flow.collectLatest

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.ui.components.OfflineBanner
import com.example.ui.screens.SplashScreen
import com.example.util.NetworkMonitor

class MainActivity : ComponentActivity() {
    private val viewModel: BattleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProGeniusBattleTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val context = LocalContext.current
                var showSplash by remember { mutableStateOf(true) }

                val networkMonitor = remember { NetworkMonitor(context) }
                val isOnline by networkMonitor.isOnline.collectAsState(initial = true)

                LaunchedEffect(Unit) {
                    viewModel.snackBarEvent.collectLatest { message ->
                        snackbarHostState.showSnackbar(message)
                    }
                }

                if (showSplash) {
                    SplashScreen(onSplashFinished = { showSplash = false })
                } else {
                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        containerColor = EsportsBackground,
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(EsportsBackground)
                                .padding(innerPadding)
                        ) {
                            OfflineBanner(isOffline = !isOnline)
                            Box(modifier = Modifier.weight(1f)) {
                                AppNavigation(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: BattleViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    when (val screen = currentScreen) {
        is Screen.Login -> {
            LoginScreen(viewModel = viewModel, isLoading = isLoading)
        }
        is Screen.Register -> {
            RegisterScreen(viewModel = viewModel, isLoading = isLoading)
        }
        is Screen.ForgotPassword -> {
            ForgotPasswordScreen(viewModel = viewModel, isLoading = isLoading)
        }
        is Screen.PlayerDashboard -> {
            PlayerDashboardScreen(viewModel = viewModel, initialTab = screen.tab)
        }
        is Screen.Announcements -> {
            AnnouncementsScreen(viewModel = viewModel, onBack = { viewModel.navigateTo(Screen.PlayerDashboard(PlayerTab.HOME)) })
        }
        is Screen.GroupChat -> {
            GroupChatScreen(viewModel = viewModel, onBack = { viewModel.navigateTo(Screen.PlayerDashboard(PlayerTab.HOME)) })
        }
        is Screen.Profile -> {
            ProfileScreen(viewModel = viewModel, onBack = { viewModel.navigateTo(Screen.PlayerDashboard(PlayerTab.HOME)) })
        }
        is Screen.MatchHistory -> {
            MatchHistoryScreen(viewModel = viewModel, onBack = { viewModel.navigateTo(Screen.PlayerDashboard(PlayerTab.HOME)) })
        }
        is Screen.Rules -> {
            RulesScreen(viewModel = viewModel, onBack = { viewModel.navigateTo(Screen.PlayerDashboard(PlayerTab.HOME)) })
        }
        is Screen.Support -> {
            SupportScreen(viewModel = viewModel, onBack = { viewModel.navigateTo(Screen.PlayerDashboard(PlayerTab.HOME)) })
        }
        is Screen.AboutUs -> {
            AboutUsScreen(viewModel = viewModel, onBack = { viewModel.navigateTo(Screen.PlayerDashboard(PlayerTab.HOME)) })
        }
        is Screen.PrivacyPolicy -> {
            PrivacyPolicyScreen(viewModel = viewModel, onBack = { viewModel.navigateTo(Screen.PlayerDashboard(PlayerTab.HOME)) })
        }
        is Screen.TermsConditions -> {
            TermsConditionsScreen(viewModel = viewModel, onBack = { viewModel.navigateTo(Screen.PlayerDashboard(PlayerTab.HOME)) })
        }
        is Screen.TeamRegistration -> {
            TeamRegistrationScreen(viewModel = viewModel, onBack = { viewModel.navigateTo(Screen.PlayerDashboard(PlayerTab.HOME)) })
        }
        is Screen.AdminDashboard -> {
            AdminDashboardScreen(viewModel = viewModel, initialTab = screen.tab)
        }
    }
}

typealias SupportScreenRoute = Screen.Support
