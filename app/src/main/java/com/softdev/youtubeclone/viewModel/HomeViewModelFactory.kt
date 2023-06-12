package com.softdev.youtubeclone.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softdev.youtubeclone.db.MealDatabase

class HomeViewModelFactory(private var mealDatabase: MealDatabase):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(mealDatabase) as T
    }
}