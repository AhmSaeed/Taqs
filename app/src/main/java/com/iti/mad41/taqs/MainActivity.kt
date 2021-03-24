package com.iti.mad41.taqs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.iti.mad41.taqs.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigationDrawer()
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration =
            AppBarConfiguration.Builder(R.id.homeFragment, R.id.settingsFragment)
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
                R.id.settingsFragment -> {
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    supportActionBar?.title = "Preferences"
                    true
                }
                else -> {}
            }
        }
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