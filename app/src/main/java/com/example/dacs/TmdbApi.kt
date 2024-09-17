package com.example.dacs


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("list/{list_id}")
    suspend fun getMovieList(
        @Path("list_id") listId: Int,
        @Query("api_key") apiKey: String
    ): ListResponse
}