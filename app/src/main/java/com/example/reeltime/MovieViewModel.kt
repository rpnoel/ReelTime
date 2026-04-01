package com.example.reeltime

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: MovieRepository
    val movies: LiveData<List<Movie>>

    init {
        val dao = MovieDatabase.getDatabase(application).movieDao()
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