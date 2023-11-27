package com.dam2.tripify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dam2.appmovil.R
import com.dam2.appmovil.databinding.FragmentClientesBinding
import com.dam2.appmovil.databinding.FragmentHomeBinding
import modelo.Almacen

class clientes : Fragment() {
    lateinit var binding: FragmentClientesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClientesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val nombre = Almacen.usuario.nombre.toString()
        val correo = Almacen.usuario.correo.toString()
        binding.tvNombre.text = nombre
        binding.tvCorreo.text = correo
        return binding.root
    }
}