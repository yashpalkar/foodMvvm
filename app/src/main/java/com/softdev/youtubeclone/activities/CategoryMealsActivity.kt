package com.softdev.youtubeclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.softdev.youtubeclone.R
import com.softdev.youtubeclone.adapters.CategoriesAdapter
import com.softdev.youtubeclone.adapters.CategoryMealAdapter
import com.softdev.youtubeclone.databinding.ActivityCategoryMealsBinding
import com.softdev.youtubeclone.fragment.HomeFragment
import com.softdev.youtubeclone.viewModel.CategoryMealViewModel

class CategoryMealsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryMealViewModel: CategoryMealViewModel
    lateinit var categoriesAdapter: CategoryMealAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_meals)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecycleView()

        categoryMealViewModel = ViewModelProviders.of(this)[CategoryMealViewModel::class.java]
        categoryMealViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryMealViewModel.observerMealLiveData().observe(this, Observer { mealList ->
            binding.tvCategoryCount.text=mealList.size.toString()
          categoriesAdapter.setMealsList(mealList)
        })
    }

    private fun prepareRecycleView() {
      categoriesAdapter= CategoryMealAdapter()
        binding.rvMeals.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter=categoriesAdapter
        }
    }
}