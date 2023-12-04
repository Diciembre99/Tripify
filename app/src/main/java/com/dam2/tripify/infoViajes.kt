package com.dam2.tripify

import Auxiliar.AdaptadorRecycler
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.dam2.appmovil.R
import com.dam2.appmovil.databinding.ActivityInfoViajesBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import modelo.Almacen
import modelo.AlmacenViajes
import modelo.Viaje
import java.util.Date
import java.util.Locale

class infoViajes : AppCompatActivity() {
    lateinit var binding: ActivityInfoViajesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoViajesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var v = intent.getSerializableExtra("obj") as Viaje

        if (Almacen.usuario.rol == "pasajero"){

            binding.swEdit.isVisible = false
        }
        binding.toolbarDetalle.setTitle("Viaje a " + v.destino)
        binding.cliente.setText(v.cliente)
        binding.destino.setText(v.destino)
        binding.origen.setText(v.origen)
        binding.fecha.setText(v.fecha)
        binding.hora.setText(v.hora)


        binding.swEdit.setOnCheckedChangeListener { _, isChecked ->
            binding.cliente.isEnabled = isChecked
            binding.destino.isEnabled = isChecked
            binding.origen.isEnabled = isChecked
            binding.fecha.isEnabled = isChecked
            binding.hora.isEnabled = isChecked
            binding.btnGuardar.isVisible = isChecked
            binding.btnClock.isVisible = isChecked
            binding.btnFecha.isVisible = isChecked
        }

        binding.btnFecha.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .build()
            datePicker.show(supportFragmentManager, "tag")
            datePicker.addOnPositiveButtonClickListener { selection ->
                val selectedDate = Date(selection)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                var fecha = sdf.format(selectedDate)
                binding.fecha.setText(fecha)
            }

        }
        binding.btnClock.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText("Select Appointment time")
                    .build()

            picker.addOnPositiveButtonClickListener {
                val selectedHour = picker.hour
                val selectedMinute = picker.minute
                val formato = "$selectedHour : $selectedMinute"
                binding.hora.setText(formato)
            }
            picker.show(supportFragmentManager, "tag")
        }
        setSupportActionBar(binding.toolbarDetalle)
        binding.btnGuardar.setOnClickListener {
            val viaje = Viaje(
                binding.destino.text.toString(),
                binding.origen.text.toString(),
                binding.cliente.text.toString(),
                binding.fecha.text.toString(),
                binding.hora.text.toString(),
                v.llave
            )
            auxiliar.Conexion.modificarViajes(viaje, this)
        }

        //Menu superior
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarDetalle.setNavigationOnClickListener {
            val miAdapter = AdaptadorRecycler(AlmacenViajes.viajes,this )
            finish()
        }
    }
}