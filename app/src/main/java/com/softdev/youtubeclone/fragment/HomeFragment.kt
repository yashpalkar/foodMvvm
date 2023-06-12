package com.softdev.youtubeclone.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.softdev.youtubeclone.R
import com.softdev.youtubeclone.activities.CategoryMealsActivity
import com.softdev.youtubeclone.activities.MainActivity
import com.softdev.youtubeclone.activities.MealActivity
import com.softdev.youtubeclone.adapters.CategoriesAdapter
import com.softdev.youtubeclone.adapters.MostPopularViewAdapter
import com.softdev.youtubeclone.databinding.FragmentHomeBinding
import com.softdev.youtubeclone.fragment.bottomsheet.MealBottomSheetFragment
import com.softdev.youtubeclone.pojo.Category
import com.softdev.youtubeclone.pojo.MealsByCategory
import com.softdev.youtubeclone.pojo.Meal
import com.softdev.youtubeclone.viewModel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var mostPopularViewAdapter: MostPopularViewAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val Meal_ID = "com.softdev.youtubeclone.fragment.idMeal"
        const val Meal_NAME = "com.softdev.youtubeclone.fragment.nameMeal"
        const val Meal_THUMB = "com.softdev.youtubeclone.fragment.thumbMeal"
        const val CATEGORY_NAME = "com.softdev.youtubeclone.fragment.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
//    homeMvvm=ViewModelProviders.of(this).get(HomeViewModel::class.java)
//        homeMvvm = ViewModelProviders.of(this)[HomeViewModel::class.java]


        mostPopularViewAdapter = MostPopularViewAdapter()
        categoriesAdapter = CategoriesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRandomMeal()
        observerRandomMeal()
        randomMealClick()

        preparePopularItemRecyclerView()
        prepareCategoryRecyclerView()
        viewModel.getPopularItems()
        observerPopularItemLiveData()
        onPopularItemClick()

        viewModel.getCategories()
        observerCategoriesLiveData()
        onCatergoryClick()
        onPopularItemLongClick()
//        Glide.with(this@HomeFragment).load("randamMeal".strMealThumb).into(binding.imgRandomMeal)
        onsearchIconClick()

    }

    private fun onsearchIconClick() {
     binding.imgSearch.setOnClickListener(object : View.OnClickListener {
         override fun onClick(view: View?) {
             findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
         } })
     }

//     Log.d("text","text")



    private fun onPopularItemLongClick() {
         mostPopularViewAdapter.onLongItemClick={
             meal->
             val mealBottomSheetFragment=MealBottomSheetFragment.newInstance(meal.idMeal)
             mealBottomSheetFragment.show(childFragmentManager,"Meal info")
         }
    }

    private fun onCatergoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoryRecyclerView() {
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observerCategoriesLiveData() {
        viewModel.observeCategoriesLiveData()
            .observe(viewLifecycleOwner, object : Observer<List<Category>> {
                override fun onChanged(categories: List<Category>) {
                    categoriesAdapter.setCategoryList(categories)
                }
            })
    }

    private fun onPopularItemClick() {
        mostPopularViewAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(Meal_ID, meal.idMeal)
            intent.putExtra(Meal_NAME, meal.strMeal)
            intent.putExtra(Meal_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mostPopularViewAdapter
        }
    }

    private fun observerPopularItemLiveData() {
        viewModel.observePopluarItemLiveData()
            .observe(viewLifecycleOwner, object : Observer<List<MealsByCategory>> {
                override fun onChanged(mealList: List<MealsByCategory>) {
                    mostPopularViewAdapter.setMeal(mealList as ArrayList<MealsByCategory>)
                }

            })
    }

    private fun randomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(context, MealActivity::class.java)
            intent.putExtra(Meal_ID, randomMeal.idMeal)
            intent.putExtra(Meal_NAME, randomMeal.strMeal)
            intent.putExtra(Meal_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLivedata().observe(viewLifecycleOwner, object : Observer<Meal> {
            override fun onChanged(value: Meal) {

                Glide.with(this@HomeFragment).load(value.strMealThumb).into(binding.imgRandomMeal)
                randomMeal = value
            }

        })
    }

}
