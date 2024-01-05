package auxiliar

import android.content.ContentValues
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import modelo.Almacen

object ConexionSQLite {
    private  var DATABASE_NAME = "lugares.db3"
    private  var DATABASE_VERSION = 1

    fun agregarLugar(contexto: AppCompatActivity, lugar:String, fecha:String):Long{
        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.writableDatabase //habilito la BBDD para escribir en ella, tambié deja leer.
        val registro = ContentValues() //objeto de kotlin, contenido de valores, un Map. Si haceis ctrl+clic lo veis.
        val correo = Almacen.usuario.correo
        registro.put("lugar",lugar)
        registro.put("fecha", fecha)
        val codigo = bd.insert("lugares", null, registro)
        bd.close()
        Log.i("KRCC", "Lugar agregado $lugar")
        return codigo
    }

    fun obtenerLugares(contexto: AppCompatActivity):ArrayList<String>{
        var lugares:ArrayList<String> = ArrayList(1)

        val admin = AdminSQLIteConexion(contexto, this.DATABASE_NAME, null, DATABASE_VERSION)
        val bd = admin.readableDatabase
        val fila = bd.rawQuery("select lugar from lugares", null)
        while (fila.moveToNext()) {
            var l:String = (fila.getString(0))
            lugares.add(l)
        }
        bd.close()
        return lugares //este arrayList lo puedo poner en un adapter de un RecyclerView por ejemplo.
    }
}