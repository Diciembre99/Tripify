package com.dam2.tripify

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.widget.Toast
import com.dam2.appmovil.R
import com.dam2.appmovil.databinding.ActivityRegisterBinding
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseauth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseauth = FirebaseAuth.getInstance()
        //Evitamos que se puedan dar saltos de linea en los TextView con un inputFilter
        val inputFilter = InputFilter { source, _, _, _, _, _ ->
            for (i in source.indices) {
                if (source[i] == '\n') {
                    return@InputFilter "" // Eliminar saltos de línea
                }
            }
            null
        }

        // Aplicar el InputFilter al EditText
        binding.txtName.filters = arrayOf(inputFilter)
        binding.txtEmail.filters = arrayOf(inputFilter)
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            if (binding.txtEmail.text!!.isNotEmpty() && binding.txtPassword.text!!.isNotEmpty() && binding.txtName.text!!.isNotEmpty() && binding.txtPasswordConfirmed.text!!.isNotEmpty()) {
                if (confirmarClave()){
                    firebaseauth.createUserWithEmailAndPassword(
                        binding.txtEmail.text.toString(),
                        binding.txtPassword.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this,"Registrado con exito", Toast.LENGTH_SHORT).show()
                            var email = binding.txtEmail.text.toString()
                            var name = binding.txtName.text.toString()
                            var password = binding.txtPassword.text.toString()
                            auxiliar.Conexion.guardarUsuario(this, email, name, password)
                            acceder(email, name, password)
                        } else {
                            showAlert(R.string.messageRegister)
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Conexión no establecida", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                showAlert(R.string.messageFieldError)
            }
        }
    }
    fun showAlert(mensaje: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.title))
            .setMessage(resources.getString(mensaje))
            .setIcon(resources.getDrawable(R.drawable.ic_message_error))
            .setPositiveButton(resources.getString(R.string.positive_button)) { dialog, which ->
            }
            .show()
    }


    private fun confirmarClave():Boolean {
        return if (binding.txtPassword.text.toString()!! == binding.txtPasswordConfirmed.text.toString()){
            true
        }else{
            Toast.makeText(this, "Verifica que tu contrasena sea correcta",Toast.LENGTH_SHORT).show()
            false
        }
    }
    private fun acceder(email:String,nombre:String,password:String){
        var provider =Proveedor.BASIC
        Log.e(ContentValues.TAG, "Valores: ${email}, ${provider}, $nombre")
        val homeIntent = Intent(this, Menu::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
            putExtra("nombre", nombre)
        }
        startActivity(homeIntent)
    }

}