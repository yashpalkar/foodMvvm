package com.softdev.youtubeclone.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softdev.youtubeclone.db.MealDatabase
import com.softdev.youtubeclone.pojo.Meal
import com.softdev.youtubeclone.pojo.MealList
import com.softdev.youtubeclone.retrofit.RetrofitIntance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(val mealDatabase: MealDatabase ) :ViewModel(){

    private var mealDetailsLiveData=MutableLiveData<Meal>()

    fun getMealDetail(id:String){
        RetrofitIntance.api.getMealbyID(id).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body()!=null){
                    mealDetailsLiveData.value=response.body()!!.meals[0]
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
               Log.d("MealActivity",t.message.toString())
            }

        })
    }
    fun observerMealDetailsLiveData():LiveData<Meal>{
        return mealDetailsLiveData
    }

    fun insertMeal(meal:Meal){
        viewModelScope.launch {
         mealDatabase.mealDao().upsert(meal)

        }
    }
    fun deleteMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }
}