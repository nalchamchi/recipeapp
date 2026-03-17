package com.example.recipeapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recipeapp.data.remote.MealSummary
import com.example.recipeapp.ui.SmartPantryViewModel

@Composable
fun HomeScreen(
    viewModel: SmartPantryViewModel,
    onMealClick: (String) -> Unit,
) {
    val pantryItems by viewModel.pantryItems.collectAsState()
    val mealResults by viewModel.mealResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var ingredientInput by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Text(
                    text = "SmartPantry",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = ingredientInput,
                    onValueChange = { ingredientInput = it },
                    label = { Text("Enter ingredient") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.addIngredient(ingredientInput)
                        ingredientInput = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Ingredient")
                }


                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "My Pantry",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (pantryItems.isEmpty()) {
                item {
                    Text("No ingredients yet.")
                    Spacer(modifier = Modifier.height(20.dp))
                }
            } else {
                items(pantryItems) { ingredient ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = ingredient)

                            Row {
                                Text(
                                    text = "Search",
                                    modifier = Modifier.clickable {
                                        viewModel.searchMealsByIngredient(ingredient)
                                    }
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    text = "Delete",
                                    modifier = Modifier.clickable {
                                        viewModel.removeIngredient(ingredient)
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            item {
                Text(
                    text = "Recipe Results",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            when {
                isLoading && mealResults.isEmpty() -> {
                    item {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    item {
                        Text(
                            text = "Error: $error",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                mealResults.isEmpty() -> {
                    item {
                        Text("Select an ingredient to search recipes.")
                    }
                }

                else -> {
                    items(mealResults) { meal ->
                        MealSummaryCard(
                            meal = meal,
                            onClick = { onMealClick(meal.idMeal) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MealSummaryCard(
    meal: MealSummary,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = meal.strMealThumb,
                contentDescription = meal.strMeal,
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = meal.strMeal,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Meal ID: ${meal.idMeal}")
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Tap to view details")
            }
        }
    }
}