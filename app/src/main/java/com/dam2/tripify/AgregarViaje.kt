package com.dam2.tripify

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dam2.appmovil.databinding.FragmentAgregarViajeBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import modelo.Viaje
import java.util.Date
import java.util.Locale

class AgregarViaje : Fragment() {

    val db = Firebase.firestore
    private lateinit var firebaseauth : FirebaseAuth
    private lateinit var binding: FragmentAgregarViajeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAgregarViajeBinding.inflate(inflater, container, false)
        firebaseauth = FirebaseAuth.getInstance()
        binding.btnAgregar.setOnClickListener {
            val viaje = construirViaje()
            val usuario = FirebaseAuth.getInstance().currentUser
            if (usuario != null){
                auxiliar.conexion.guardarViaje(context,viaje)
            }else{
                Toast.makeText(context,"Error en la info del usuario", Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnCalendario.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .build()
            datePicker.show(childFragmentManager,"tag")
            datePicker.addOnPositiveButtonClickListener { selection ->
                val selectedDate = Date(selection)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                var fecha = sdf.format(selectedDate)
                binding.tvFecha.text = fecha
            }
        }
        return binding.root
    }

    private fun construirViaje(): Viaje {
        val destino = binding.txtDestino.text
        val origen = binding.txtOrigen.text
        val cliente = binding.txtCliente.text
        val fecha: String = binding.tvFecha.text.toString()
        return Viaje(destino, origen, cliente, fecha)
    }
}