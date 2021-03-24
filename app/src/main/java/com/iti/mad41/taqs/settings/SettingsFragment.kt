package com.iti.mad41.taqs.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.iti.mad41.taqs.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {
    private lateinit var settingsFragmentBinding: SettingsFragmentBinding;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsFragmentBinding = SettingsFragmentBinding.inflate(inflater, container, false).apply {

        }
        return settingsFragmentBinding.root;
    }

}