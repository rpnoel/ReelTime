package com.example.reeltime.api

import com.example.reeltime.model.Movie
import kotlinx.serialization.Serializable
import com.google.gson.annotations.SerializedName

@Serializable
data class MovieDTO(
    val id: Int,
    val title: String,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("poster_path")
    val posterPath: String?
)

@Serializable
data class MovieResponse(
    val page: Int,
    val results: List<MovieDTO>
)

fun MovieDTO.toMovie(): Movie {
    return Movie (
        id = this.id,
        title = this.title,
        releaseDate = this.releaseDate,
        rating = this.voteAverage,
        posterUrl = this.posterPath
    )
}