package com.dam2.tripify

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import auxiliar.Conexion
import auxiliar.ConexionSQLite
import com.dam2.appmovil.R
import com.dam2.appmovil.databinding.ActivityFragmentsBinding
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import modelo.Almacen
import modelo.Lugares
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
            R.id.mnOp2 -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(resources.getString(R.string.titleAbout))
                    .setMessage(resources.getString(R.string.messageAbout))
                    .setPositiveButton(resources.getString(R.string.positive_button)) { dialog, which ->
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragments)
        var binding = ActivityFragmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        notificacion()
        firebaseauth = FirebaseAuth.getInstance()
        var nombre:String = intent.getStringExtra("nombre").toString()
        var email:String = intent.getStringExtra("email").toString()
        var provider:String = intent.getStringExtra("provider").toString()
        var user: Usuario = Usuario(nombre,email,provider)
        binding.viewPager.adapter = AdaptadorViewPage(this)

        Lugares.lugares = ConexionSQLite.obtenerLugares(this)
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,index->
            tab.text = when(index){
                0->{"Viajes"}
                1->{"Agregar Viajes"}
                2->{"Informacion de usuarios"}
                3->{"Cartera de clientes"}
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
    fun notificacion(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, token)
        })
    }
}