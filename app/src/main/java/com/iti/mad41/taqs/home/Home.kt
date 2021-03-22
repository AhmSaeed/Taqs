package com.iti.mad41.taqs.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.iti.mad41.taqs.R
import com.iti.mad41.taqs.adapter.DailyWeatherAdapter
import com.iti.mad41.taqs.adapter.HourlyWeatherAdapter
import com.iti.mad41.taqs.adapter.HourlyWeatherBindingAdapter
import com.iti.mad41.taqs.data.repo.DefaultWeatherRepository
import com.iti.mad41.taqs.data.repo.WeatherRepository
import com.iti.mad41.taqs.data.source.remote.WeatherRemoteDataSource
import com.iti.mad41.taqs.databinding.HomeFragmentBinding

class Home : Fragment() {

    private lateinit var homeFragmentBinding: HomeFragmentBinding

    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter
    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter

    private lateinit var weatherRemoteDataSource: WeatherRemoteDataSource

    private lateinit var weatherRepository: WeatherRepository

    private val viewModel by viewModels<HomeViewModel>(){
        weatherRemoteDataSource = WeatherRemoteDataSource()
        weatherRepository = DefaultWeatherRepository(weatherRemoteDataSource)
        HomeViewModelFactory(weatherRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeFragmentBinding = HomeFragmentBinding.inflate(inflater, container, false).apply {
            homeView = viewModel
        }

        return homeFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeFragmentBinding.lifecycleOwner = this.viewLifecycleOwner
        setupDailyListAdapter()
        setupHourlyListAdapter()

        viewModel.isDataLoadingError.observe(viewLifecycleOwner, Observer { isDataLoadingError ->
            if(!isDataLoadingError){

            }
        })
    }

    private fun setupDailyListAdapter(){
        val viewModel = homeFragmentBinding.homeView
        if(viewModel != null) {
            dailyWeatherAdapter = DailyWeatherAdapter(viewModel)
            homeFragmentBinding.dailyWeatherList.adapter = dailyWeatherAdapter
        } else {
            Log.i("Home", "setupDailyListAdapter: ")
        }
    }

    private fun setupHourlyListAdapter(){
        val viewModel = homeFragmentBinding.homeView
        if(viewModel != null) {
            hourlyWeatherAdapter = HourlyWeatherAdapter(viewModel)
            homeFragmentBinding.hourlyWeatherList.adapter = hourlyWeatherAdapter
        } else {
            Log.i("Home", "setupHourlyListAdapter: ")
        }
    }

}