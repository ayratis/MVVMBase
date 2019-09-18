package com.ayratis.abstractapp.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.ayratis.abstractapp.R
import com.ayratis.abstractapp.arch.setupWithNavController
import com.ayratis.abstractapp.ui.views.CenteredTitleToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {

    private var bottomNavigationView: BottomNavigationView? = null
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById<CenteredTitleToolbar>(R.id.toolbar))
        if (savedInstanceState == null) setupBottomNavigationBar()
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        bottomNavigationView = findViewById(R.id.bottom_nav)

        val navGraphIds = listOf(
            R.navigation.home,
            R.navigation.dashboard,
            R.navigation.notifications
        )

        val controller = bottomNavigationView?.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        controller?.observe(this, Observer { navController ->
            Log.d("CurrentNavController", navController.toString())
            setupActionBarWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                supportActionBar?.apply {
                    setHomeAsUpIndicator(R.drawable.ic_arrow_back)
                }
            }
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

}
