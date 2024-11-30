package com.example.examen2.data

import com.example.examen2.data.database.ShowDao
import com.example.examen2.data.database.ShowFavorito
import com.example.examen2.modelos.Show
import com.example.examen2.network.ShowsApiService
import kotlinx.coroutines.flow.first

class ShowsRepository(
    private val showsApiService: ShowsApiService,
    private val showDao: ShowDao
) {
    suspend fun getShows(): List<Show> = showsApiService.getShows()
    suspend fun getShowById(id: Int): Show = showsApiService.getShowById(id)
    suspend fun getShowsByName(name: String): List<Show> {
        val response = showsApiService.getShowsByName(name)
        return response.map { it.show }
    }
    suspend fun addFavoriteShow(favoriteShow: ShowFavorito) {
        showDao.insertFavoriteShow(favoriteShow)
    }

    suspend fun getAllFavoriteShows(): List<ShowFavorito>{
        return showDao.getAll().first()
    }

    suspend fun removeFavoriteShow(favoriteShow: ShowFavorito) {
        showDao.deleteFavoriteShow(favoriteShow)
    }

    suspend fun isFavoriteShow(id: Int): Boolean {
        return showDao.getFavoriteShowById(id) != null
    }
}