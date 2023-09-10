package dev.thunderbolt.dogbreeds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.thunderbolt.dogbreeds.presentation.breed.detail.BreedDetailScreen
import dev.thunderbolt.dogbreeds.presentation.breed.favorites.BreedFavoritesScreen
import dev.thunderbolt.dogbreeds.presentation.breed.list.BreedListScreen
import dev.thunderbolt.dogbreeds.presentation.theme.DogBreedsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogBreedsTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "breed-list",
                ) {
                    composable(route = "breed-list") {
                        BreedListScreen(
                            navigateToDetail = { breed ->
                                navController.navigate("breed/$breed")
                            },
                            navigateToFavorites = {
                                navController.navigate("breed-favorites")
                            },
                        )
                    }
                    composable(
                        route = "breed/{breed}",
                        arguments = listOf(navArgument("breed") { type = NavType.StringType }),
                    ) {
                        BreedDetailScreen(
                            navigateBack = { navController.navigateUp() },
                        )
                    }
                    composable(route = "breed-favorites") {
                        BreedFavoritesScreen(
                            navigateBack = { navController.navigateUp() },
                        )
                    }
                }
            }
        }
    }
}
