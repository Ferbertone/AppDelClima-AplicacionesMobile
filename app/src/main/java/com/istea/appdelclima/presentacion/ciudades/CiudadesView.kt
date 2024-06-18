package com.istea.appdelclima.presentacion.ciudades

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.istea.appdelclima.presentacion.clima.ClimaIntencion
import com.istea.appdelclima.repository.modelos.Ciudad
import com.istea.appdelclima.ui.theme.AppDelClimaTheme

@Composable
fun CiudadesView (
    modifier: Modifier = Modifier,
    state : CiudadesEstado,
    onAction: (CiudadesIntencion)->Unit
) {
    var value by remember{ mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = value,
            label = {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Buscar por nombre"
                )
            },
            onValueChange = {
                value = it
                onAction(CiudadesIntencion.Buscar(value))
            },
            modifier = modifier.padding(vertical = 10.dp)
        )
        when(state) {
            CiudadesEstado.Cargando -> Text(text = "Cargando...")
            is CiudadesEstado.Error -> Text(
                text = state.mensaje,
                color = MaterialTheme.colorScheme.error,
                )
            is CiudadesEstado.Resultado -> ListaDeCiudades(state.ciudades) {
                onAction(
                    CiudadesIntencion.Seleccionar(it)
                )
            }
            CiudadesEstado.Vacio -> Text(
                text = "No hay resultados",
                color = MaterialTheme.colorScheme.error,)
        }
        Button(onClick = { /*  TODO*/ }) {
            Text(text = "Ver Clima Favorito")
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaDeCiudades(ciudades: List<Ciudad>, onSelect: (Ciudad)->Unit) {
    LazyColumn (horizontalAlignment = Alignment.CenterHorizontally,) {
        items(items = ciudades) {
            Card(
                onClick = { onSelect(it) },
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                ) {
                Text(
                    text = it.name,
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black,
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ciudadPreviewResulatado(){
    val ciudades = listOf(
        Ciudad(name = "Córdoba", lat = -31.4194f, lon = -64.1860f, country = "Argentina"),
        Ciudad(name = "Buenos Aires", lat = -34.6037f, lon = -58.3816f, country = "Argentina"),
        Ciudad(name = "La Plata", lat = -34.9215f, lon = -57.9545f, country = "Argentina")
    )
    AppDelClimaTheme {
        CiudadesView(state = CiudadesEstado.Resultado(ciudades) ) {
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ciudadPreviewCargando(){
    AppDelClimaTheme {
        CiudadesView(state = CiudadesEstado.Cargando) {
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ciudadPreviewVacio(){
    AppDelClimaTheme {
        CiudadesView(state = CiudadesEstado.Vacio) {
        }
    }
}


