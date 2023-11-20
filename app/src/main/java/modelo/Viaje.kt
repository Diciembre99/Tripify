package modelo

import android.text.Editable
import java.io.Serializable

data class Viaje(var destino: Editable?, var origen: Editable?, var cliente: Editable?, var fecha:String):Serializable {
}