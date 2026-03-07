package com.example.recipeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipeapp.ui.SmartPantryViewModel
import com.example.recipeapp.ui.screens.DetailScreen
import com.example.recipeapp.ui.screens.HomeScreen

object AppDestinations {
    const val HOME = "home"
    const val DETAIL_ROUTE = "detail/{mealId}"
}

@Composable
fun AppNavGraph(viewModel: SmartPantryViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME
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
    }
}