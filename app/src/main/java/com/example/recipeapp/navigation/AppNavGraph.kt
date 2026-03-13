package com.example.recipeapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipeapp.ui.SmartPantryViewModel
import com.example.recipeapp.ui.screens.DetailScreen
import com.example.recipeapp.ui.screens.FavoritesScreen
import com.example.recipeapp.ui.screens.HomeScreen

object AppDestinations {
    const val HOME = "home"
    const val DETAIL_ROUTE = "detail/{mealId}"
    const val FAVORITES = "favorites"

    val bottomTabBar = listOf(HOME, FAVORITES)
}

@Composable
fun AppNavGraph(viewModel: SmartPantryViewModel) {
    val navController = rememberNavController()
    val currentScreen = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentScreen in AppDestinations.bottomTabBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentScreen == AppDestinations.HOME,
                        onClick = { navController.navigate(AppDestinations.HOME) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppDestinations.FAVORITES,
                        onClick = { navController.navigate(AppDestinations.FAVORITES) },
                        icon = { Icon(Icons.Default.Star, contentDescription = "Favorites") }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppDestinations.HOME,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(AppDestinations.HOME) {
                HomeScreen(
                    viewModel = viewModel,
                    onMealClick = { mealId ->
                        navController.navigate("detail/$mealId")
                    }
                )
            }

            composable(
                route = AppDestinations.DETAIL_ROUTE,
                arguments = listOf(
                    navArgument("mealId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val mealId = backStackEntry.arguments?.getString("mealId").orEmpty()

                DetailScreen(
                    mealId = mealId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(AppDestinations.FAVORITES) {
                FavoritesScreen(
                    viewModel = viewModel,
                    onMealClick = { mealId ->
                        navController.navigate("detail/$mealId")
                    }
                )
            }
        }
    }
}