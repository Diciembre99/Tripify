package com.dam2.tripify

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import auxiliar.ConexionSQLite
import com.dam2.appmovil.R
import com.dam2.appmovil.databinding.FragmentAgregarViajeBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import modelo.AlmacenCliente
import modelo.Lugares
import modelo.Viaje
import java.util.Calendar
import java.util.Locale

class AgregarViaje : Fragment() {

    val db = Firebase.firestore
    lateinit var seleccion:String
    private lateinit var firebaseauth : FirebaseAuth
    private lateinit var binding: FragmentAgregarViajeBinding
    private var notificacion = FirebaseFirestore.getInstance()
    private val dbInstance = FirebaseFirestore.getInstance()
    override fun onResume() {
        super.onResume()
        val opciones = ArrayList<String>()
        for (c in AlmacenCliente.Clientes){
            opciones.add(c.nombre + " " + c.apellido)
        }
        val materialSpinner: MaterialAutoCompleteTextView = binding.materialSpinner
        // Crea un ArrayAdapter utilizando el array de opciones y el diseño predeterminado
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, opciones)
        // Especifica el diseño a utilizar cuando se despliegan las opciones
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Establece el adaptador en el MaterialSpinner
        materialSpinner.setAdapter(adapter)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var lista = ArrayList<Viaje>()
        binding = FragmentAgregarViajeBinding.inflate(inflater, container, false)
        firebaseauth = FirebaseAuth.getInstance()

        //Spinners
        val opciones = ArrayList<String>()
        for (c in AlmacenCliente.Clientes){
            opciones.add(c.nombre + " " + c.apellido)
        }
        Log.d("KRCC","Lista de nombres: " + opciones)
        val materialSpinner: MaterialAutoCompleteTextView = binding.materialSpinner
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, opciones)
        // Estilo de los items del spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        materialSpinner.setAdapter(adapter)
        materialSpinner.setOnItemClickListener { parent, _, position, _ ->
            seleccion = parent.getItemAtPosition(position).toString()
            Toast.makeText(context, "Seleccionado: $seleccion", Toast.LENGTH_SHORT).show()
        }

        //Spinner lugares
        for (lugar in Lugares.lugares){
            println(lugar)
            Log.d("KRCC", "lugar: $lugar")
        }
        val spinnerLugar: MaterialAutoCompleteTextView = binding.spinnerDestino
        val spinnerOrigen: MaterialAutoCompleteTextView = binding.spinnerOrigen
        val adapterLugar = ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, Lugares.lugares)
        adapterLugar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLugar.setAdapter(adapterLugar)
        spinnerOrigen.setAdapter(adapterLugar)
        binding.btnAgregar.setOnClickListener {
            val usuario = FirebaseAuth.getInstance().currentUser
            if (todosLosCamposLlenos()) {
                val viaje = construirViaje()
                auxiliar.Conexion.guardarViaje(context, viaje)
                var asunto = "Tu proximo viaje a ${viaje.destino}"
                var contenido = "Viaje a ${viaje.destino}"+"\nDia: "+viaje.fecha+"\nHora: "+viaje.hora
                ConexionSQLite.agregarLugar(requireActivity() as AppCompatActivity,viaje.destino, viaje.fecha)
                ConexionSQLite.agregarLugar(requireActivity() as AppCompatActivity,viaje.origen, viaje.fecha)
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
                val selectedCalendar = Calendar.getInstance().apply {
                    timeInMillis = selection
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val currentCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                if (!selectedCalendar.before(currentCalendar) || selectedCalendar == currentCalendar) {
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val fecha = sdf.format(selectedCalendar.time)
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
        val destino = binding.spinnerDestino.text
        val origen = binding.spinnerOrigen.text
        val cliente = seleccion
        val fecha: String = binding.fecha.text.toString()
        val hora: String = binding.hora.text.toString()
        return Viaje(destino.toString(), origen.toString(), cliente.toString(), fecha, hora)
    }

    private fun limpiarCampos() {
        binding.spinnerDestino.text!!.clear()
        binding.spinnerOrigen.text!!.clear()
        binding.hora.text!!.clear()
        binding.fecha.text!!.clear()
        binding.materialSpinner.text!!.clear()

    }
    private fun todosLosCamposLlenos(): Boolean {
        return !binding.txtCliente.isEmpty() &&
                !binding.spinnerDestino.text.isNullOrEmpty() &&
                !binding.spinnerOrigen.text.isNullOrEmpty() &&
                !binding.hora.text.isNullOrEmpty() &&
                !binding.fecha.text.isNullOrEmpty()
    }

}