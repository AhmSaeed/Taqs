package com.iti.mad41.taqs

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.iti.mad41.taqs.data.source.preferences.ISharedPreferencesDataSource
import com.iti.mad41.taqs.data.source.preferences.SharedISharedPreferencesDataSource
import com.iti.mad41.taqs.location.LocationViewModel
import com.iti.mad41.taqs.location.LocationViewModelFactory
import com.iti.mad41.taqs.settings.Language
import com.iti.mad41.taqs.util.MyContextWrapper

class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 2001;

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var ISharedPreferencesDataSource: ISharedPreferencesDataSource

    lateinit var locationViewModel: LocationViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigationDrawer()
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        locationViewModel = ViewModelProviders.of(this,
            LocationViewModelFactory(baseContext)
        ).get(LocationViewModel::class.java)

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration =
            AppBarConfiguration.Builder(
                    R.id.homeFragment,
                    R.id.favouritesFragment,
                    R.id.settingsFragment)
                .setDrawerLayout(drawerLayout)
                .build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.homeFragment -> {
                    supportActionBar?.setDisplayShowTitleEnabled(false)
                    true
                }
                R.id.favouritesFragment -> {
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    supportActionBar?.title = getString(R.string.favourites_header_title)
                    true
                }
                R.id.settingsFragment -> {
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    supportActionBar?.title = getString(R.string.preferences_header_title)
                    true
                }
                else -> {}
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        ISharedPreferencesDataSource = SharedISharedPreferencesDataSource(newBase!!)
        val language: String = ISharedPreferencesDataSource.getSelectedLanguage(Language.EN.value)!!
        super.attachBaseContext(MyContextWrapper.wrap(newBase, language))
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun setupNavigationDrawer() {
        drawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout))
            .apply {
                setStatusBarBackground(R.color.blue)
            }
    }
}