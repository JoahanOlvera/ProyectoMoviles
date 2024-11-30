package com.example.examen2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.examen2.screens.FavouritesScreen
import com.example.examen2.screens.HomeScreen
import com.example.examen2.screens.SearchScreen
import com.example.examen2.screens.SerieDetail

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ){
       composable("home"){
           HomeScreen(
               onSerieClick = { showId ->
                   navController.navigate("detail/$showId")
               },
               onSearchClick = { navController.navigate("search") },
               onFavouritesClick = { navController.navigate("favourites") }
           )
       }
        composable("detail/{showId}") { backStackEntry ->
            val showId = backStackEntry.arguments?.getString("showId")?.toIntOrNull()
            showId?.let {
                SerieDetail(
                    idSerie = showId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
        composable("search"){
            SearchScreen(
                onHomeClick = { navController.navigate("home") },
                onSerieClick = { showId ->
                    navController.navigate("detail/$showId")
                },
                onSearchClick = {},
                onFavouritesClick = { navController.navigate("favourites") }
            )
        }
        composable("favourites"){
            FavouritesScreen(
                onHomeClick = { navController.navigate("home") },
                onSerieClick = { showId ->
                    navController.navigate("detail/$showId")
                },
                onSearchClick = { navController.navigate("search") },
                onFavouritesClick = {}
            )
        }
    }
}