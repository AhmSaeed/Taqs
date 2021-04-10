package com.iti.mad41.taqs.favourites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.iti.mad41.taqs.ServiceLocator
import com.iti.mad41.taqs.adapter.FavouritesWeatherAdapter
import com.iti.mad41.taqs.databinding.FavouritesFragmentBinding
import com.iti.mad41.taqs.map.SharedMapsViewModel
import com.iti.mad41.taqs.util.EventObserver
import com.iti.mad41.taqs.util.onQueryTextChanged
import com.iti.mad41.taqs.util.setupSnackbar

class FavouritesFragment : Fragment() {

    private lateinit var favouritesFragmentBinding: FavouritesFragmentBinding

    private lateinit var favouritesWeatherAdapter: FavouritesWeatherAdapter

    private val viewModel by viewModels<FavouritesViewModel>{
        FavouritesViewModelFactory(ServiceLocator.provideRepository(requireActivity().application))
    }

    private val sharedMapsViewModel: SharedMapsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        favouritesFragmentBinding = FavouritesFragmentBinding.inflate(inflater, container, false).apply {
            favouritesView = viewModel
        }
        return favouritesFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favouritesFragmentBinding.lifecycleOwner = this.viewLifecycleOwner
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_LONG,  {
            viewModel.deleteWeatherNode()
        })

        setFavouritesListAdapter()

        favouritesFragmentBinding.addPlaceFloatingActionButton.setOnClickListener{
            openMap()
        }

        favouritesFragmentBinding.searchView.onQueryTextChanged {
            viewModel.searchFavourites(it)
        }

        sharedMapsViewModel.locationDetails.observe(viewLifecycleOwner, EventObserver { locationDetails ->
            if(sharedMapsViewModel.isUpdate()){
                viewModel.saveWeatherNode(locationDetails)
                Log.i("sharedMapsViewModel", "onViewCreated: ${locationDetails.address}")
            }
        })

        viewModel.dataEmpty.observe(viewLifecycleOwner, Observer { isEmpty ->
            if(isEmpty) {
                toggleNoDataVisibility(View.VISIBLE)
            } else {
                toggleNoDataVisibility(View.GONE)
                toggleFavouritesListVisibility(View.VISIBLE)
            }
        })

        viewModel.openFavouriteItemDetailsEvent.observe(viewLifecycleOwner, EventObserver { id ->
            navigateToFavouriteDetails(id)
        })
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

    private fun toggleFavouritesListVisibility(visibility: Int){
        favouritesFragmentBinding.favouritesList.visibility = visibility
    }

    private fun toggleNoDataVisibility(visibility: Int){
        favouritesFragmentBinding.favouritesFragmentPlaceholder.visibility = visibility
        favouritesFragmentBinding.favouritesFragmentHandleTxtView.visibility = visibility
    }

    private fun openMap(){
        val action = FavouritesFragmentDirections.actionFavouritesFragmentToMapsFragment()
        findNavController().navigate(action)
    }

    private fun navigateToFavouriteDetails(id: Int) {
        val action = FavouritesFragmentDirections.actionFavouritesFragmentToFavouriteDetailsFragment(id)
        findNavController().navigate(action)
    }
}