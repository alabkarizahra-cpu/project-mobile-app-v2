

package com.example.project_mobile_app.navigation

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import ui.screens.CommunityDetailsScreen
import ui.screens.CommunityTripsScreen
import ui.screens.HomeScreen
import ui.screens.LoginScreen
import ui.screens.OnboardingScreen
import ui.screens.SignUpScreen
import ui.screens.SplashScreen

object LocalLensRoutes {
    const val Splash = "splash"
    const val Onboarding = "onboarding"
    const val Login = "login"
    const val SignUp = "signup"
    const val Home = "home"
    const val CommunityTrips = "communityTrips"
    const val CommunityDetails = "communityDetails/{communityId}"

    fun communityDetails(communityId: String): String {
        return "communityDetails/${Uri.encode(communityId)}"
    }
}

@Composable
fun LocalLensNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    fun navigateToHome() {
        navController.navigate(LocalLensRoutes.Home) {
            popUpTo(LocalLensRoutes.Login) {
                inclusive = true
            }
            launchSingleTop = true
        }
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

        // 02 - Onboarding
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
                    navigateToHome()
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
                    navigateToHome()
                },
                onLogin = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // 05 - Home
        // 05 - Home
        composable(LocalLensRoutes.Home) {
            HomeScreen(
                onNavigateToCommunityTrips = {
                    navController.navigate(LocalLensRoutes.CommunityTrips) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 06 - Community Trips
        composable(LocalLensRoutes.CommunityTrips) {
            CommunityTripsScreen(
                onCommunityClick = { communityId ->
                    navController.navigate(
                        LocalLensRoutes.communityDetails(communityId)
                    )
                }
            )
        }

        // 07 - Community Details
        composable(
            route = LocalLensRoutes.CommunityDetails,
            arguments = listOf(
                navArgument("communityId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val communityId =
                backStackEntry.arguments?.getString("communityId")
                    ?: ""

            CommunityDetailsScreen(
                communityId = communityId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
