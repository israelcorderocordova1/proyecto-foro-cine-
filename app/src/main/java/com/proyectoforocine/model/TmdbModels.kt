package com.proyectoforocine.model

import com.google.gson.annotations.SerializedName

data class TmdbResponse(
    val results: List<PeliculaTmdb>
)

// El objeto de una sola película
data class PeliculaTmdb(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterPath: String?, // La ruta de la imagen
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    val overview: String
) {
    // Función helper para obtener la URL completa de la imagen
    fun getFullPosterUrl(): String {
        return if (posterPath != null) "https://image.tmdb.org/t/p/w500$posterPath" else ""
    }
}