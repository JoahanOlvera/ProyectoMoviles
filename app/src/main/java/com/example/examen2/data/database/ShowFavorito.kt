package com.example.examen2.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritos")
data class ShowFavorito(
    @PrimaryKey val id: Int = 0,
    val name: String? = null,
    val image: String? = null,
    val rate: String? = null,
    val genres: String? = null
)