package modelo

import java.io.Serializable

data class Viaje(var destino:String, var origen:String, var cliente:String,var fecha:String):Serializable {
}