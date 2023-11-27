package com.dam2.tripify

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import auxiliar.Conexion
import com.dam2.appmovil.R
import com.dam2.appmovil.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseauth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseauth = FirebaseAuth.getInstance()
        firebaseauth.signOut()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        binding.btnGoogle.setOnClickListener {
            loginEnGoogle()
        }

        binding.btnLogin.setOnClickListener {
            if (binding.txtEmail.text!!.isNotEmpty() && binding.txtPassword.text!!.isNotEmpty()) {
                firebaseauth.signInWithEmailAndPassword(
                    binding.txtEmail.text.toString(),
                    binding.txtPassword.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Conexion.cargarViajes(it.result?.user?.email ?: "")
                        irHome(
                            it.result?.user?.email ?: "",
                            Proveedor.BASIC
                        )  //Esto de los interrogantes es por si está vacío el email.
                    } else {
                        showAlert(R.string.messageRegister)
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Conexión no establecida", Toast.LENGTH_SHORT).show()
                }
            } else {
                showAlert(R.string.messageFieldError)
            }
        }
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        val logoImageView = findViewById<ImageView>(R.id.logoImageView)
        val contentLayout = findViewById<ScrollView>(R.id.scrollView) // Reemplaza con tu ID de Layout
        // Cargar las animaciones desde los archivos XML
        val fadeInAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_slide_in)
        val slideFromTopAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_content)
        // Agregar un listener a la animación del logo
        fadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                contentLayout.startAnimation(slideFromTopAnimation)
            }
            override fun onAnimationEnd(animation: Animation) {
            }

            override fun onAnimationRepeat(animation: Animation) {
                // No es necesario hacer algo en repeticiones de la animación
            }
        })
        // Aplicar la animación al ImageView
        logoImageView.startAnimation(fadeInAnimation)
    }
    private fun loginEnGoogle(){
        val signInClient = googleSignInClient.signInIntent
        launcherVentanaGoogle.launch(signInClient)

    }


    private val launcherVentanaGoogle =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            manejarResultados(task)
        }
    }

    private fun manejarResultados(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if (account != null){
                actualizarUI(account)
            }
        }
        else {
            Toast.makeText(this,task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        //pido un token, y con ese token, si todo va bien obtengo la info.
        firebaseauth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                //hacer account. y ver otras propiedades interesantes.
                Conexion.cargarViajes(account.email.toString())
                irHome(account.email.toString(),Proveedor.GOOGLE, account.displayName.toString())
            }
            else {
                Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAlert(mensaje:Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.title))
            .setMessage(resources.getString(mensaje))
            .setIcon(resources.getDrawable(R.drawable.ic_message_error))
            .setPositiveButton(resources.getString(R.string.positive_button)) { dialog, which ->
            }
            .show()
    }

    private fun irHome(email: String, provider: Proveedor, nombre: String = "Usuario") {
        Log.e(ContentValues.TAG, "Valores: ${email}, ${provider}, ${nombre}")
        val homeIntent = Intent(this, Menu::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
            putExtra("nombre", nombre)
        }
        startActivity(homeIntent)
    }
}