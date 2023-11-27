package Auxiliar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import auxiliar.Conexion
import com.dam2.appmovil.R
import com.dam2.tripify.infoViajes
import modelo.AlmacenViajes
import modelo.Viaje

class AdaptadorRecycler(var viajes:ArrayList<Viaje>, var context: Context) : RecyclerView.Adapter<AdaptadorRecycler.ViewHolder>() {

    companion object {
        var seleccionado:Int = -1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = viajes.get(position)
        holder.bind(item, context, position, this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val vista = LayoutInflater.from(parent.context).inflate(R.layout.cardview_viajes, parent, false)
        val viewHolder = ViewHolder(vista)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context, infoViajes::class.java)
            context.startActivity(intent)
        }
        return viewHolder
    }


    override fun getItemCount(): Int {
        return viajes.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val destino = view.findViewById(R.id.tvDestino) as TextView
        val cliente = view.findViewById(R.id.tvCliente) as TextView
        val hora = view.findViewById(R.id.tvHora) as TextView
        val card = view.findViewById(R.id.card) as CardView

        @SuppressLint("ResourceAsColor")
        fun bind(v: Viaje, context: Context, pos: Int, miAdaptadorRecycler: AdaptadorRecycler){
            destino.text = v.destino
            cliente.text = v.cliente
            hora.text = v.hora
            if (pos == AdaptadorRecycler.seleccionado) {
                with(destino) {
                    this.setTextColor(resources.getColor(R.color.red))
                }
                cliente.setTextColor(R.color.red)
            }
            else {
                with(destino) {
                    this.setTextColor(resources.getColor(R.color.black))
                }
                cliente.setTextColor(R.color.black)
            }

            itemView.setOnClickListener {
                if (pos == AdaptadorRecycler.seleccionado){
                    AdaptadorRecycler.seleccionado = -1
                }
                else {
                    AdaptadorRecycler.seleccionado = pos
                }
                miAdaptadorRecycler.notifyDataSetChanged()
            }
            itemView.setOnLongClickListener(View.OnLongClickListener {
                Conexion.eliminarViaje(context,AlmacenViajes.viajes[pos].llave.toString())
                AlmacenViajes.viajes.removeAt(pos)
                miAdaptadorRecycler.notifyDataSetChanged()
                true
            })
            card.setOnClickListener {
                var inte : Intent = Intent(context, infoViajes::class.java)
                inte.putExtra("obj",v)
                ContextCompat.startActivity(context, inte, null)
            }
        }
    }
}