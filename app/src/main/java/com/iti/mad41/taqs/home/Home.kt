package com.iti.mad41.taqs.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.iti.mad41.taqs.R
import com.iti.mad41.taqs.adapter.DailyWeatherAdapter
import com.iti.mad41.taqs.data.repo.DefaultWeatherRepository
import com.iti.mad41.taqs.data.repo.WeatherRepository
import com.iti.mad41.taqs.data.source.remote.WeatherRemoteDataSource
import com.iti.mad41.taqs.databinding.HomeFragmentBinding

class Home : Fragment() {

    private lateinit var homeFragmentBinding: HomeFragmentBinding

    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter

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
        homeFragmentBinding.lifecycleOwner = this.viewLifecycleOwner

        return homeFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
        // Set the lifecycle owner to the lifecycle of the view
        homeFragmentBinding.lifecycleOwner = this.viewLifecycleOwner
    }

}