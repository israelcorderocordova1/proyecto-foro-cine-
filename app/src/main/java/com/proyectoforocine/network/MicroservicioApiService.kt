interface MicroservicioApiService {
    // Coincide con @GetMapping en Spring
    @GET("temas")
    suspend fun obtenerTemas(): List<TemaForo>

    // Coincide con @PostMapping en Spring
    @POST("temas")
    suspend fun crearTema(@Body tema: TemaForo): TemaForo

    // Coincide con @DeleteMapping("/{id}") en Spring
    @DELETE("temas/{id}")
    suspend fun eliminarTema(@Path("id") id: Long)
}