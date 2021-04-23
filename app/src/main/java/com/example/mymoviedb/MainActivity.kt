package com.example.mymoviedb

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav.setOnNavigationItemSelectedListener(navListner)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,HomeFragment.newInstance()).commit()
    }

    var navListner : BottomNavigationView.OnNavigationItemSelectedListener = object:BottomNavigationView.OnNavigationItemSelectedListener {
        var tag : String = ""
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            var fragmentSelected : Fragment ?= null

            when (item.itemId){
                R.id.category -> {
                    fragmentSelected = CategoryFragment()
                    tag = "Genres"
                }
                R.id.home -> {
                    fragmentSelected = HomeFragment()
                    tag = "Home"
                }
                R.id.profile -> {
                    fragmentSelected = ProfileFragment()
                    tag = "Profile"
                }

            }
            if (fragmentSelected != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragmentSelected,tag).commit()
            }
            return  true;
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            var fragmentManager : FragmentManager = supportFragmentManager
            fragmentManager.popBackStackImmediate()
        }
        return true
    }
}