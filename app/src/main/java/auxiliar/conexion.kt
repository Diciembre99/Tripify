package auxiliar

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import modelo.Almacen
import modelo.Usuario
import modelo.Viaje

object conexion {
    private lateinit var firebaseauth : FirebaseAuth
    val db = Firebase.firestore

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
    fun guardarViaje(context: Context?, v: Viaje){
        var viaje = hashMapOf(
            "email" to Almacen.usuario.correo.toString(),
            "destino" to v.destino.toString(),
            "origen" to v.origen.toString(),
            "cliente" to v.cliente.toString(),
            "fecha" to v.fecha.toString(),
            "roles" to arrayListOf(1, 2, 3),
            "timestamp" to FieldValue.serverTimestamp()
        )
        db.collection("viajes")
            .document(viaje.get("email").toString()) //Será la clave del documento.
            .set(viaje).addOnSuccessListener {
                Toast.makeText(context,"Almacenado", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
            }
    }
}