package com.example.recipeapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recipeapp.data.remote.MealDetail
import com.example.recipeapp.ui.SmartPantryViewModel

@Composable
fun DetailScreen(
    mealId: String,
    viewModel: SmartPantryViewModel,
    onBack: () -> Unit
) {
    val selectedMealDetail by viewModel.selectedMealDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isFavorite by viewModel.isFavorite(mealId).collectAsState(initial = false)


    LaunchedEffect(mealId) {
        viewModel.fetchMealDetail(mealId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when {
            isLoading && selectedMealDetail == null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBack) {
                        Text("Back")
                    }
                }
            }

            selectedMealDetail != null -> {
                DetailContent(
                    meal = selectedMealDetail!!,
                    isFavorite,
                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
fun DetailContent(
    meal: MealDetail,
    isFavorite: Boolean,
    onToggleFavorite: (MealDetail) -> Unit,
    onBack: () -> Unit
) {
    val ingredients = buildIngredientList(meal)


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Home")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    AsyncImage(
                        model = meal.strMealThumb,
                        contentDescription = meal.strMeal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = meal.strMeal,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        IconButton(onClick = { onToggleFavorite(meal) }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                                contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites"

                            )
                        }
                    }



                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Category: ${meal.strCategory ?: "N/A"}")
                    Text(text = "Area: ${meal.strArea ?: "N/A"}")

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Instructions",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = meal.strInstructions ?: "No instructions available."
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ingredients",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    if (ingredients.isEmpty()) {
                        Text("No ingredient data available.")
                    } else {
                        ingredients.forEach { item ->
                            Text(text = "• $item")
                        }
                    }
                }
            }
        }
    }
}

fun buildIngredientList(meal: MealDetail): List<String> {
    val rawIngredients = listOf(
        meal.strIngredient1 to meal.strMeasure1,
        meal.strIngredient2 to meal.strMeasure2,
        meal.strIngredient3 to meal.strMeasure3,
        meal.strIngredient4 to meal.strMeasure4,
        meal.strIngredient5 to meal.strMeasure5,
        meal.strIngredient6 to meal.strMeasure6,
        meal.strIngredient7 to meal.strMeasure7,
        meal.strIngredient8 to meal.strMeasure8,
        meal.strIngredient9 to meal.strMeasure9,
        meal.strIngredient10 to meal.strMeasure10
    )

    return rawIngredients.mapNotNull { (ingredient, measure) ->
        val ingredientText = ingredient?.trim().orEmpty()
        val measureText = measure?.trim().orEmpty()

        if (ingredientText.isBlank()) {
            null
        } else if (measureText.isBlank()) {
            ingredientText
        } else {
            "$ingredientText - $measureText"
        }
    }
}