package com.example.recipeapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {

    @GET("filter.php")
    suspend fun filterByIngredient(
        @Query("i") ingredient: String
    ): MealFilterResponse

    @GET("lookup.php")
    suspend fun lookupMealById(
        @Query("i") mealId: String
    ): MealDetailResponse
}