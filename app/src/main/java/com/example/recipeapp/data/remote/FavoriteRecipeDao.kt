package com.example.recipeapp.data.remote

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: MealDetail)

    @Delete
    suspend fun delete(recipe: MealDetail)

    @Query("SELECT * FROM MealDetail")
    fun getAllRecipes() : Flow<List<MealDetail>>

    @Query("SELECT * FROM MealDetail WHERE idMeal = :id")
    fun getRecipeById(id: String) : Flow<MealDetail?>
}
