package com.example.recipeapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.remote.MealDetail
import com.example.recipeapp.data.remote.MealSummary
import com.example.recipeapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SmartPantryViewModel : ViewModel() {

    private val _pantryItems = MutableStateFlow<List<String>>(emptyList())
    val pantryItems: StateFlow<List<String>> = _pantryItems

    private val _mealResults = MutableStateFlow<List<MealSummary>>(emptyList())
    val mealResults: StateFlow<List<MealSummary>> = _mealResults

    private val _selectedMealDetail = MutableStateFlow<MealDetail?>(null)
    val selectedMealDetail: StateFlow<MealDetail?> = _selectedMealDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun addIngredient(ingredient: String) {
        val trimmed = ingredient.trim()
        if (trimmed.isBlank()) return

        val current = _pantryItems.value.toMutableList()
        val exists = current.any { it.equals(trimmed, ignoreCase = true) }

        if (!exists) {
            current.add(trimmed)
            _pantryItems.value = current
        }
    }

    fun removeIngredient(ingredient: String) {
        _pantryItems.value = _pantryItems.value.filterNot {
            it.equals(ingredient, ignoreCase = true)
        }
    }

    fun searchMealsByIngredient(ingredient: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = RetrofitInstance.api.filterByIngredient(ingredient)
                _mealResults.value = response.meals ?: emptyList()
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _mealResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchMealDetail(mealId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = RetrofitInstance.api.lookupMealById(mealId)
                _selectedMealDetail.value = response.meals?.firstOrNull()
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSelectedMealDetail() {
        _selectedMealDetail.value = null
    }
}