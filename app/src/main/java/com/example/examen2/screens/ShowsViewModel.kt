package com.example.examen2.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.examen2.ShowsApplication
import com.example.examen2.data.ShowsRepository
import com.example.examen2.data.database.ShowFavorito
import com.example.examen2.modelos.Show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UIState(
    val shows: List<Show> = emptyList(),
    val selectedShow: Show? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val favoritesShow: List<ShowFavorito> = emptyList(),
)

class ShowsViewModel(
    private val showsRepository: ShowsRepository
) : ViewModel() {

    val uiState = mutableStateOf(UIState())

    private val _query = MutableStateFlow("") // Estado de la consulta de búsqueda
    val query: StateFlow<String> = _query

    private val _filteredSeries = MutableStateFlow<List<Show>>(emptyList()) // Estado de las series filtradas
    val filteredSeries: StateFlow<List<Show>> = _filteredSeries

    //api
    fun getShows() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = uiState.value.copy(isLoading = true)
            try {
                val shows = showsRepository.getShows()
                uiState.value = uiState.value.copy(shows = shows)
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(error = e.message)
            } finally {
                uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }

    fun getShowById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = uiState.value.copy(isLoading = true)
            try {
                val show = showsRepository.getShowById(id)
                uiState.value = uiState.value.copy(selectedShow = show)
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(error = e.message)
            } finally {
                uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }

    fun searchSeries(query: String) {
        _query.value = query // Actualiza la consulta
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isNotBlank()) {
                try {
                    val shows = showsRepository.getShowsByName(query)
                    _filteredSeries.value = shows // Actualiza las series filtradas
                } catch (e: Exception) {
                    _filteredSeries.value = emptyList() // Reinicia la lista si hay un error
                }
            } else {
                _filteredSeries.value = emptyList() // Limpia los resultados si no hay consulta
            }
        }
    }

    fun getShowsByName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = uiState.value.copy(isLoading = true)
            try {
                val shows = showsRepository.getShowsByName(name)
                uiState.value = uiState.value.copy(shows = shows)
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(error = e.message)
            } finally {
                uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }

    fun getAllFavoriteShows(){
        viewModelScope.launch((Dispatchers.IO)) {
            val allFavorites = showsRepository.getAllFavoriteShows()
            uiState.value = uiState.value.copy(favoritesShow = allFavorites)
        }
    }

    fun addFavoriteShow(favoriteShow: ShowFavorito) {
        viewModelScope.launch((Dispatchers.IO)) {
            showsRepository.addFavoriteShow(favoriteShow)
            val allFavorites = showsRepository.getAllFavoriteShows()
            Log.d("TAG", "Todos los favoritos: $allFavorites")
        }
    }

    fun deleteFavoriteShow(favoriteShow: ShowFavorito){
        viewModelScope.launch(Dispatchers.IO) {
            showsRepository.removeFavoriteShow(favoriteShow)
            getAllFavoriteShows()
            val allFavorites = showsRepository.getAllFavoriteShows()
            Log.d("TAG", "Todos los favoritos: $allFavorites")
        }
    }

    fun isFavoriteShow(id: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch((Dispatchers.IO)) {
            val result = showsRepository.isFavoriteShow(id)
            onResult(result)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (
                        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as
                                ShowsApplication)
                val showsRepository = application.container.showsRepository
                ShowsViewModel(showsRepository = showsRepository)
            }
        }
    }
}
