package com.example.examen2.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
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
import androidx.compose.runtime.LaunchedEffect
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
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: ShowsViewModel = viewModel(factory = ShowsViewModel.Factory),
    onSerieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onFavouritesClick: () -> Unit
) {
    // Obtenemos el estado actual del ViewModel
    val uiState = viewModel.uiState.value

    LaunchedEffect(Unit) {
        viewModel.getShows()
    }

    // Scaffold para la estructura de la pantalla
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = "Flix", onAction = { }) },
        bottomBar = {
            BottomBar(
                onHomeClick = {},
                onSearchClick = onSearchClick,
                onFavouritesClick = onFavouritesClick,
                modifier = Modifier
                    .padding(WindowInsets.navigationBars.asPaddingValues()) // Ajustar por los insets del sistema
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Mostrar loading si está cargando
            if (uiState.isLoading) {
                Text(
                    text = "Cargando...",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            } else if (uiState.error != null) {
                // Mostrar error si existe
                Text(
                    text = "Error: ${uiState.error}",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            } else {
                // Mostrar la lista de series
                MostrarPantallaSeries(
                    modifier = Modifier.fillMaxWidth(),
                    seriesList = uiState.shows, // Pasar la lista de shows
                    onSerieClick = onSerieClick
                )
            }
        }
    }
}

@Composable
fun MostrarPantallaSeries(
    modifier: Modifier = Modifier,
    seriesList: List<Show>?,
    onSerieClick: (Int) -> Unit
) {
    if (seriesList.isNullOrEmpty()) {
        Text(
            text = "No hay series disponibles.",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
    } else {
        LazyColumn(modifier = modifier.padding(8.dp)) {
            seriesList.chunked(2).forEach { rowItems ->
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (serie in rowItems) {
                            SeriesItem(
                                serie = serie,
                                onClick = onSerieClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SeriesItem(serie: Show, modifier: Modifier = Modifier, onClick: (Int) -> Unit) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .clickable { serie.id?.let { onClick(it) } }, // Evita llamar onClick si id es null
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(200.dp)) {
            AsyncImage(
                model = serie.image?.medium ?: "", // Provee un valor predeterminado si image.medium es null
                contentDescription = serie.name ?: "Sin nombre",
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = serie.rating?.average?.toString() ?: "N/A",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .offset(x = 22.dp, y = -5.dp)
            )
        }
        Text(
            text = serie.name ?: "Sin nombre", // Provee un valor predeterminado si name es null
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onAction: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.headlineMedium) },
        actions = {
            // Puedes agregar acciones si las necesitas
        }
    )
}

@Composable
fun BottomBar(
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