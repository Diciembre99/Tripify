package com.dam2.tripify

import HomeFragment
import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdaptadorViewPage(fragments: ActivityFragments): FragmentStateAdapter(fragments) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{clientes()}
            1->{HomeFragment()}
            2->{AgregarViaje()}
            else->{throw Resources.NotFoundException("Posicion no encontrada")
            }
        }
    }

}