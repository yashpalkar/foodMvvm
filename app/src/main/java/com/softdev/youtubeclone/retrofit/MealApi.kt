package com.softdev.youtubeclone.retrofit

import com.softdev.youtubeclone.pojo.CategoryList
import com.softdev.youtubeclone.pojo.MealsByCategoryList
import com.softdev.youtubeclone.pojo.MealList
import com.softdev.youtubeclone.pojo.MealsByCategory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php")
    fun getMealbyID(
        @Query("i") id: String
    ): Call<MealList>

    @GET("filter.php")
    fun getPopularitem(
        @Query("c")category:String):Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategoriesitem():Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String):Call<MealsByCategoryList>

    @GET("search.php")
    fun getMealBySearch(@Query("s") searchQuery: String):Call<MealList>
}