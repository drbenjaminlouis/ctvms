package com.example.ctvms

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class adminHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        if (!NetworkUtils.isNetworkAvailable(this)) {
            NetworkUtils.showNetworkAlert(this)
        } else {
            // Network is available, proceed with your logic
        }
        val navController = findNavController(R.id.mainfragcontainer)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)
    }
}
