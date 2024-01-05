package com.dam2.tripify

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import auxiliar.AdaptadorRecyclerCliente
import com.dam2.appmovil.databinding.ActivityInfoCliente2Binding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import modelo.Almacen
import modelo.AlmacenCliente
import modelo.Cliente
import java.io.File

class infoCliente2 : AppCompatActivity() {

    var storage = Firebase.storage
    var storageRef = storage.reference
    val TAG = "KRCC"
    lateinit var binding: ActivityInfoCliente2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoCliente2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        var c = intent.getSerializableExtra("obj") as Cliente

        val apellidos = c.apellido
        var nomImagen = apellidos
        //var spaceRef = storageRef.child("images/saturno.webp")
        var spaceRef = storageRef.child("cliente/${Almacen.usuario.correo}/$nomImagen")
        val localfile = File.createTempFile("tempImage", "jpg")
        spaceRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imgUser.setImageBitmap(bitmap)
        }.addOnFailureListener {
        }
        if (Almacen.usuario.rol == "pasajero"){

            binding.swEdit.isVisible = false
        }
        binding.txtNombre.setText(c.nombre)
        binding.txtApellidos.setText(c.apellido)
        binding.txtTelefono.setText(c.numero)


        binding.swEdit.setOnCheckedChangeListener { _, isChecked ->
            binding.txtNombre.isEnabled = isChecked
            binding.txtApellidos.isEnabled = isChecked
            binding.txtTelefono.isEnabled = isChecked
            binding.btnGuardar.isVisible = isChecked
        }
        binding.imgUser.setOnClickListener{
            fileUpload()
        }
        binding.btnGuardar.setOnClickListener {
            val cliente = Cliente(
                binding.txtNombre.text.toString(),
                binding.txtApellidos.text.toString(),
                binding.txtTelefono.text.toString(),
                c.llave
            )
            auxiliar.Conexion.modificarCliente(cliente, this)
        }

        //Menu superior
        binding.toolbarDetalle.setTitle("Cliente " + c.nombre)
        setSupportActionBar(binding.toolbarDetalle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarDetalle.setNavigationOnClickListener {
            val miAdapter = AdaptadorRecyclerCliente(AlmacenCliente.Clientes,this )
            finish()
        }
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
            val file_name: StorageReference =
                Folder.child(binding.txtTelefono.text.toString())
            file_name.putFile(uri).addOnSuccessListener { taskSnapshot ->
                file_name.getDownloadUrl().addOnSuccessListener { uri ->
                }
            }
        } else {
            Log.d(TAG, "No media selected")
        }
    }

}