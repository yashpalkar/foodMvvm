package com.softdev.youtubeclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.softdev.youtubeclone.databinding.MealItemBinding
import com.softdev.youtubeclone.pojo.MealsByCategory

class CategoryMealAdapter : RecyclerView.Adapter<CategoryMealAdapter.CategoryMealsViewModel>() {
    private var mealList = ArrayList<MealsByCategory>()

    fun setMealsList(mealList: List<MealsByCategory>) {
        this.mealList = mealList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    inner class CategoryMealsViewModel(val binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewModel {
        return CategoryMealsViewModel(
            MealItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: CategoryMealsViewModel, position: Int) {
        Glide.with(holder.itemView).load(mealList.get(position).strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealList[position].strMeal
    }

    override fun getItemCount(): Int {
   return mealList.size
    }
}