package com.istea.appdelclima.presentacion.clima

sealed class ClimaIntencion {

    object actualizarClima: ClimaIntencion()

    object volver : ClimaIntencion()

}
