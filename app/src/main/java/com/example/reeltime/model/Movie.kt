package com.example.reeltime.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val movieId: Int = 0,
    val id: Int,
    val title: String,
    val releaseDate: String?,
    val rating: Double,
    val posterUrl: String?
)