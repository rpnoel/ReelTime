package com.example.reeltime

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TmdbApiService {
    @GET("3/search/movie")
    suspend fun searchMovies(
        @Header("Authorization") authorization: String = "Bearer ${BuildConfig.TMDB_BEARER_TOKEN}",
        @Query("query") query: String,
        @Query("language") language: String = "en-US",
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("page") page: Int = 1
    ): TmdbSearchResponse
}