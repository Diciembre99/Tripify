package com.dam2.tripify

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.dam2.appmovil.R
import com.dam2.appmovil.databinding.ActivityFragmentsBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import modelo.Almacen
import modelo.Usuario

class ActivityFragments : AppCompatActivity() {

    lateinit var binding: ActivityFragments
    var TAG: String = "KRCC"
    private lateinit var firebaseauth : FirebaseAuth
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.up_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mnOp1 -> {
                firebaseauth.signOut()
                val signInClient = Identity.getSignInClient(this)
                signInClient.signOut()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragments)
        var binding = ActivityFragmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseauth = FirebaseAuth.getInstance()
        var nombre:String = intent.getStringExtra("nombre").toString()
        var email:String = intent.getStringExtra("email").toString()
        var provider:String = intent.getStringExtra("provider").toString()
        var user: Usuario = Usuario(nombre,email,provider)
        binding.viewPager.adapter = AdaptadorViewPage(this)


        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,index->
            tab.text = when(index){
                0->{"Viajes"}
                1->{"Agregar Viajes"}
                2->{"Informacion de usuarios"}
                else->{throw Resources.NotFoundException("Posición no encontrada") }
            }
        }.attach()
        //Menu superior
        Log.e(TAG, nombre)
        Log.e(TAG, email)
        var bienvenido = resources.getString(R.string.Bienveido)
        binding.toolbar.title = "$bienvenido $nombre"
        setSupportActionBar(binding.toolbar)
    }
}