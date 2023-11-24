package auxiliar

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import modelo.Almacen
import modelo.AlmacenViajes
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
    fun guardarViaje(context: Context?, v: Viaje) {
        var viaje = hashMapOf(
            "email" to Almacen.usuario.correo.toString(),
            "destino" to v.destino.toString(),
            "origen" to v.origen.toString(),
            "cliente" to v.cliente.toString(),
            "fecha" to v.fecha.toString(),
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("viajes")
            .add(viaje) // Utiliza add() para agregar un nuevo documento con una clave autogenerada
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, "Viaje añadido tu viaje a: ${v.origen}", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
            }
    }
    fun cargarViajes(context: Context, email: String){
            var al = ArrayList<String>()
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val querySnapshot = db.collection("viajes")
                        .whereEqualTo("email",email)
                        .get()
                        .addOnSuccessListener{ficheros ->
                            AlmacenViajes.viajes.clear()
                            for (f in ficheros){
                                val v = Viajes(
                                    f.getString("destino").toString(),
                                    f.getString("origen").toString(),
                                    f.getString("cliente").toString(),
                                    f.getString("fecha").toString(),
                                )

                            }
                        }
                    }

                    // Realiza acciones en el hilo principal
                    launch(Dispatchers.Main) {
                        // Procesa los resultados aquí

                        Log.e("KRCC::", "Esto está en el hilo principal rellenado después de la corrutina: ${al.toString()}")
                    }
                } catch (e: Exception) {
                    // Maneja errores aquí
                    e.printStackTrace()
                }
            }
        Log.e("KRCC::", "Aunque el código va después de la llamada el ArrayList está vacío: ${al.toString()}")
    }
}