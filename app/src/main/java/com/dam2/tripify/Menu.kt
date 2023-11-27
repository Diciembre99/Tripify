package com.dam2.tripify

// MainActivity.kt
import HomeFragment
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dam2.appmovil.R
import com.dam2.appmovil.R.id.mnOp1
import com.dam2.appmovil.databinding.ActivityMenuBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import modelo.Almacen
import modelo.Usuario

class Menu : AppCompatActivity() {


    private val homeFragment = HomeFragment()
    private val clientesFragment = Fragment()
    private val agregarViajeragment = AgregarViaje()
    private lateinit var binding: ActivityMenuBinding
    private lateinit var firebaseauth : FirebaseAuth
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.up_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            mnOp1 -> {
                firebaseauth.signOut()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
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
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        //Menu superior
        val bienvenido = R.string.Bienveido
        binding.toolbar.title = "$bienvenido $nombre"
        //aquí simplemente inflo la toolBaar, pero aún no hay opciones ni botón home.
        setSupportActionBar(binding.toolbar)
        loadFragment(homeFragment)
    }



    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}
