package com.iti.mad41.taqs.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.iti.mad41.taqs.ServiceLocator
import com.iti.mad41.taqs.adapter.DailyWeatherAdapter
import com.iti.mad41.taqs.adapter.HourlyWeatherAdapter
import com.iti.mad41.taqs.data.source.preferences.ISharedPreferencesDataSource
import com.iti.mad41.taqs.data.source.preferences.SharedISharedPreferencesDataSource
import com.iti.mad41.taqs.data.source.remote.WeatherRemoteDataSource
import com.iti.mad41.taqs.databinding.HomeFragmentBinding
import com.iti.mad41.taqs.location.LocationViewModel
import com.iti.mad41.taqs.location.LocationViewModelFactory
import com.iti.mad41.taqs.settings.AccessLocationType
import com.iti.mad41.taqs.util.setupSnackbar

class HomeFragment : Fragment() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 2001;

    private lateinit var homeFragmentBinding: HomeFragmentBinding

    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter
    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter

    private lateinit var locationViewModel: LocationViewModel

    private val viewModel by viewModels<HomeViewModel>{
        HomeViewModelFactory(requireActivity().application, ServiceLocator.provideRepository(requireActivity().application))
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_LONG)
        homeFragmentBinding.lifecycleOwner = this.viewLifecycleOwner
        setupDailyListAdapter()
        setupHourlyListAdapter()
        prepareRequestLocationPermission()

        homeFragmentBinding.requestPermissionButton.setOnClickListener{
            navigateToAppSetting()
        }

        viewModel.isDataLoadingError.observe(viewLifecycleOwner, Observer { isError ->
            if(isError){
                toggleNoNetworkVisibility(View.VISIBLE)
                toggleHomeComponentsVisibility(View.GONE)
                toggleLocationPermissionVisibility(View.GONE)
            }
        })

        viewModel.accessLocationType.observe(viewLifecycleOwner, Observer {type ->
            if(type.equals(AccessLocationType.Map.value)){
                locationViewModel.getLocationLiveData().removeObservers(viewLifecycleOwner)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if(checkIsLocationPermissionGranted()){
            toggleHomeComponentsVisibility(View.VISIBLE)
            toggleLocationPermissionVisibility(View.GONE)
        }
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
                    toggleHomeComponentsVisibility(View.GONE)
                    toggleLocationPermissionVisibility(View.VISIBLE)
                }
            }
        }
    }

    private fun setupDailyListAdapter(){
        val viewModel = homeFragmentBinding.homeView
        if(viewModel != null) {
            dailyWeatherAdapter = DailyWeatherAdapter()
            homeFragmentBinding.dailyWeatherList.adapter = dailyWeatherAdapter
        } else {
            Log.i("Home", "setupDailyListAdapter: ")
        }
    }

    private fun setupHourlyListAdapter(){
        val viewModel = homeFragmentBinding.homeView
        if(viewModel != null) {
            hourlyWeatherAdapter = HourlyWeatherAdapter()
            homeFragmentBinding.hourlyWeatherList.adapter = hourlyWeatherAdapter
        } else {
            Log.i("Home", "setupHourlyListAdapter: ")
        }
    }

    private fun requestLocationUpdates(){
        locationViewModel = ViewModelProviders.of(this,
            LocationViewModelFactory(requireContext())).get(LocationViewModel::class.java)
        Log.i("requestLocationUpdates", "first")
        locationViewModel.getLocationLiveData().observe(viewLifecycleOwner, Observer {
            Log.i("requestLocationUpdates", "requestLocationUpdates: ${it}")
            viewModel.saveLocation(it.latitude, it.longitude, it.address)
            homeFragmentBinding.cityTxtView.text = it.address
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
        } else {
            requestLocationUpdates()
        }
    }

    private fun toggleHomeComponentsVisibility(visibility: Int){
        homeFragmentBinding.cityTxtView.visibility = visibility
        homeFragmentBinding.dateTimeTxtView.visibility = visibility
        homeFragmentBinding.currentWeatherCardView.visibility = visibility
        homeFragmentBinding.currentWeatherIcon.visibility = visibility
        homeFragmentBinding.currentWeatherExtraCardView.visibility = visibility
        homeFragmentBinding.hourlyWeatherList.visibility = visibility
        homeFragmentBinding.dailyWeatherList.visibility = visibility
    }

    private fun toggleLocationPermissionVisibility(visibility: Int){
        homeFragmentBinding.requestPermissionCardView.visibility = visibility
        homeFragmentBinding.requestPermissionButton.visibility = visibility
    }

    private fun toggleNoNetworkVisibility(visibility: Int){
        homeFragmentBinding.noNetworkImagePlaceholder.visibility = visibility
        homeFragmentBinding.tryAgainBtn.visibility = visibility
    }

    private fun navigateToAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity?.packageName, null)
        intent.setData(uri)
        startActivity(intent)
    }

}