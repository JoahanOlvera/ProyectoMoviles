package com.example.examen2.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowDao {

    @Query("SELECT * FROM favoritos")
    fun getAll(): Flow<List<ShowFavorito>>

    @Query("SELECT COUNT(*) FROM favoritos")
    fun getCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteShow(favoriteShow: ShowFavorito)

    @Delete
    suspend fun deleteFavoriteShow(favoriteShow: ShowFavorito)

    @Query("SELECT * FROM favoritos WHERE id = :showId")
    suspend fun getFavoriteShowById(showId: Int): ShowFavorito?
}