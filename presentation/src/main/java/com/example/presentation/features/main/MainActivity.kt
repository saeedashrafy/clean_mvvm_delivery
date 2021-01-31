package com.example.presentation.features.main

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.market.core.snack
import com.example.presentation.R
import com.example.presentation.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {


    private val mainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var navController: NavController
    private var backPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)
        setupViews()

    }

    private fun setupViews() {
        mainBinding.bottomNavigationView.setOnNavigationItemReselectedListener {
            if (it.itemId != mainBinding.bottomNavigationView.selectedItemId)
                NavigationUI.onNavDestinationSelected(it, navController)
            true
        }

        setSupportActionBar(mainBinding.toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        //mainBinding.toolbarTitle.text = title ?: ""
        // Finding the Navigation Controller
        var navHostFragment = supportFragmentManager.findFragmentById(R.id.fragNavHost) as NavHostFragment
        navController = navHostFragment.navController

        // Setting Navigation Controller with the BottomNavigationView
        NavigationUI.setupWithNavController(mainBinding.bottomNavigationView, navController)

        //var appBarConfiguration = AppBarConfiguration(navHostFragment.navController.graph)
        var appBarConfiguration = AppBarConfiguration(setOf(R.id.foodsFragment, R.id.profileFragment, R.id.cardFragment))
        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onBackPressed() {
        if (navController.graph.startDestination == navController.currentDestination?.id) {
            if (backPressedOnce) {
                super.onBackPressed()
                return
            }

            backPressedOnce = true
            mainBinding.fragNavHost.snack("برای خروج مجددا دکمه بازگشت را فشار دهید")

            lifecycleScope.launch {
                delay(2000)
                backPressedOnce = false
            }
        } else {
            super.onBackPressed()
        }
    }


}