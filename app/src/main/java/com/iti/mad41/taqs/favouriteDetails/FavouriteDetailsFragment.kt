package com.iti.mad41.taqs.favouriteDetails

import android.app.Service
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.iti.mad41.taqs.R
import com.iti.mad41.taqs.ServiceLocator
import com.iti.mad41.taqs.adapter.DailyWeatherAdapter
import com.iti.mad41.taqs.adapter.HourlyWeatherAdapter
import com.iti.mad41.taqs.databinding.FavouriteDetailsFragmentBinding

class FavouriteDetailsFragment : Fragment() {

    private lateinit var favouriteDetailsFragmentBinding: FavouriteDetailsFragmentBinding

    private val args: FavouriteDetailsFragmentArgs by navArgs()

    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter

    private val viewModel by viewModels<FavouriteDetailsViewModel>{
        FavouriteDetailsViewModelFactory(ServiceLocator.provideRepository(requireActivity().application))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        favouriteDetailsFragmentBinding = FavouriteDetailsFragmentBinding.inflate(inflater, container, false).apply {
            favouriteDetailsView = viewModel
        }

        viewModel.start(args.weatherNodeId)

        return favouriteDetailsFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouriteDetailsFragmentBinding.lifecycleOwner = this.viewLifecycleOwner
        setupDailyListAdapter()
        setupHourlyListAdapter()
    }

    private fun setupDailyListAdapter(){
        val viewModel = favouriteDetailsFragmentBinding.favouriteDetailsView
        if(viewModel != null) {
            dailyWeatherAdapter = DailyWeatherAdapter()
            favouriteDetailsFragmentBinding.dailyWeatherList.adapter = dailyWeatherAdapter
        } else {
            Log.i("FavouriteDetails", "setupDailyListAdapter: ")
        }
    }

    private fun setupHourlyListAdapter(){
        val viewModel = favouriteDetailsFragmentBinding.favouriteDetailsView
        if(viewModel != null) {
            hourlyWeatherAdapter = HourlyWeatherAdapter()
            favouriteDetailsFragmentBinding.hourlyWeatherList.adapter = hourlyWeatherAdapter
        } else {
            Log.i("FavouriteDetails", "setupHourlyListAdapter: ")
        }
    }
}