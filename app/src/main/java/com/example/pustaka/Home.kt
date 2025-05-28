package com.example.pustaka

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.pustaka.basic_api.ui.view.LoanFragment
import com.example.pustaka.basic_api.ui.view.ProfileFragment
import com.example.pustaka.basic_api.ui.view.FragmentHome
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {
    private lateinit var toolbarText: TextView
    private lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_home)

        toolbar = findViewById(R.id.toolbar)

        toolbarText = findViewById(R.id.toolbarText)

        if (savedInstanceState == null) {
            loadFragment(FragmentHome())
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(FragmentHome())
                    true
                }
                R.id.nav_loan -> {
                    loadFragment(LoanFragment())
                    toolbarText.text = "Library"
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    toolbarText.text = "Profile"
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}