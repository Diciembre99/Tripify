package auxiliar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dam2.appmovil.R
import com.dam2.tripify.infoViajes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import modelo.Almacen
import modelo.AlmacenCliente
import modelo.AlmacenViajes
import modelo.Viaje
import java.io.File
import kotlin.math.log

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
        var storage = Firebase.storage
        var storageRef = storage.reference
        val destino = view.findViewById(R.id.tvNombre) as TextView
        val cliente = view.findViewById(R.id.tvApellido) as TextView
        val hora = view.findViewById(R.id.tvTelefono) as TextView
        val card = view.findViewById(R.id.card) as CardView
        val img = view.findViewById(R.id.imgCliente) as ImageView
        @SuppressLint("ResourceAsColor")
        fun bind(v: Viaje, context: Context, pos: Int, miAdaptadorRecycler: AdaptadorRecycler){
            destino.text = v.destino
            hora.text = v.hora
            cliente.text = v.cliente
            //Extrar los datos para buscarlos en la lista de clientes y asignar una foto
            val apellidos = cliente.text.toString().substringAfter(" ")
            var nomImagen = apellidos
            Log.d("KRCC","$apellidos")
            //var spaceRef = storageRef.child("images/saturno.webp")
            var spaceRef = storageRef.child("cliente/${Almacen.usuario.correo}/$nomImagen")
            val localfile = File.createTempFile("tempImage", "jpg")
            spaceRef.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                img.setImageBitmap(bitmap)
            }.addOnFailureListener {
            }
            if (pos == seleccionado) {
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
                if (pos == seleccionado){
                    seleccionado = -1
                }
                else {
                    seleccionado = pos
                }
                miAdaptadorRecycler.notifyDataSetChanged()
            }
            itemView.setOnLongClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle("Aviso")
                    .setMessage("¿Seguro que quieres eliminar este viaje?")
                    .setPositiveButton("Aceptar") { dialog, which ->
                        Conexion.eliminarViaje(context, AlmacenViajes.viajes[pos].llave.toString())
                        AlmacenViajes.viajes.removeAt(pos)
                        miAdaptadorRecycler.notifyDataSetChanged()
                    }
                    .setNegativeButton("Cancelar") { dialog, which ->
                        // No es necesario hacer nada aquí si se ha cancelado
                    }
                    .show()

                true // Devuelve true para indicar que has manejado el evento de clic largo
            }
            card.setOnClickListener {
                var inte : Intent = Intent(context, infoViajes::class.java)
                inte.putExtra("obj",v)
                ContextCompat.startActivity(context, inte, null)
            }
        }
    }
}