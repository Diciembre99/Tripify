package com.dam2.tripify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.dam2.appmovil.R
import com.dam2.appmovil.databinding.ActivityAgregarClienteBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import modelo.Almacen
import modelo.Cliente

class AgregarCliente : AppCompatActivity() {
    lateinit var binding: ActivityAgregarClienteBinding
    private lateinit var firebaseauth : FirebaseAuth
    var TAG = "KRCC"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseauth = FirebaseAuth.getInstance()
        binding = ActivityAgregarClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAgregar.setOnClickListener {
            val usuario = FirebaseAuth.getInstance().currentUser
            if (todosLosCamposLlenos()) {
                val cliente = construirCliente()
                auxiliar.Conexion.guardarCliente(this, cliente)
                limpiarCampos()
            } else {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.title))
                    .setMessage(getString(R.string.messageFieldError))
                    .setPositiveButton(getString(R.string.positive_button)) { dialog, which ->
                    }
                    .show()
            }
        }
        binding.imgUser.setOnClickListener {
            fileUpload()
        }
    }


    fun construirCliente(): Cliente {
        val nombre = binding.txtNombre.text
        val apellido = binding.txtApellidos.text
        val telefono = binding.txtTelefono.text
        return Cliente(nombre.toString(), apellido.toString(), telefono.toString())
    }
    fun fileUpload() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d(TAG, "Selected URI: $uri")
            binding.imgUser.setImageURI(uri)
            Log.d(TAG, "Cargada")
            val Folder: StorageReference =
                FirebaseStorage.getInstance().getReference().child("cliente")
//            val file_name: StorageReference = Folder.child("" + uri!!.lastPathSegment) //<-- Podemos coger el último segmento de toda la ruta como nombre.
            val file_name: StorageReference =
                Folder.child(binding.txtTelefono.text.toString())  //<-- Podemos poner el nombre que queramos. lo leo de la caja de texto.
            file_name.putFile(uri).addOnSuccessListener { taskSnapshot ->
                file_name.getDownloadUrl().addOnSuccessListener { uri ->
                }
            }
        } else {
            Log.d(TAG, "No media selected")
        }
    }

    private fun limpiarCampos() {
        binding.txtApellidos.text!!.clear()
        binding.txtNombre.text!!.clear()
        binding.txtTelefono.text!!.clear()

    }
    private fun todosLosCamposLlenos(): Boolean {
        return !binding.txtApellidos.text.isNullOrEmpty() &&
                !binding.txtNombre.text.isNullOrEmpty() &&
                !binding.txtTelefono.text.isNullOrEmpty()
    }
}