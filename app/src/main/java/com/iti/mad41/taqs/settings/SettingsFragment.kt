package com.iti.mad41.taqs.settings

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import com.iti.mad41.taqs.MainActivity
import com.iti.mad41.taqs.R
import com.iti.mad41.taqs.ServiceLocator
import com.iti.mad41.taqs.data.source.preferences.ISharedPreferencesDataSource
import com.iti.mad41.taqs.data.source.preferences.SharedISharedPreferencesDataSource
import com.iti.mad41.taqs.data.source.remote.WeatherRemoteDataSource
import com.iti.mad41.taqs.databinding.SettingsFragmentBinding
import com.iti.mad41.taqs.location.LocationViewModel
import com.iti.mad41.taqs.location.LocationViewModelFactory
import com.iti.mad41.taqs.map.SharedMapsViewModel
import com.iti.mad41.taqs.util.EventObserver
import com.iti.mad41.taqs.util.setupSnackbar

class SettingsFragment : Fragment() {
    private val AUTOCOMPLETE_REQUEST_CODE = 2006
    private val LOCATION_PERMISSION_REQUEST_CODE = 2001;

    private lateinit var settingsFragmentBinding: SettingsFragmentBinding

    private lateinit var locationViewModel: LocationViewModel

    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModelFactory(ServiceLocator.provideRepository(requireActivity().application))
    }

    private val sharedMapsViewModel: SharedMapsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsFragmentBinding = SettingsFragmentBinding.inflate(inflater, container, false).apply {
            settingsView = viewModel
        }
        return settingsFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
        settingsFragmentBinding.lifecycleOwner = this.viewLifecycleOwner

        viewModel.requestLocationPermissionEvent.observe(viewLifecycleOwner, EventObserver {
            prepareRequestLocationPermission()
        })

        viewModel.openMapEvent.observe(viewLifecycleOwner, EventObserver {
            openMap()
        })

        viewModel.reinitializeActivityEvent.observe(viewLifecycleOwner, EventObserver {
            restartMainActivity()
        })

        sharedMapsViewModel.locationDetails.observe(viewLifecycleOwner, EventObserver { locationDetails ->
            viewModel.saveAccessLocationType(AccessLocationType.Map.value)
            viewModel.saveLocation(
                locationDetails.latitude,
                locationDetails.longitude,
                locationDetails.address
            )
        })
    }

    private fun restartMainActivity(){
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun initAutoCompleteIntent(){
        val app = requireActivity().packageManager.getApplicationInfo(requireActivity().packageName, PackageManager.GET_META_DATA)
        val bundle: Bundle = app.metaData
        val apiKey = bundle.getString("com.google.android.geo.API_KEY")
        Places.initialize(requireActivity(), apiKey!!)
    }

    private fun navigateToAutoCompleteIntent(){
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(
                Place.Field.ID,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.NAME
        )

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(requireContext())
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    requestLocationUpdates()
                }  else {
                    viewModel.showSnackbarMessage(R.string.cannot_access_current_location_msg)
                }
            }
        }
    }

    private fun requestLocationUpdates(){
        locationViewModel = ViewModelProviders.of(this,
            LocationViewModelFactory(requireContext())
        ).get(LocationViewModel::class.java)
        locationViewModel.getLocationLiveData().observe(this, Observer {
            viewModel.saveLocation(it.latitude, it.longitude, it.address)
        })
    }

    private fun checkIsLocationPermissionGranted(): Boolean{
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false;
        }
        return true;
    }

    private fun prepareRequestLocationPermission(){
        if(!checkIsLocationPermissionGranted()){
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            val permissionRequest = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permissionRequest, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    private fun openMap(){
        val action = SettingsFragmentDirections.actionSettingsFragmentToMapsFragment()
        findNavController().navigate(action)
    }

}