package com.istea.appdelclima.presentacion.clima

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.istea.appdelclima.repository.modelos.Clouds
import com.istea.appdelclima.repository.modelos.ListForecast
import com.istea.appdelclima.repository.modelos.Main
import com.istea.appdelclima.repository.modelos.Sys
import com.istea.appdelclima.repository.modelos.Weather
import com.istea.appdelclima.repository.modelos.Wind
import com.istea.appdelclima.ui.theme.AppDelClimaTheme


@Composable
fun ClimaView(
    modifier: Modifier = Modifier,
    state : ClimaEstado,
    onAction: (ClimaIntencion)->Unit
) {
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        onAction(ClimaIntencion.actualizarClima)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(state){
            is ClimaEstado.Error -> ErrorView(mensaje = state.mensaje)
            is ClimaEstado.ClimaExitoso -> ClimaView(
                ciudad = state.ciudad,
                temperatura = state.temperatura,
                descripcion = state.descripcion,
                st = state.st,
                min_temp = state.minTemp,
                max_temp = state.maxTemp,
                pronostico = state.pronostico

            )
            ClimaEstado.Vacio -> EmptyView()
            ClimaEstado.Cargando -> LoadingView()
        }
        Spacer(modifier = Modifier.height(100.dp))
        Row {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Marcar como favorito")
            }
            Button(onClick = { onAction(ClimaIntencion.volver) }) {
                Text(text = "Volver")
            }
        }
    }
}

@Composable
fun EmptyView(){
    Text(text = "No hay nada que mostrar")
}

@Composable
fun LoadingView(){
    Text(text = "Cargando")
}

@Composable
fun ErrorView(mensaje: String){
    Text(text = mensaje)
    println(mensaje)
}

@Composable
fun ClimaView(
    ciudad: String,
    temperatura: Double,
    descripcion: String,
    st:Double?,
    min_temp: Double?,
    max_temp:Double?,
    pronostico: List<ListForecast>,
    modifier: Modifier = Modifier, ){
    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text ="${ciudad}" ,
            style = MaterialTheme.typography.titleMedium)
        Text(
            text = "${temperatura}°",
            style = MaterialTheme.typography.titleLarge)
        Text(
            text = "${min_temp}/${max_temp}",
            style = MaterialTheme.typography.bodyMedium)
        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodyMedium)
        Text(
            text = "sensacionTermica: ${st}°",
            style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Pronóstico extendido:",
            style = MaterialTheme.typography.titleMedium)
        pronostico.forEach { item ->
            PronosticoItemView(item)
        }
    }
}

@Composable
fun PronosticoItemView(item: ListForecast) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(text = "Fecha: ${formatTimestamp(item.dt)}", style = MaterialTheme.typography.bodyMedium)
        Row {
            Text(
                text = "Temp: ${item.main.temp}°",
                style = MaterialTheme.typography.bodyMedium)
            Text(
                text = item.weather.firstOrNull()?.description ?: "",
                style = MaterialTheme.typography.bodyMedium)
        }
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    val date = java.util.Date(timestamp * 1000) // El timestamp viene en segundos, hay que multiplicarlo por 1000 para convertirlo a milisegundos
    return sdf.format(date)
}

@Preview(showBackground = true)
@Composable
fun ClimaPreviewVacio() {
    AppDelClimaTheme {
        ClimaView(state = ClimaEstado.Vacio, onAction = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ClimaPreviewCargando() {
    AppDelClimaTheme {
        ClimaView(state = ClimaEstado.Cargando, onAction = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ClimaPreviewError() {
    AppDelClimaTheme {
        ClimaView(state = ClimaEstado.Error("Algo salio mal"), onAction = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ClimaPreviewExitoso() {
    val pronosticoFicticio = listOf(
        ListForecast(
            dt = 1625241600,
            main = Main(
                temp = 25.0,
                feels_like = 25.0,
                temp_min = 20.0,
                temp_max = 30.0,
                pressure = 1013,
                seaLevel = 1013,
                grndLevel = 1000,
                humidity = 60,
                tempKf = 0.0
            ),
            weather = listOf(
                Weather(
                    id = 800,
                    main = "Clear",
                    description = "Despejado",
                    icon = "01d"
                )
            ),
            clouds = Clouds(all = 0),
            wind = Wind(
                speed = 5.0,
                deg = 180,
                gust = 8.0
            ),
            visibility = 10000,
            pop = 0.0,
            rain = null,
            sys = Sys(pod = "d"),
            dtTxt = "2021-07-02 12:00:00"
        ),
        ListForecast(
            dt = 1625328000,
            main = Main(
                temp = 26.0,
                feels_like = 26.0,
                temp_min = 21.0,
                temp_max = 31.0,
                pressure = 1014,
                seaLevel = 1014,
                grndLevel = 1001,
                humidity = 55,
                tempKf = 0.0
            ),
            weather = listOf(
                Weather(
                    id = 801,
                    main = "Clouds",
                    description = "Parcialmente nublado",
                    icon = "02d"
                )
            ),
            clouds = Clouds(all = 20),
            wind = Wind(
                speed = 4.0,
                deg = 170,
                gust = 7.0
            ),
            visibility = 10000,
            pop = 0.0,
            rain = null,
            sys = Sys(pod = "d"),
            dtTxt = "2021-07-03 12:00:00"
        )
    )
    AppDelClimaTheme {
        ClimaView(
            state = ClimaEstado.ClimaExitoso(
                ciudad = "mendoza",
                temperatura = 10.1,
                descripcion = "nuboso",
                st = 17.5,
                minTemp = 10.15,
                maxTemp = 15.15,
                pronostico = pronosticoFicticio
            ),
            onAction = {})
    }
}