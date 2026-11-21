

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
import ui.screens.ExploreScreen
import ui.screens.MapScreen
import ui.screens.ProfileScreen
import ui.screens.SavedPlacesScreen
import ui.screens.TravelJournalScreen
import ui.screens.TripDetailsScreen
import ui.screens.ExperienceDetailScreen
import ui.screens.BuildMyDayScreen
object LocalLensRoutes {
    const val Splash = "splash"
    const val Onboarding = "onboarding"
    const val Login = "login"
    const val SignUp = "signup"
    const val Home = "home"
    const val Explore = "explore"
    const val CommunityTrips = "communityTrips"
    const val CommunityDetails = "communityDetails/{communityId}"
    const val Map = "map"
    const val Profile = "profile"
    const val SavedPlaces = "savedPlaces"
    const val TravelJournal = "travelJournal"
    const val TripDetails = "tripDetails"
    const val ExperienceDetail = "experienceDetail/{placeId}"
    const val BuildMyDay = "buildMyDay"

    fun experienceDetail(placeId: String): String {
        return "experienceDetail/${Uri.encode(placeId)}"
    }


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
        composable(LocalLensRoutes.Home) {
            HomeScreen(
                onNavigateToMap = {
                    navController.navigate(LocalLensRoutes.Map) {
                        launchSingleTop = true
                    }
                },

                onNavigateToExplore = {
                    navController.navigate(LocalLensRoutes.Explore) {
                        launchSingleTop = true
                    }
                },

                onNavigateToCommunityTrips = {
                    navController.navigate(LocalLensRoutes.CommunityTrips) {
                        launchSingleTop = true
                    }
                },

                onNavigateToExperienceDetails = { placeId ->
                    navController.navigate(
                        LocalLensRoutes.experienceDetail(placeId)
                    )
                },

                onNavigateToProfile = {
                    navController.navigate(LocalLensRoutes.Profile) {
                        launchSingleTop = true
                    }
                },

                onNavigateToBuildMyDay = {
                    navController.navigate(LocalLensRoutes.BuildMyDay)
                },

                onNavigateToSaved = {
                    navController.navigate(LocalLensRoutes.SavedPlaces) {
                        launchSingleTop = true
                    }
                },

                onNavigateToHome = {
                    // Already on Home
                }
            )
        }
        // Build My Day
        composable(LocalLensRoutes.BuildMyDay) {
            BuildMyDayScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

// Explore Screen
        composable(LocalLensRoutes.Explore) {
            ExploreScreen(
                onNavigateToHome = {
                    navController.navigate(LocalLensRoutes.Home) {
                        popUpTo(LocalLensRoutes.Home) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onNavigateToMap = {
                    navController.navigate(LocalLensRoutes.Map) {
                        launchSingleTop = true
                    }
                },
                onNavigateToExplore = {
                    // Already on Explore
                },
                        onNavigateToExperienceDetails = { placeId ->
                    navController.navigate(
                        LocalLensRoutes.experienceDetail(placeId)
                    )
                },
                onNavigateToCommunity = {
                    navController.navigate(LocalLensRoutes.CommunityTrips) {
                        launchSingleTop = true
                    }
                },

            )
        }
        // Map Screen
        composable(LocalLensRoutes.Map) {
            MapScreen(
                onViewPlaceClick = { placeId ->
                    navController.navigate(
                        LocalLensRoutes.experienceDetail(placeId)
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(LocalLensRoutes.Home) {
                        popUpTo(LocalLensRoutes.Home) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },onNavigateToExplore = {
                    navController.navigate(LocalLensRoutes.Explore) {
                        popUpTo(LocalLensRoutes.Home) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                        onNavigateToCommunity = {
                    navController.navigate(LocalLensRoutes.CommunityTrips) {
                        launchSingleTop = true
                    }
                },
                                onNavigateToProfile = {
                            navController.navigate(LocalLensRoutes.Profile) {
                                launchSingleTop = true
                            }
                        }

            )
        }
        // Experience Detail Screen
        composable(
            route = LocalLensRoutes.ExperienceDetail,
            arguments = listOf(
                navArgument("placeId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val placeId = backStackEntry.arguments
                ?.getString("placeId")
                ?: ""

            ExperienceDetailScreen(
                placeId = placeId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        // Profile Screen
        composable(LocalLensRoutes.Profile) {
            ProfileScreen(
                onSavedPlacesClick = {
                    navController.navigate(LocalLensRoutes.SavedPlaces)
                },
                onMyTripsClick = {
                    navController.navigate(LocalLensRoutes.TripDetails)
                },
                onJournalClick = {
                    navController.navigate(LocalLensRoutes.TravelJournal)
                },
                onReviewsClick = {
                    Toast.makeText(
                        context,
                        "My reviews & photos will be available later",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onAccountClick = {
                    Toast.makeText(
                        context,
                        "Account details will be available later",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onSettingsClick = {
                    Toast.makeText(
                        context,
                        "Settings will be available later",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onEditInterestsClick = {
                    Toast.makeText(
                        context,
                        "Edit interests will be available later",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onManageExperiencesClick = {
                    Toast.makeText(
                        context,
                        "Manage experiences will be available later",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
        // Travel Journal Screen
        composable(LocalLensRoutes.TravelJournal) {
            TravelJournalScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onFilterClick = {
                    Toast.makeText(
                        context,
                        "Use the available journal filters",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onAddEntryClick = {
                    Toast.makeText(
                        context,
                        "Add journal entry will be available later",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onMemoryClick = { memoryId ->
                    Toast.makeText(
                        context,
                        "Memory details will be available later",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
        // Trip Details Screen
        composable(LocalLensRoutes.TripDetails) {
            TripDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onShareClick = {
                    Toast.makeText(
                        context,
                        "Share trip will be available later",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onMeetupLocationClick = {
                    navController.navigate(LocalLensRoutes.Map)
                },
                onItineraryClick = {
                    Toast.makeText(
                        context,
                        "Trip itinerary will be connected later",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onJoinTripClick = {
                    Toast.makeText(
                        context,
                        "Trip joined successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
        // Saved Places Screen
        composable(LocalLensRoutes.SavedPlaces) {
            SavedPlacesScreen(
                onNavigateToHome = {
                    navController.navigate(LocalLensRoutes.Home) {
                        launchSingleTop = true
                    }
                },
                onNavigateToExplore = {
                    navController.navigate(LocalLensRoutes.Explore) {
                        launchSingleTop = true
                    }
                },
                onNavigateToMap = {
                    navController.navigate(LocalLensRoutes.Map) {
                        launchSingleTop = true
                    }
                },
                onNavigateToSaved = {
                    // Already on Saved Places
                },
                onNavigateToProfile = {
                    navController.navigate(LocalLensRoutes.Profile) {
                        launchSingleTop = true
                    }
                },
                        onNavigateToExperienceDetails = { placeId ->
                    navController.navigate(
                        LocalLensRoutes.experienceDetail(placeId)
                    )
                },

            )
        }
        // 06 - Community Trips
        composable(LocalLensRoutes.CommunityTrips) {
            CommunityTripsScreen(
                onCommunityClick = { communityId ->
                    navController.navigate(
                        LocalLensRoutes.communityDetails(communityId)
                    )
                },

                onTripClick = { tripId ->
                    navController.navigate(LocalLensRoutes.TripDetails)
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
                },
                onTripClick = { tripId ->
                    navController.navigate(LocalLensRoutes.TripDetails)
                }
            )
        }
    }
}
