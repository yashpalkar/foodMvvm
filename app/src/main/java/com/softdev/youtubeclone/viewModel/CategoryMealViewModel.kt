package com.softdev.youtubeclone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softdev.youtubeclone.pojo.CategoryList
import com.softdev.youtubeclone.pojo.MealsByCategory
import com.softdev.youtubeclone.pojo.MealsByCategoryList
import com.softdev.youtubeclone.retrofit.RetrofitIntance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CategoryMealViewModel:ViewModel() {
    private var mealLiveData=MutableLiveData<List<MealsByCategory>>()

    fun getMealsByCategory(cateogryName:String){
        RetrofitIntance.api.getMealsByCategory(cateogryName).enqueue(object :Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let { meallist->
                    mealLiveData.postValue(meallist.meals)
                }

            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("Category",t.message.toString())
            }

        })
    }
    fun observerMealLiveData():LiveData<List<MealsByCategory>>{
        return mealLiveData
    }
}