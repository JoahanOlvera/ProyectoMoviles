package com.example.examen2.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.examen2.data.database.ShowFavorito

@Composable
fun FavouritesScreen(
    modifier: Modifier = Modifier,
    viewModel: ShowsViewModel = viewModel(factory = ShowsViewModel.Factory),
    onHomeClick: () -> Unit,
    onSerieClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    onFavouritesClick: () -> Unit
) {
    // Obtener el estado actual del ViewModel
    val uiState = viewModel.uiState.value

    // Cargar los favoritos al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.getAllFavoriteShows()
    }

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar3(title = "Favoritos", onAction = { }) },
        bottomBar = {
            BottomBar3(
                onHomeClick = onHomeClick,
                onSearchClick = onSearchClick,
                onFavouritesClick = onFavouritesClick,
                modifier = Modifier
                    .padding(WindowInsets.navigationBars.asPaddingValues()) // Ajustar por los insets del sistema
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    // Mostrar loading si está cargando
                    Text(
                        text = "Cargando favoritos...",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
                uiState.error != null -> {
                    // Mostrar error si ocurre
                    Text(
                        text = "Error: ${uiState.error}",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
                uiState.favoritesShow.isEmpty() -> {
                    // Mostrar mensaje si no hay favoritos
                    Text(
                        text = "No tienes favoritos guardados.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
                else -> {
                    // Mostrar lista de favoritos
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.favoritesShow) { favorite ->
                            FavoriteItem(
                                favoriteShow = favorite,
                                onClick = { onSerieClick(favorite.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteItem(
    favoriteShow: ShowFavorito,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = favoriteShow.image,
            contentDescription = "Imagen de ${favoriteShow.name}",
            modifier = Modifier
                .size(80.dp)
                .padding(end = 16.dp)
        )
        Column {
            Text(
                text = favoriteShow.name ?: "Sin título",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Rating: ${favoriteShow.rate ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = "Genres: ${favoriteShow.genres?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar3(
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
fun BottomBar3(
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