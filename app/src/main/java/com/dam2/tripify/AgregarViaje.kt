package com.dam2.tripify

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import auxiliar.Conexion
import com.dam2.appmovil.R
import com.dam2.appmovil.databinding.FragmentAgregarViajeBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import modelo.Viaje
import java.util.Calendar
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
        var lista = ArrayList<Viaje>()
        binding = FragmentAgregarViajeBinding.inflate(inflater, container, false)
        firebaseauth = FirebaseAuth.getInstance()
        binding.btnAgregar.setOnClickListener {
            val usuario = FirebaseAuth.getInstance().currentUser
            if (todosLosCamposLlenos()) {
                val viaje = construirViaje()
                auxiliar.Conexion.guardarViaje(context, viaje)
                limpiarCampos()
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.title))
                    .setMessage(getString(R.string.messageFieldError))
                    .setPositiveButton(getString(R.string.positive_button)) { dialog, which ->
                    }
                    .show()
            }
        }
        binding.btnFecha.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .build()

            datePicker.show(childFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener { selection ->
                val selectedDate = Date(selection)
                val currentDate = Calendar.getInstance().time

                if (selectedDate.after(currentDate)) {
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val fecha = sdf.format(selectedDate)
                    binding.fecha.setText(fecha)
                } else {
                    Toast.makeText(context, "Selecciona una fecha válida", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnHora.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText("Select Appointment time")
                    .build()

            picker.addOnPositiveButtonClickListener {
                // Aquí obtienes la hora y el minuto seleccionados
                val selectedHour = picker.hour
                val selectedMinute = picker.minute
                val formato = "$selectedHour : $selectedMinute"
                // Puedes hacer lo que necesites con la hora y el minuto seleccionados
                // Por ejemplo, imprimirlos en la consola
                binding.hora.setText( formato.toString())
            }
            picker.show(childFragmentManager, "")
        }
        return binding.root
    }

    private fun construirViaje(): Viaje {
        val destino = binding.txtDestino.text
        val origen = binding.txtOrigen.text
        val cliente = binding.txtCliente.text
        val fecha: String = binding.fecha.text.toString()
        val hora: String = binding.hora.text.toString()
        return Viaje(destino.toString(), origen.toString(), cliente.toString(), fecha, hora)
    }

    private fun limpiarCampos() {
        binding.txtCliente.text!!.clear()
        binding.txtDestino.text!!.clear()
        binding.txtOrigen.text!!.clear()
        binding.hora.text!!.clear()
        binding.fecha.text!!.clear()

    }
    private fun todosLosCamposLlenos(): Boolean {
        return !binding.txtCliente.text.isNullOrEmpty() &&
                !binding.txtDestino.text.isNullOrEmpty() &&
                !binding.txtOrigen.text.isNullOrEmpty() &&
                !binding.hora.text.isNullOrEmpty() &&
                !binding.fecha.text.isNullOrEmpty()
    }
}