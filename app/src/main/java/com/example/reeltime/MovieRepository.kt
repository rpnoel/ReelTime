package com.example.reeltime

import com.example.reeltime.model.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val dao: MovieDao) {
    val movies: Flow<List<Movie>> = dao.getAllMovies()

    suspend fun addMovie(movie: Movie) = dao.insert(movie)
    suspend fun deleteMovie(movie: Movie) = dao.delete(movie)
}