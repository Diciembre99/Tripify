package modelo

data class Usuario (val nombre: String, val correo: String, val password: String?,val provider:String, val viajes:ArrayList<Viaje>?,val rol:Int?) {
    constructor(nombre: String, correo: String, password: String,provider:String) : this(nombre, correo, password,provider,null,null)
    constructor(nombre: String, correo: String,provider: String):this(nombre, correo,null,provider,null,null)
}