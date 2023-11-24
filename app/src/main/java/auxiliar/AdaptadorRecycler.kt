package auxiliar

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dam2.appmovil.R
import modelo.AlmacenViajes
import modelo.Viaje

class AdaptadorRecycler {
    class AdaptadorRecycler(var viajes:ArrayList<Viaje>, var context: Context) : RecyclerView.Adapter<AdaptadorRecycler.ViewHolder>() {

        companion object {
            //Esta variable estática nos será muy útil para saber cual está marcado o no.
            var seleccionado: Int = -1
            /*
            PAra marcar o desmarcar un elemento de la lista lo haremos diferente a una listView. En la listView el listener
            está en la activity por lo que podemos controlar desde fuera el valor de seleccionado y pasarlo al adapter, asociamos
            el adapter a la listview y resuelto.
            En las RecyclerView usamos para pintar cada elemento la función bind (ver código más abajo, en la clase ViewHolder).
            Esto se carga una vez, solo una vez, de ahí la eficiencia de las RecyclerView. Si queremos que el click que hagamos
            se vea reflejado debemos recargar la lista, para ello forzamos la recarga con el método: notifyDataSetChanged().
             */
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = viajes.get(position)
            holder.bind(item, context, position, this)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val layoutInflater = LayoutInflater.from(parent.context)
//        //return ViewHolder(layoutInflater.inflate(R.layout.item_lo,parent,false))
//        return ViewHolder(layoutInflater.inflate(R.layout.item_card,parent,false))

            //Este método infla cada una de las CardView

            val vista =
                LayoutInflater.from(parent.context).inflate(R.layout.cardview_viajes, parent, false)
            val viewHolder = ViewHolder(vista)
            // Configurar el OnClickListener para pasar a la segunda ventana.
//            viewHolder.itemView.setOnClickListener {
//                val intent = Intent(context, infoCubos::class.java)
//                context.startActivity(intent)
//            }

            return viewHolder
        }


        override fun getItemCount(): Int {
            //del array list que se pasa, el size, así sabe los elementos a pintar.
            return viajes.size
        }


        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val destino = view.findViewById(R.id.tvDestino) as TextView
            val cliente = view.findViewById(R.id.tvCliente) as TextView
            val fecha = view.findViewById(R.id.tvHora) as TextView

            /**
             * Éste método se llama desde el método onBindViewHolder de la clase contenedora. Como no vuelve a crear un objeto
             * sino que usa el ya creado en onCreateViewHolder, las asociaciones findViewById no vuelven a hacerse y es más eficiente.
             */
            @SuppressLint("ResourceAsColor")
            fun bind(
                viajes: Viaje,
                context: Context,
                pos: Int,
                miAdaptadorRecycler: AdaptadorRecycler
            ) {
                destino.text = viajes.destino
                cliente.text = viajes.cliente
                fecha.text = viajes.fecha

                if (pos == AdaptadorRecycler.seleccionado) {
                    with(destino) {
                        this.setTextColor(resources.getColor(R.color.black))
                    }
                    cliente.setTextColor(R.color.white)
                } else {
                    with(destino) {
                        this.setTextColor(resources.getColor(R.color.black))
                    }
                    cliente.setTextColor(R.color.black)
                }

//            itemView.setOnLongClickListener(View.OnLongClickListener() {
//                Log.e("ACSC0","long click")
//            }

                //Se levanta una escucha para cada item. Si pulsamos el seleccionado pondremos la selección a -1, (deselecciona)
                // en otro caso será el nuevo sleccionado.
                itemView.setOnClickListener {
                    if (pos == AdaptadorRecycler.seleccionado) {
                        AdaptadorRecycler.seleccionado = -1
                    } else {
                        AdaptadorRecycler.seleccionado = pos
                    }
                    //Con la siguiente instrucción forzamos a recargar el viewHolder porque han cambiado los datos. Así pintará al seleccionado.

                    miAdaptadorRecycler.notifyDataSetChanged()

//                val intent = Intent(context, MainActivity2::class.java)
//
//                context.startActivity(intent)

                    Toast.makeText(
                        context,
                        "Valor seleccionado " + AdaptadorRecycler.seleccionado.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }
                itemView.setOnLongClickListener(View.OnLongClickListener {
                    AlmacenViajes.viajes.removeAt(pos)
                    miAdaptadorRecycler.notifyDataSetChanged()
                    true //Tenemos que devolver un valor boolean.
                })

            }
        }
    }
}