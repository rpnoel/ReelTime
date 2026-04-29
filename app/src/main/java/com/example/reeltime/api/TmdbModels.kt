package com.example.reeltime.api

data class TmdbMovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val release_date: String?,
    val vote_average: Double
)

data class TmdbSearchResponse(
    val results: List<TmdbMovieDto>
)