package com.dam2.tripify

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import auxiliar.AdaptadorRecycler
import auxiliar.AdaptadorRecyclerCliente
import com.dam2.appmovil.databinding.FragmentCarteraClientesBinding
import modelo.AlmacenCliente
import modelo.AlmacenViajes


class Cartera_clientes : Fragment() {
    private lateinit var binding: FragmentCarteraClientesBinding
    private lateinit var miRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        miRecyclerView = binding.recyclerClientes as RecyclerView
        miRecyclerView.setHasFixedSize(true)//hace que se ajuste a lo que has diseñado
        miRecyclerView.layoutManager = LinearLayoutManager(requireContext())//se dice el tipo de Layout, dejampos este.
        val miAdapter = AdaptadorRecyclerCliente(AlmacenCliente.Clientes,requireContext() )
        miRecyclerView.adapter = miAdapter
        super.onStart()
    }
    override fun onResume() {
        super.onResume()
        miRecyclerView = binding.recyclerClientes as RecyclerView
        miRecyclerView.setHasFixedSize(true)//hace que se ajuste a lo que has diseñado
        miRecyclerView.layoutManager = LinearLayoutManager(requireContext())//se dice el tipo de Layout, dejampos este.
        val miAdapter = AdaptadorRecyclerCliente(AlmacenCliente.Clientes,requireContext() )
        miRecyclerView.adapter = miAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCarteraClientesBinding.inflate(inflater, container, false)
        miRecyclerView = binding.recyclerClientes as RecyclerView
        miRecyclerView.setHasFixedSize(true)//hace que se ajuste a lo que has diseñado
        miRecyclerView.layoutManager = LinearLayoutManager(requireContext())//se dice el tipo de Layout, dejampos este.
        val miAdapter = AdaptadorRecyclerCliente(AlmacenCliente.Clientes,requireContext() )
        miRecyclerView.adapter = miAdapter

        binding.btnAgregarCliente.setOnClickListener {
            val intent = Intent(context, AgregarCliente::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}