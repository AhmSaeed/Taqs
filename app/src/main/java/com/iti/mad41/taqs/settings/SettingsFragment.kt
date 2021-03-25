package com.iti.mad41.taqs.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.iti.mad41.taqs.MainActivity
import com.iti.mad41.taqs.data.repo.DefaultWeatherRepository
import com.iti.mad41.taqs.data.repo.WeatherRepository
import com.iti.mad41.taqs.data.source.preferences.PreferencesDataSource
import com.iti.mad41.taqs.data.source.preferences.SharedPreferencesDataSource
import com.iti.mad41.taqs.data.source.remote.WeatherRemoteDataSource
import com.iti.mad41.taqs.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {
    private lateinit var settingsFragmentBinding: SettingsFragmentBinding;

    private lateinit var weatherRemoteDataSource: WeatherRemoteDataSource

    private lateinit var preferencesDataSource: PreferencesDataSource

    private lateinit var weatherRepository: WeatherRepository

    private val viewModel by viewModels<SettingsViewModel>(){
        weatherRemoteDataSource = WeatherRemoteDataSource()
        preferencesDataSource = SharedPreferencesDataSource(requireContext())
        weatherRepository = DefaultWeatherRepository(weatherRemoteDataSource, preferencesDataSource)
        SettingsViewModelFactory(requireActivity().application, weatherRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsFragmentBinding = SettingsFragmentBinding.inflate(inflater, container, false).apply {
            settingsView = viewModel
        }
        return settingsFragmentBinding.root;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        settingsFragmentBinding.lifecycleOwner = this.viewLifecycleOwner

        viewModel.reinitializeActivityEvent.observe(viewLifecycleOwner, Observer {
            restartMainActivity()
        })
    }

    private fun restartMainActivity(){
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

}