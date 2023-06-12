package com.softdev.youtubeclone.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.softdev.youtubeclone.R
import com.softdev.youtubeclone.databinding.ActivityMealBinding
import com.softdev.youtubeclone.db.MealDatabase
import com.softdev.youtubeclone.fragment.HomeFragment
import com.softdev.youtubeclone.pojo.Meal
import com.softdev.youtubeclone.viewModel.MealViewModel
import com.softdev.youtubeclone.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealMvvm: MealViewModel
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeStr: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
//        mealMvvm= ViewModelProviders.of(this)[MealViewModel::class.java]
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInformationfromIntent()
        setInformationInViews()
        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observerMealDetailLiveData()
        onYoutubeImageClick()
        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnAddToFav.setOnClickListener {
            mealtoSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var mealtoSave: Meal? = null
    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    youtubeStr
                )
            )
            startActivity(intent)
        }
    }

    private fun observerMealDetailLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(value: Meal) {
                onResponseCase()
                mealtoSave = value
                binding.tvCategory.text = "Category: ${value!!.strCategory}"
                binding.tvArea.text = "Area: ${value!!.strArea}"
                binding.tvInstructions.text = value!!.strInstructions
                youtubeStr = value.strYoutube.toString()
            }

        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext).load(mealThumb).into(binding.imgMealDeatil)
        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))


    }

    private fun getMealInformationfromIntent() {
        var intent = getIntent()
        mealId = intent.getStringExtra(HomeFragment.Meal_ID).toString();
        mealName = intent.getStringExtra(HomeFragment.Meal_NAME).toString();
        mealThumb = intent.getStringExtra(HomeFragment.Meal_THUMB).toString();
    }

    private fun loadingCase() {
        binding.progressbar.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.progressbar.visibility = View.INVISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }
}