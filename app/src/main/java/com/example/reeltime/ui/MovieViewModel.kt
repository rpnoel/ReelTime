package com.example.reeltime.ui

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.reeltime.MovieDatabase
import com.example.reeltime.MovieRepository
import com.example.reeltime.api.TmdbClient
import com.example.reeltime.api.TmdbMovieDto
import com.example.reeltime.model.Movie
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: MovieRepository
    val movies: LiveData<List<Movie>>

    var searchResults = mutableStateOf<List<TmdbMovieDto>>(emptyList())
        private set

    var isLoading = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    init {
        val dao = MovieDatabase.getDatabase(application).movieDao()
        repo = MovieRepository(dao)
        movies = repo.movies.asLiveData()
    }

    fun searchTmdb(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            try {
                // Now using apiKey query parameter with default value from BuildConfig
                val response = TmdbClient.api.searchMovies(query = query)
                searchResults.value = response.results ?: emptyList()
            } catch (e: Exception) {
                errorMessage.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }

    fun addMovie(dto: TmdbMovieDto) = viewModelScope.launch {
        repo.addMovie(
            Movie(
                tmdbId = dto.id,
                title = dto.title,
                overview = dto.overview ?: "",
                posterPath = dto.poster_path,
                releaseDate = (dto.release_date)?.substring(0, 4),
                voteAverage = (dto.vote_average * 10).roundToInt()
            )
        )
    }

    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        repo.deleteMovie(movie)
    }
}