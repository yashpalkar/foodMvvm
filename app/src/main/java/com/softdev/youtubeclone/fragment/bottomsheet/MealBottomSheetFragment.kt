package com.softdev.youtubeclone.fragment.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.putString
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softdev.youtubeclone.R
import com.softdev.youtubeclone.activities.MainActivity
import com.softdev.youtubeclone.activities.MealActivity
import com.softdev.youtubeclone.databinding.FragmentMealBottomSheetBinding
import com.softdev.youtubeclone.fragment.HomeFragment
import com.softdev.youtubeclone.viewModel.HomeViewModel

private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment(){
    // TODO: Rename and change types of parameters
    private var mealId: String? = null
    private lateinit var binding:FragmentMealBottomSheetBinding
    private lateinit var viewModel:HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
        viewModel=(activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root
//        return inflater.inflate(R.layout.fragment_meal_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealId?.let { viewModel.getMealById(it) }
        observeBottomSheetMeal()
        onBottomSheetDiaogClick()
    }

    private fun onBottomSheetDiaogClick() {
      binding.bottomSheet.setOnClickListener{
if(mealName!=null && mealThumb!=null){
    val intent= Intent(activity,MealActivity::class.java)
    intent.apply {
        putExtra(HomeFragment.Meal_ID,mealId)
        putExtra(HomeFragment.Meal_NAME,mealName)
        putExtra(HomeFragment.Meal_THUMB,mealThumb)
    }
    startActivity(intent)


}
      }
    }
    private var mealName:String?=null
    private var mealThumb:String?=null

    private fun observeBottomSheetMeal() {
        viewModel.observeBottomSheetMeal().observe(viewLifecycleOwner, Observer {
             meal->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgBottomSheet)
            binding.tvBottomsheetMealName.text=meal.strMeal
            binding.tvBottomsheetMealArea.text=meal.strArea
            binding.tvBottomsheetMealCategories.text=meal.strCategory
            mealName=meal.strMeal
            mealThumb=meal.strMealThumb
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)

                }
            }
    }

}