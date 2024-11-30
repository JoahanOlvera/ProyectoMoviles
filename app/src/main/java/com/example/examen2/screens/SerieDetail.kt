package com.example.examen2.screens

import android.content.Intent
import android.content.Intent.createChooser
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.examen2.modelos.Show

@Composable
fun SerieDetail(
    modifier: Modifier = Modifier,
    viewModel: ShowsViewModel = viewModel(factory = ShowsViewModel.Factory),
    idSerie: Int,
    onBack: () -> Unit
) {
    val uiState = viewModel.uiState.value // Observar el estado del ViewModel
    var favorito by remember {mutableStateOf(false)}
    var icono by remember {mutableStateOf(Icons.Filled.FavoriteBorder)}

    // Llamar al método para obtener la serie por ID al inicializar la pantalla
    LaunchedEffect(idSerie) {
        viewModel.getShowById(idSerie)
        viewModel.isFavoriteShow(idSerie) { result ->
        favorito = result
        icono = if (result) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
        }
    }

    val serie = uiState.selectedShow // Obtener la serie seleccionada desde el estado

    val context = LocalContext.current
    val shareText = "Link compartido: ${serie?.officialSite ?: "Sin enlace"}"

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = "Detalles", onBack = onBack) }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.isLoading) {
                // Mostrar un indicador de carga si los datos están cargando
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Cargando...", style = MaterialTheme.typography.bodyMedium)
                }
            } else if (serie != null) {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 8.dp)
                ) {
                    SerieDetalles(
                        modifier = Modifier.padding(top = 16.dp),
                        serie = serie
                    )
                }

                FloatingActionButton(
                    onClick = {
                        val favoriteShow = uiState.selectedShow?.toFavoriteShow()
                        if (favoriteShow != null) {
                            if (favorito) {
                                viewModel.deleteFavoriteShow(favoriteShow)
                            } else {
                                viewModel.addFavoriteShow(favoriteShow)
                            }
                            favorito = !favorito
                            icono = if (favorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 80.dp, bottom = 75.dp)
                ) {

                    Icon(imageVector = icono, contentDescription = "Favourites")

                }

                FloatingActionButton(
                    onClick = {
                        context.startActivity(createChooser(shareIntent, null))
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 75.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                }
            } else if (uiState.error != null) {
                // Mostrar mensaje de error si algo falló
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${uiState.error}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun SerieDetalles(
    modifier: Modifier = Modifier,
    serie: Show?,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()  // Ajustamos al ancho, no a toda la pantalla.
            .padding(vertical = 8.dp),
    ) {
        // Fila con imagen y detalles de la serie
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = serie?.image?.medium,
                contentDescription = "",
                modifier = Modifier
                    .size(180.dp)
                    .padding(horizontal = 4.dp)
            )
            // Columna con detalles de la serie
            Column(
                modifier = Modifier
                    .weight(1f) // Ajustar el tamaño relativo
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = serie?.name ?: "",
                    fontSize = 25.sp
                )
                Text(
                    text = "Genres: " + (serie?.genres?.joinToString(", ") ?: ""),
                    fontSize = 18.sp
                )
                Text(
                    text = "Premiered: " + serie?.premiered,
                    fontSize = 18.sp
                )
                Text(
                    text = "Country: " + serie?.network?.country?.name + "," + serie?.network?.country?.code,
                    fontSize = 18.sp
                )
                Text(
                    text = "Language: " + serie?.language,
                    fontSize = 15.sp
                )
            }
        }

        // Título para el resumen
        Text(
            text = "Summary",
            fontSize = 25.sp
        )

        // Texto de resumen de prueba
//        val summaryText = "This is a test summary to check if it's displayed."
        val summaryText = serie?.summary?.let {
            // Convertir HTML a texto legible
            HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        } ?: "Sin resumen disponible"
        // Texto del resumen
        Text(
            text = summaryText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth() // Asegura que el texto ocupe todo el ancho disponible
                .padding(vertical = 4.dp),
            fontSize = 15.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBack: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.headlineMedium) },
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    )
}