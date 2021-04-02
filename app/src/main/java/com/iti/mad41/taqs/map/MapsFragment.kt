package com.iti.mad41.taqs.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.iti.mad41.taqs.R
import com.iti.mad41.taqs.data.repo.DefaultWeatherRepository
import com.iti.mad41.taqs.data.repo.WeatherRepository
import com.iti.mad41.taqs.data.source.preferences.PreferencesDataSource
import com.iti.mad41.taqs.data.source.preferences.SharedPreferencesDataSource
import com.iti.mad41.taqs.data.source.remote.WeatherRemoteDataSource
import com.iti.mad41.taqs.location.LocationViewModel
import com.iti.mad41.taqs.location.LocationViewModelFactory
import com.iti.mad41.taqs.util.ACCESS_LOCATION_WITH_MAP
import com.iti.mad41.taqs.util.setupSnackbar

class MapsFragment : Fragment() {
    private val AUTOCOMPLETE_REQUEST_CODE = 2006
    private val LOCATION_PERMISSION_REQUEST_CODE = 2001;
    private val MAX_RESULT_VALUE = 1
    private var latitude = 36.7783
    private var longitude = 119.4179

    private lateinit var locationViewModel: LocationViewModel

    private lateinit var mapFragment: SupportMapFragment

    private lateinit var viewModel: MapsViewModel

    private lateinit var weatherRemoteDataSource: WeatherRemoteDataSource

    private lateinit var preferencesDataSource: PreferencesDataSource

    private lateinit var weatherRepository: WeatherRepository

    private val onMapReadyCallback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        googleMap.clear()
        var latLong = LatLng(
            latitude,
            longitude
        )
        var address = getLocationGeoCoding(latitude, longitude)
        viewModel.setLocationDetails(latitude, longitude, address)
        googleMap.addMarker(MarkerOptions().position(latLong))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLong))
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.setOnMapClickListener {
            googleMap.clear()
            var address = getLocationGeoCoding(it.latitude, it.longitude)
            googleMap.addMarker(MarkerOptions().position(it).title(address))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            viewModel.setLocationDetails(it.latitude, it.longitude, address)
            viewModel.showSnackbarMessage(R.string.confirm_location_message)
        }
    }

    private fun getLocationGeoCoding(lat: Double, long: Double): String {
        val geoCoder = Geocoder(context)
        val locationsResult = geoCoder.getFromLocation(lat, long, MAX_RESULT_VALUE)

        return if(locationsResult != null && locationsResult.size != 0) locationsResult[0].adminArea else ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRequestLocationPermission()

        weatherRemoteDataSource = WeatherRemoteDataSource()
        preferencesDataSource = SharedPreferencesDataSource(requireContext())
        weatherRepository = DefaultWeatherRepository(weatherRemoteDataSource, preferencesDataSource)
        viewModel = ViewModelProviders.of(this, MapsViewModelFactory(weatherRepository)).get(MapsViewModel::class.java)

        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_LONG, object: View.OnClickListener {
            override fun onClick(view: View?) {
                viewModel.saveLocationIncludingType(ACCESS_LOCATION_WITH_MAP)
                findNavController().popBackStack()
            }
        })

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(onMapReadyCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Log.i("HomeFragment", "requestLocationUpdates:")
                    requestLocationUpdates()
            }
        }
    }

    private fun requestLocationUpdates(){
        locationViewModel = ViewModelProviders.of(this,
            LocationViewModelFactory(requireContext())
        ).get(LocationViewModel::class.java)
        locationViewModel.getLocationLiveData().observe(viewLifecycleOwner, Observer {
            Log.i("HomeFragment", "requestLocationUpdates: ${it.latitude}")
            Log.i("HomeFragment", "requestLocationUpdates: ${it.longitude}")
            latitude = it.latitude
            longitude = it.longitude
            mapFragment.getMapAsync(onMapReadyCallback)
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
}