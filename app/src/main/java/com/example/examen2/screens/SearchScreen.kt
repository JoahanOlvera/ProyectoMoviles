package com.example.examen2.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.examen2.modelos.Show

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: ShowsViewModel = viewModel(factory = ShowsViewModel.Factory),
    onHomeClick: () -> Unit,
    onSerieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onFavouritesClick: () -> Unit
) {
    val query by viewModel.query.collectAsState() // Recupera la consulta actual
    val filteredSeries by viewModel.filteredSeries.collectAsState() // Recupera las series filtradas

    Scaffold(
        modifier = modifier,
        topBar = {
            Box(
                Modifier
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .fillMaxWidth()
            ) {
                SearchBar(
                    query = query,
                    onQueryChange = {
                        viewModel.searchSeries(it) // Actualiza la búsqueda en tiempo real
                    }
                )
            }
        },
        bottomBar = {
            BottomBar2(
                onHomeClick = onHomeClick,
                onSearchClick = onSearchClick,
                onFavouritesClick = onFavouritesClick,
                modifier = Modifier
                    .padding(WindowInsets.navigationBars.asPaddingValues())
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            PantallaDeResultadosFiltrados(
                modifier = Modifier.fillMaxWidth(),
                seriesList = filteredSeries, // Muestra las series filtradas
                onSerieClick = onSerieClick
            )
        }
    }
}

@Composable
fun PantallaDeResultadosFiltrados(
    modifier: Modifier = Modifier,
    seriesList: List<Show>?,
    onSerieClick: (Int) -> Unit
) {
    LazyColumn(modifier = modifier.padding(8.dp)) { // Padding de 8dp para el contenido
        seriesList?.chunked(2)?.forEach { rowItems ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (serie in rowItems) {
                        SerieItem(
                            serie = serie,
                            onClick = onSerieClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SerieItem(serie: Show, modifier: Modifier = Modifier, onClick: (Int) -> Unit) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .clickable { serie.id?.let { onClick(it) }}, // Hacer el item clickable
        horizontalAlignment = Alignment.CenterHorizontally // Centrar contenido horizontalmente
    ) {
        Box(modifier = Modifier.size(200.dp)) { // Usamos Box para superponer el rating y la imagen
            // Mostrar la imagen
            AsyncImage(
                model = serie.image?.medium, // Usar la URL de la imagen
                contentDescription = serie.name,
                modifier = Modifier
                    .size(200.dp) // Ajusta el tamaño según lo necesites
            )

            // Mostrar el rating en la esquina superior izquierda
            Text(
                text = serie.rating?.average?.toString() ?: "N/A", // Mostrar el rating o "N/A" si es null
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopStart) // Alinear el rating en la esquina superior izquierda
                    .padding(8.dp) // Padding para que no esté pegado al borde
                    .offset(x = 22.dp, y = -5.dp) // Desplazamiento para ajustar la posición
            )
        }
        // Mostrar el nombre de la serie centrado con respecto a la imagen
        Text(
            text = serie.name ?: "Sin titulo",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true,
        textStyle = androidx.compose.ui.text.TextStyle(
            color = Color.White,
            fontSize = 16.sp
        ),
        decorationBox = { innerTextField ->
            if (query.isEmpty()) {
                Text(text = "Buscar series...", color = Color.Gray)
            }
            innerTextField()
        }
    )
}

@Composable
fun BottomBar2(
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp), // Agregar un padding general
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onHomeClick) {
            Icon(
                imageVector = Icons.Rounded.Home,
                contentDescription = "Back"
            )
        }
        IconButton(onClick = onSearchClick) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Back"
            )
        }
        IconButton(onClick = onFavouritesClick) {
            Icon(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = "Favourites"
            )
        }
    }
}