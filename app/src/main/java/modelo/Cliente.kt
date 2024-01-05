package modelo

import java.io.Serializable

data class Cliente (var nombre: String, var apellido: String, var numero: String, var llave: String?=null):Serializable{

}