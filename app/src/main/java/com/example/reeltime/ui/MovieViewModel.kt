package com.example.reeltime.ui

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.reeltime.BuildConfig
import com.example.reeltime.MovieDatabase
import com.example.reeltime.MovieRepository
import com.example.reeltime.api.TmdbClient
import com.example.reeltime.api.TmdbMovieDto
import com.example.reeltime.model.Movie
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * ViewModel for managing movie data, search results, and AI recommendations.
 * 
 * This class acts as the bridge between the UI (Compose screens) and the data layer
 * (Room database and TMDb API). It also handles the integration with Gemini AI.
 */
class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: MovieRepository
    
    /**
     * LiveData stream of all movies saved in the local Room database.
     */
    val movies: LiveData<List<Movie>>

    /**
     * State for TMDb search results, observed by the search UI.
     */
    var searchResults = mutableStateOf<List<TmdbMovieDto>>(emptyList())
        private set

    /**
     * General loading state for network operations.
     */
    var isLoading = mutableStateOf(false)
        private set

    /**
     * Holds error messages for display in the UI.
     */
    var errorMessage = mutableStateOf<String?>(null)
        private set

    /**
     * The latest AI-generated recommendation text.
     */
    var aiRecommendations = mutableStateOf("Fetching your personalized recommendations...")
        private set

    /**
     * Specific loading state for AI recommendation fetches.
     */
    var isAiLoading = mutableStateOf(false)
        private set

    /**
     * Tracks the last prompt sent to Gemini to prevent redundant calls on rotation.
     */
    private var lastPrompt: String? = null
    
    /**
     * Reference to the active AI coroutine job, used for cancellation.
     */
    private var recommendationJob: kotlinx.coroutines.Job? = null

    private var _generativeModel: GenerativeModel? = null

    /**
     * Lazy-initialized Gemini model using the API key from BuildConfig.
     */
    val generativeModel: GenerativeModel?
        get() {
            if (_generativeModel == null) {
                try {
                    _generativeModel = GenerativeModel(
                        modelName = "gemini-2.5-flash-lite",
                        apiKey = BuildConfig.GEMINI_API_KEY
                    )
                } catch (e: Throwable) {
                    // Log or handle the error
                }
            }
            return _generativeModel
        }

    /**
     * Fetches movie recommendations from Gemini AI based on the provided prompt.
     * 
     * This method cancels any existing recommendation requests before starting a new one.
     * It ensures the loading state is managed correctly even if the request is superseded.
     *
     * @param prompt The instructions and context to send to the AI.
     * @param force If true, bypasses the deduplication check against [lastPrompt].
     */
    fun fetchAiRecommendations(prompt: String, force: Boolean = false) {
        if (lastPrompt == prompt && !force) return
        
        val model = generativeModel ?: return
        lastPrompt = prompt
        
        recommendationJob?.cancel()
        recommendationJob = viewModelScope.launch {
            val currentJob = coroutineContext[kotlinx.coroutines.Job]
            isAiLoading.value = true
            
            try {
                // Show a personalized message if library data is detected in the prompt
                if (prompt.contains("I have already saved")) {
                    aiRecommendations.value = "Analyzing your library to find movies you'll love..."
                }

                val response = model.generateContent(prompt)
                aiRecommendations.value = response.text ?: "No recommendations found."
            } catch (e: Exception) {
                if (e !is kotlinx.coroutines.CancellationException) {
                    aiRecommendations.value = "Error: ${e.localizedMessage}"
                    lastPrompt = null 
                }
            } finally {
                // Only reset loading state if this is still the active request
                if (recommendationJob == currentJob) {
                    isAiLoading.value = false
                }
            }
        }
    }

    init {
        val dao = MovieDatabase.getDatabase(application).movieDao()
        repo = MovieRepository(dao)
        movies = repo.movies.asLiveData()
    }

    /**
     * Searches the TMDb API for movies matching the query.
     */
    fun searchTmdb(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            try {
                val response = TmdbClient.api.searchMovies(query = query)
                searchResults.value = response.results ?: emptyList()
            } catch (e: Exception) {
                errorMessage.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }

    /**
     * Converts a TMDb DTO to a local Movie entity and adds it to the database.
     */
    fun addMovie(dto: TmdbMovieDto) = viewModelScope.launch {
        repo.addMovie(
            Movie(
                tmdbId = dto.id,
                title = dto.title,
                overview = dto.overview,
                posterPath = dto.poster_path,
                releaseDate = (dto.release_date)?.take(4),
                voteAverage = (dto.vote_average * 10).roundToInt(),
                userRating = null
            )
        )
    }

    /**
     * Removes a movie from the local database.
     */
    fun deleteMovie(movie: Movie) = viewModelScope.launch {
        repo.deleteMovie(movie)
    }

    /**
     * Updates an existing movie (e.g., when a user adds a personal rating).
     */
    fun updateMovie(movie: Movie) = viewModelScope.launch {
        repo.updateMovie(movie)
    }

}
