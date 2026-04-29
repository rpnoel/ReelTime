package com.example.reeltime.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a movie entity in the local Room database.
 *
 * @property id The unique auto-generated ID for the database record.
 * @property tmdbId The unique ID of the movie from the TMDb API.
 * @property title The title of the movie.
 * @property overview A brief plot summary of the movie.
 * @property posterPath The partial URL path for the movie's poster image.
 * @property releaseDate The release year or full date of the movie.
 * @property voteAverage The average rating from TMDb (scaled as 0-100 for storage).
 * @property userRating The user's personal rating (0-10), if they have provided one.
 */
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tmdbId: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String?,
    val voteAverage: Int,
    val userRating : Int?
)
