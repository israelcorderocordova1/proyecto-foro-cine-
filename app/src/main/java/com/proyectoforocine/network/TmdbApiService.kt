package com.proyectoforocine.network

import com.proyectoforocine.model.TmdbResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiService {
    @GET("movie/now_playing")
    suspend fun obtenerEstrenos(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES",
        @Query("page") page: Int = 1
    ): TmdbResponse
}