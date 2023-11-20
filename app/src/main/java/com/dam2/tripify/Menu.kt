package com.dam2.tripify

// MainActivity.kt
import HomeFragment
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.dam2.appmovil.R
import com.dam2.appmovil.databinding.ActivityMenuBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import modelo.Almacen
import modelo.Usuario

class Menu : AppCompatActivity() {


    private val homeFragment = HomeFragment()
    private val clientesFragment = Fragment()
    private val agregarViajeragment = AgregarViaje()
    private lateinit var binding: ActivityMenuBinding

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
                R.id.agregarViaje -> {
                    loadFragment(agregarViajeragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var nombre:String = intent.getStringExtra("nombre").toString()
        var email:String = intent.getStringExtra("email").toString()
        var provider:String = intent.getStringExtra("provider").toString()
        var user:Usuario = Usuario(nombre,email,provider)
        Almacen.usuario = user
        binding.tvBienvenido.setText(nombre)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        // Carga el fragmento inicial
        loadFragment(homeFragment)
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}
