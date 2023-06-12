package com.softdev.youtubeclone.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.softdev.youtubeclone.R
import com.softdev.youtubeclone.activities.MainActivity
import com.softdev.youtubeclone.adapters.MealsAdapter
import com.softdev.youtubeclone.databinding.FragmentSearchBinding
import com.softdev.youtubeclone.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecyclerviewAdapter:MealsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
//        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerViewAdapter()
        binding.imgSearchArrow.setOnClickListener{
            searchMeal()
        }
        observeSearchedMealsLiveData()

        var searchJob: Job?=null
        binding.edSearchBox.addTextChangedListener{ searchQuery->
            searchJob?.cancel()
            searchJob=lifecycleScope.launch {
                delay(500)
                viewModel.searchMeal(searchQuery.toString())
            }
        }


    }

    private fun observeSearchedMealsLiveData() {
  viewModel.observeSearchedMealsLiveData().observe(viewLifecycleOwner, Observer { mealList->

      searchRecyclerviewAdapter.differ.submitList(mealList)
  })
    }

    private fun searchMeal() {
       var searchQuery=binding.edSearchBox.text.toString()
        if(searchQuery.isNotEmpty()){
            viewModel.searchMeal(searchQuery)
        }

    }

    private fun prepareRecyclerViewAdapter() {
        searchRecyclerviewAdapter=MealsAdapter()
        binding.rvSearchedMeals.apply {
            layoutManager=GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter=searchRecyclerviewAdapter
        }
    }
}