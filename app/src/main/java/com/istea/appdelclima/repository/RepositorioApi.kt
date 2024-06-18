package com.istea.appdelclima.repository

import com.istea.appdelclima.repository.modelos.Ciudad
import com.istea.appdelclima.repository.modelos.Clima
import com.istea.appdelclima.repository.modelos.ClimaForecast
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class RepositorioApi : Repositorio {

    private val apiKey = "b50ef9bd8cd77b180199eb8bff27a736"

    private val cliente = HttpClient(){
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override suspend fun buscarCiudad(ciudad: String): List<Ciudad> {
        val respuesta = cliente.get("https://api.openweathermap.org/geo/1.0/direct"){
            parameter("q",ciudad)
            parameter("limit",100)
            parameter("appid",apiKey)
        }

        if (respuesta.status == HttpStatusCode.OK){
            val ciudades = respuesta.body<List<Ciudad>>()
            return ciudades
        }else{
            throw Exception()
        }
    }

    override suspend fun traerClima(lat: Float, lon: Float): Pair<Clima,ClimaForecast>  {
        val respuestaClima = cliente.get("https://api.openweathermap.org/data/2.5/weather"){
            parameter("lat",lat)
            parameter("lon",lon)
            parameter("units","metric")
            parameter("lang","es")
            parameter("appid",apiKey)
        }
        val respuestaPronostico = cliente.get("https://api.openweathermap.org/data/2.5/forecast"){
            parameter("lat",lat)
            parameter("lon",lon)
            parameter("units","metric")
            parameter("lang","es")
            parameter("appid",apiKey)
        }
        if (respuestaClima.status == HttpStatusCode.OK && respuestaPronostico.status == HttpStatusCode.OK) {
            val clima = respuestaClima.body<Clima>()
            val pronostico = respuestaPronostico.body<ClimaForecast>()
            return Pair(clima, pronostico)
        }else{
            throw Exception()
        }
    }

    override suspend fun traerPronostico(lat: Float, lon: Float): ClimaForecast {
        val respuesta = cliente.get("https://api.openweathermap.org/data/2.5/forecast"){
            parameter("lat",lat)
            parameter("lon",lon)
            parameter("units","metric")
            parameter("lang","es")
            parameter("appid",apiKey)
        }
        if (respuesta.status == HttpStatusCode.OK){
            val ClimaForecast = respuesta.body<ClimaForecast>()
            return ClimaForecast
        }else{
            throw Exception()
        }
    }
}