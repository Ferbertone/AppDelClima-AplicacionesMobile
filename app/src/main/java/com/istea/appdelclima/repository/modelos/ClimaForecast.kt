package com.istea.appdelclima.repository.modelos

import kotlinx.serialization.Serializable


@Serializable
data class ClimaForecast(
    val cod: String,
    val message: Long,
    val cnt: Long,
    val list: List<ListForecast>,
    val city: City,
)
@Serializable
data class ListForecast(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Long,
    val pop: Double,
    val rain: Rain? = null,
    val sys: Sys,
    val dtTxt: String? = null,
)

@Serializable
data class Rain(
    val n3h: Double? = 0.0,
)
@Serializable
data class Sys(
    val pod: String = "",
)
@Serializable
data class City(
    val id: Long,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Long,
    val timezone: Long,
    val sunrise: Long,
    val sunset: Long,
)

