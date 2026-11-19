
package com.example.project_mobile_app.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import ui.screens.LoginScreen
import ui.screens.OnboardingScreen
import ui.screens.SignUpScreen
import ui.screens.SplashScreen

object LocalLensRoutes {
    const val Splash = "splash"
    const val Onboarding = "onboarding"
    const val Login = "login"
    const val SignUp = "signup"
}

@Composable
fun LocalLensNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    fun showPendingMessage() {
        Toast.makeText(
            context,
            "Firebase Authentication will be connected in the next phase",
            Toast.LENGTH_LONG
        ).show()
    }

    NavHost(
        navController = navController,
        startDestination = LocalLensRoutes.Splash
    ) {

        // 01 - Splash
        composable(LocalLensRoutes.Splash) {
            LaunchedEffect(Unit) {
                delay(2000)

                navController.navigate(LocalLensRoutes.Onboarding) {
                    popUpTo(LocalLensRoutes.Splash) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }

            SplashScreen()
        }

        // 02 - Onboarding / Welcome
        composable(LocalLensRoutes.Onboarding) {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate(LocalLensRoutes.Login) {
                        popUpTo(LocalLensRoutes.Onboarding) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onSkip = {
                    navController.navigate(LocalLensRoutes.Login) {
                        popUpTo(LocalLensRoutes.Onboarding) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 03 - Login
        composable(LocalLensRoutes.Login) {
            LoginScreen(
                onLogin = {
                    showPendingMessage()
                },
                onSignUp = {
                    navController.navigate(LocalLensRoutes.SignUp) {
                        launchSingleTop = true
                    }
                },
                onBack = {
                    navController.popBackStack()
                },
                onForgotPassword = {
                    Toast.makeText(
                        context,
                        "Password recovery will be available later",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }

        // 04 - Sign Up
        composable(LocalLensRoutes.SignUp) {
            SignUpScreen(
                onSignUp = {
                    showPendingMessage()
                },
                onLogin = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
