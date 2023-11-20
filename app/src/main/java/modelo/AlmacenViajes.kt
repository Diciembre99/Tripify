package modelo

object AlmacenViajes {
    lateinit var viajes: ArrayList<Viaje>
    private fun add(viaje: Viaje){
        viajes.add(viaje)
    }
}