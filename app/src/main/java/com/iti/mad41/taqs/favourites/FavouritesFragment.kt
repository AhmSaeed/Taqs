package com.iti.mad41.taqs.favourites

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iti.mad41.taqs.R
import com.iti.mad41.taqs.adapter.FavouritesWeatherAdapter
import com.iti.mad41.taqs.adapter.HourlyWeatherAdapter
import com.iti.mad41.taqs.databinding.FavouritesFragmentBinding

class FavouritesFragment : Fragment() {

    private lateinit var viewModel: FavouritesViewModel

    private lateinit var favouritesFragmentBinding: FavouritesFragmentBinding

    private lateinit var favouritesWeatherAdapter: FavouritesWeatherAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favourites_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavouritesViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun setFavouritesListAdapter(){
        val viewModel = favouritesFragmentBinding.favouritesView
        if(viewModel != null) {
            favouritesWeatherAdapter = FavouritesWeatherAdapter(viewModel)
            favouritesFragmentBinding.favouritesList.adapter = favouritesWeatherAdapter
        } else {
            Log.i("Favourite", "setupFavouriteListAdapter: ")
        }
    }
}