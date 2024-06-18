package com.istea.appdelclima.presentacion.clima

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.istea.appdelclima.repository.Repositorio
import com.istea.appdelclima.repository.modelos.ClimaForecast
import com.istea.appdelclima.router.Router
import com.istea.appdelclima.router.Ruta
import kotlinx.coroutines.launch

class ClimaViewModel(
    val respositorio: Repositorio,
    val router: Router,
    val lat : Float,
    val lon : Float
) : ViewModel() {

    var uiState by mutableStateOf<ClimaEstado>(ClimaEstado.Vacio)

    fun ejecutar(intencion: ClimaIntencion){
        when(intencion){
            ClimaIntencion.actualizarClima -> traerClima()
            ClimaIntencion.volver -> volverCiudades()
        }
    }

    private fun volverCiudades() {
        val ruta = Ruta.Ciudades
        router.navegar(ruta)
    }

    fun traerClima() {
        uiState = ClimaEstado.Cargando
        viewModelScope.launch {
            try{
                val (clima,pronostico)    = respositorio.traerClima(lat = lat, lon = lon)
                uiState = ClimaEstado.ClimaExitoso(
                    ciudad = clima.name ,
                    temperatura = clima.main.temp,
                    descripcion = clima.weather.first().description,
                    st = clima.main.feels_like,
                    minTemp = clima.main.temp_min,
                    maxTemp = clima.main.temp_max,
                    pronostico = pronostico.list.take(5)
                )
            } catch (exception: Exception){
                uiState = ClimaEstado.Error(exception.localizedMessage ?: "error desconocido")
            }
        }
    }
}

class ClimaViewModelFactory(
    private val repositorio: Repositorio,
    private val router: Router,
    private val lat: Float,
    private val lon: Float,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClimaViewModel::class.java)) {
            return ClimaViewModel(repositorio,router,lat,lon) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}