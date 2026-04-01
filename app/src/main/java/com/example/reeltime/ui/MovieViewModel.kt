package com.example.reeltime.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.reeltime.model.Movie
import com.example.reeltime.MovieDatabase
import com.example.reeltime.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: MovieRepository
    val movies: LiveData<List<Movie>>

    init {
        val dao = MovieDatabase.Companion.getDatabase(application).movieDao()
        repo = MovieRepository(dao)
        movies = repo.movies.asLiveData()
    }

    fun addMovie(text: String) = viewModelScope.launch {
        repo.addMovie(Movie(text = text))
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        repo.deleteMovie(movie)
    }
}