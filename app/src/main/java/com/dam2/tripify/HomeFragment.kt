import Auxiliar.AdaptadorRecycler
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dam2.appmovil.R
import com.dam2.appmovil.databinding.FragmentHomeBinding
import modelo.AlmacenViajes

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var miRecyclerView: RecyclerView
    override fun onResume() {
        miRecyclerView = binding.recyclerViajes as RecyclerView
        miRecyclerView.setHasFixedSize(true)//hace que se ajuste a lo que has diseñado
        miRecyclerView.layoutManager = LinearLayoutManager(requireContext())//se dice el tipo de Layout, dejampos este.
        val miAdapter = AdaptadorRecycler(AlmacenViajes.viajes,requireContext() )
        miRecyclerView.adapter = miAdapter
        val cabecera = AlmacenViajes.viajes.size.toString()+" Viajes"
        binding.tvViajes.text = cabecera
        super.onResume()
    }

    override fun onStart() {
        miRecyclerView = binding.recyclerViajes as RecyclerView
        miRecyclerView.setHasFixedSize(true)//hace que se ajuste a lo que has diseñado
        miRecyclerView.layoutManager = LinearLayoutManager(requireContext())//se dice el tipo de Layout, dejampos este.
        val miAdapter = AdaptadorRecycler(AlmacenViajes.viajes,requireContext() )
        miRecyclerView.adapter = miAdapter
        val cabecera = AlmacenViajes.viajes.size.toString()+" Viajes"
        binding.tvViajes.text = cabecera
        super.onStart()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        miRecyclerView = binding.recyclerViajes as RecyclerView
        miRecyclerView.setHasFixedSize(true)//hace que se ajuste a lo que has diseñado
        miRecyclerView.layoutManager = LinearLayoutManager(requireContext())//se dice el tipo de Layout, dejampos este.
        val miAdapter = AdaptadorRecycler(AlmacenViajes.viajes,requireContext() )
        miRecyclerView.adapter = miAdapter
        val cabecera = AlmacenViajes.viajes.size.toString()+ R.string.viajes
        binding.tvViajes.text = cabecera
        return binding.root
    }
}
