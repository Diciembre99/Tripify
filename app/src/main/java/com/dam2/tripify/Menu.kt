package com.dam2.tripify

// MainActivity.kt
import HomeFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dam2.appmovil.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Menu : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val clientesFragment = Fragment()

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(homeFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_clientes -> {
                    loadFragment(clientesFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // Carga el fragmento inicial
        loadFragment(homeFragment)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}
