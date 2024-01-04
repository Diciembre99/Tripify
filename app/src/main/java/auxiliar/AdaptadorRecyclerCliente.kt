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
import modelo.Cliente
import java.io.File

class AdaptadorRecyclerCliente(var clientes:ArrayList<Cliente>, var context: Context) : RecyclerView.Adapter<AdaptadorRecyclerCliente.ViewHolder>() {

    companion object {
        var seleccionado:Int = -1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = clientes.get(position)
        holder.bind(item, context, position, this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val vista = LayoutInflater.from(parent.context).inflate(R.layout.cardview_clientes, parent, false)
        val viewHolder = ViewHolder(vista)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context, infoViajes::class.java)
            context.startActivity(intent)
        }
        return viewHolder
    }


    override fun getItemCount(): Int {
        return clientes.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var storage = Firebase.storage
        var storageRef = storage.reference
        val nombre = view.findViewById(R.id.tvNombre) as TextView
        val apellido = view.findViewById(R.id.tvApellido) as TextView
        val telefono = view.findViewById(R.id.tvTelefono) as TextView
        val foto = view.findViewById(R.id.imgCliente) as ImageView
        val card = view.findViewById(R.id.cardCliente) as CardView

        @SuppressLint("ResourceAsColor")
        fun bind(c: Cliente, context: Context, pos: Int, miAdaptadorRecycler: AdaptadorRecyclerCliente){
            nombre.text = c.nombre
            apellido.text = c.apellido
            telefono.text = c.numero
            var nomImagen = c.numero
            //var spaceRef = storageRef.child("images/saturno.webp")
            var spaceRef = storageRef.child("cliente/$nomImagen")
            val localfile = File.createTempFile("tempImage", "jpg")
            spaceRef.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                foto.setImageBitmap(bitmap)
            }.addOnFailureListener {
            }
            if (pos == seleccionado) {
                with(nombre) {
                    this.setTextColor(resources.getColor(R.color.red))
                }
            }
            else {
                with(nombre) {
                    this.setTextColor(resources.getColor(R.color.black))
                }
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
                        Log.d("KRCC", "$AlmacenCliente.Clientes[pos].llave.toString()")
                        Conexion.eliminarCliente(context, AlmacenCliente.Clientes[pos].llave.toString())
                        AlmacenCliente.Clientes.removeAt(pos)
                        miAdaptadorRecycler.notifyDataSetChanged()
                    }
                    .setNegativeButton("Cancelar") { dialog, which ->
                        // No es necesario hacer nada aquí si se ha cancelado
                    }
                    .show()

                true // Devuelve true para indicar que has manejado el evento de clic largo
            }
        }
    }
    fun fileDownload() {


    }
}