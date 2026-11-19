
package com.example.project_mobile_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project_mobile_app.ui.theme.ProjectmobileappTheme
import ui.screens.CommunityTripsScreen
import ui.screens.CommunityDetailsScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ProjectmobileappTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "communityTrips"
                ) {

                    composable("communityTrips") {
                        CommunityTripsScreen(
                            onCommunityClick = { communityId ->
                                navController.navigate(
                                    "communityDetails/$communityId"
                                )
                            }
                        )
                    }

                    composable(
                        route = "communityDetails/{communityId}",
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
        }
    }
}
