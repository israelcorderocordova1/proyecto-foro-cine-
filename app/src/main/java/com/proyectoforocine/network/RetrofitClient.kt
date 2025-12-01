package com.proyectoforocine.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL_SPRING = "http://10.0.2.2:8080/"

    val microservicioApi: MicroservicioApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_SPRING)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MicroservicioApiService::class.java)
    }

    // Configuración para TMDB (Se mantiene igual)
    private const val BASE_URL_TMDB = "https://api.themoviedb.org/3/"
    val tmdbApi: TmdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_TMDB)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApiService::class.java)
    }
}