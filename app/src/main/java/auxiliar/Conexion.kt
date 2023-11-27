package auxiliar

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import modelo.Almacen
import modelo.AlmacenViajes
import modelo.Usuario
import modelo.Viaje


object Conexion {
    private lateinit var firebaseauth : FirebaseAuth
    val db = Firebase.firestore
    val viajesRef = db.collection("viajes")

    fun guardarUsuario(context: Context,email:String,name:String,password:String){
        var u:Usuario = Usuario(name, email, password)
        var user = hashMapOf(
            "email" to email,
            "name" to name,
            "password" to password,
            "roles" to arrayListOf(1, 2, 3),
            "timestamp" to FieldValue.serverTimestamp()
        )
        db.collection("users")
            .document(user.get("email").toString()) //Será la clave del documento.
            .set(user).addOnSuccessListener {
                Toast.makeText(context, "Almacenado", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
            }
    }

    fun modificarViajes(v:Viaje,context:Context){
        val db = FirebaseFirestore.getInstance()
        var viaje = hashMapOf(
            "email" to Almacen.usuario.correo.toString(),
            "destino" to v.destino.toString(),
            "origen" to v.origen.toString(),
            "cliente" to v.cliente.toString(),
            "fecha" to v.fecha.toString(),
            "hora" to v.hora.toString(),
            "timestamp" to FieldValue.serverTimestamp()
        )
        db.collection("viajes").document(v.llave.toString())
            .set(viaje)// Utiliza add() para agregar un nuevo documento con una clave autogenerada
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, "Viaje modificado tu viaje a: ${v.destino}", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
            }.addOnCompleteListener{
                Conexion.cargarViajes(Almacen.usuario.correo)
            }
    }
    fun guardarViaje(context: Context?, v: Viaje) {
        var viaje = hashMapOf(
            "email" to Almacen.usuario.correo.toString(),
            "destino" to v.destino.toString(),
            "origen" to v.origen.toString(),
            "cliente" to v.cliente.toString(),
            "fecha" to v.fecha.toString(),
            "hora" to v.hora.toString(),
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("viajes")
            .add(viaje) // Utiliza add() para agregar un nuevo documento con una clave autogenerada
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, "Viaje añadido tu viaje a: ${v.destino}", Toast.LENGTH_SHORT).show()
                v.llave = documentReference.id
            }
            .addOnFailureListener {
                Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
            }.addOnCompleteListener{
                Conexion.cargarViajes(Almacen.usuario.correo)
            }
    }
    fun eliminarViaje(context: Context?,codigo: String){
        val db = FirebaseFirestore.getInstance()
        // Obtener la referencia del documento usando la clave
        val referenciaDocumento = db.collection("viajes").document(codigo)
        // Eliminar el documento
        referenciaDocumento.delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Viaje eliminado con exito", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "Documento eliminado correctamente")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error al eliminar documento", e)
            }
    }
    fun cargarViajes(email: String) {
        val db = FirebaseFirestore.getInstance()
        val arrayList = ArrayList<Viaje>()
        AlmacenViajes.viajes = ArrayList()
        db.collection("viajes")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener {
                for (document in it){
                    AlmacenViajes.viajes.add(Viaje(document.get("destino").toString(),document.get("origen").toString(),
                        document.get("cliente").toString(),document.get("fecha").toString(),document.get("hora").toString(),document.id))
                }
            }.addOnCompleteListener{
                Log.d("KRCC", AlmacenViajes.viajes.toString())
            }.addOnFailureListener{
                Log.d("KRCC", "Error al cargar")
            }.addOnCanceledListener {
                Log.d("KRCC", "Cancel")
            }
    }
}