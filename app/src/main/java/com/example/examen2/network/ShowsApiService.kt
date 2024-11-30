package com.example.examen2.network

import com.example.examen2.modelos.SearchShowResponse
import com.example.examen2.modelos.Show
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShowsApiService {

    @GET("shows")
    suspend fun getShows(): List<Show>

    @GET("shows/{id}?embed=cast")
    suspend fun getShowById(@Path("id") id: Int): Show

    @GET("search/shows")
    suspend fun getShowsByName(@Query("q") name: String): List<SearchShowResponse>

    @GET("shows")
    suspend fun getShowsByPage(@Query("page") page: Int): List<Show>

}