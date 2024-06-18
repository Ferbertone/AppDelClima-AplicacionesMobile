package com.istea.appdelclima.presentacion.clima

import com.istea.appdelclima.repository.modelos.ListForecast

sealed class ClimaEstado {

    data class ClimaExitoso (
        val ciudad: String = "",
        val temperatura: Double,
        val descripcion: String = "nuboso" ,
        val st :Double,
        val minTemp :Double,
        val maxTemp :Double,
        val pronostico : List<ListForecast>

    ) : ClimaEstado()

    data class Error(
        val mensaje :String = "",
    ) : ClimaEstado()

    data object Vacio: ClimaEstado()

    data object Cargando: ClimaEstado()

}
