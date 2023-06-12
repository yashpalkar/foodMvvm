package com.softdev.youtubeclone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softdev.youtubeclone.db.MealDatabase
import com.softdev.youtubeclone.pojo.*
import com.softdev.youtubeclone.retrofit.RetrofitIntance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
) : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLivedata = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData=MutableLiveData<Meal>()
    private val searchedMealsLiveData=MutableLiveData<List<Meal>>()
//    init{
//        getRandomMeal()
//    }
    var saveSateRandomMeal:Meal?=null
    fun getRandomMeal() {
        saveSateRandomMeal?.let {
            randommeal->
            randomMealLiveData.postValue(randommeal)
            return
        }
        RetrofitIntance.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randamMeal: Meal = response.body()!!.meals.get(0)
                    randomMealLiveData.value = randamMeal
                    saveSateRandomMeal=randamMeal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("TEST", t.message.toString())
            }
        })
    }

    fun observeRandomMealLivedata(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun getPopularItems() {
        RetrofitIntance.api.getPopularitem("Seafood")
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        popularItemLiveData.value = response.body()!!.meals
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d("TEST", t.message.toString())
                }
            })
    }

    fun observePopluarItemLiveData(): LiveData<List<MealsByCategory>> {
        return popularItemLiveData
    }

    fun getCategories() {
        RetrofitIntance.api.getCategoriesitem().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null) {
                    categoriesLivedata.postValue(response.body()!!.categories)
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("TEST", t.message.toString())
            }
        })
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLivedata
    }

    fun observeFavoritesMealsLiveData(): LiveData<List<Meal>> {
        return favoritesMealsLiveData
    }
    fun getMealById(id:String){
        RetrofitIntance.api.getMealbyID(id).enqueue(object:Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal=response.body()?.meals?.first()
                meal?.let{meal->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
               Log.d("HomeViewModel",t.message.toString())
            }

        })
    }
    fun observeBottomSheetMeal():LiveData<Meal> = bottomSheetMealLiveData

    fun searchMeal(searchQuery:String)=RetrofitIntance.api.getMealBySearch(searchQuery).enqueue(
        object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
             var mealList=response.body()?.meals
                mealList?.let{
                    searchedMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel",t.message.toString())
            }

        }
    )

    fun observeSearchedMealsLiveData():LiveData<List<Meal>> = searchedMealsLiveData
}