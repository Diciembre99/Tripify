package com.dam2.tripify

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dam2.appmovil.databinding.FragmentClientesBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import modelo.Almacen
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

class clientes : Fragment() {
    val TAG = "KRCC"
    var storage = Firebase.storage
    lateinit var binding: FragmentClientesBinding
    var storageRef = storage.reference
    private val cameraRequest = 1888
    private lateinit var bitmap: Bitmap

    //Segunda activity para lanzar la cámara.
    val openCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data!!
                this.bitmap = data.extras!!.get("data") as Bitmap
                binding.imgUser.setImageBitmap(bitmap)
                if (bitmap != null) {
                    Log.d(TAG, "Cargada")
                    val Folder: StorageReference =
                        FirebaseStorage.getInstance().getReference().child("user")
                    var bitArray = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bitArray)
//            val file_name: StorageReference = Folder.child("" + uri!!.lastPathSegment) //<-- Podemos coger el último segmento de toda la ruta como nombre.
                    val file_name: StorageReference =
                        Folder.child(Almacen.usuario.correo)  //<-- Podemos poner el nombre que queramos. lo leo de la caja de texto.
                    file_name.putBytes(bitArray.toByteArray())
                        .addOnSuccessListener { taskSnapshot ->
                            file_name.getDownloadUrl().addOnSuccessListener { uri ->
                            }
                        }
                } else {
                    Log.d(TAG, "No media selected")
                }
            }

        }

    //Activity para pedir permisos de cámara.
    val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                //si quisieras vídeo. Poner el punto y ver resto de opciones que ofrece, que prueben alguna.
                //val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                openCamera.launch(intent)
            } else {
                Log.e(TAG, "Permiso de cámara no concedido")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fileDownload()
        binding = FragmentClientesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        Log.e(TAG, Almacen.usuario.nombre)
        Log.e(TAG, Almacen.usuario.correo)

        val nombre = Almacen.usuario.nombre.toString()
        val correo = Almacen.usuario.correo.toString()
        binding.tvNombre.text = nombre
        binding.tvCorreo.text = correo
        binding.btnCambiarFoto.setOnClickListener {
            fileUpload()
        }
        binding.btnAbrirCamara.setOnClickListener {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        return binding.root
    }

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d(TAG, "Selected URI: $uri")
            binding.imgUser.setImageURI(uri)
            Log.d(TAG, "Cargada")
            val Folder: StorageReference =
                FirebaseStorage.getInstance().getReference().child("user")
//            val file_name: StorageReference = Folder.child("" + uri!!.lastPathSegment) //<-- Podemos coger el último segmento de toda la ruta como nombre.
            val file_name: StorageReference =
                Folder.child(Almacen.usuario.correo)  //<-- Podemos poner el nombre que queramos. lo leo de la caja de texto.
            file_name.putFile(uri).addOnSuccessListener { taskSnapshot ->
                file_name.getDownloadUrl().addOnSuccessListener { uri ->
                }
            }
        } else {
            Log.d(TAG, "No media selected")
        }
    }

    /**
     * Método que descarga el fichero usando un archivo temporal.
     */
    fun fileDownload() {
        var nomImagen = Almacen.usuario.correo.toString()
        //var spaceRef = storageRef.child("images/saturno.webp")
        var spaceRef = storageRef.child("user/$nomImagen")

        val localfile = File.createTempFile("tempImage", "jpg")
        spaceRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imgUser.setImageBitmap(bitmap)
        }.addOnFailureListener {
        }

    }


    fun fileUpload() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}