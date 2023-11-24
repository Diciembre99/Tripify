package modelo

object AlmacenViajes {
    lateinit var viajes: ArrayList<Viaje>
    public fun add(viaje: Viaje){
        viajes.add(viaje)
    }
}