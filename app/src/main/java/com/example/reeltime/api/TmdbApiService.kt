package com.example.reeltime.api

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiService {
    @GET("3/search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("language") language: String = "en-US",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("page") page: Int = 1
    ): TmdbSearchResponse
}