package com.example.reeltime.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.reeltime.api.TmdbClient
import com.example.reeltime.model.Movie
import com.example.reeltime.ui.MovieViewModel
import com.example.reeltime.ui.getRatingColor

@Composable
fun LibraryScreen(
    viewModel: MovieViewModel,
    onMovieClick: (Movie) -> Unit
) {
    val allMovies by viewModel.movies.observeAsState(emptyList())
    // Filter for movies that have a rating
    val ratedMovies = remember(allMovies) { 
        allMovies.filter { it.userRating != null } 
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "My Library",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        if (ratedMovies.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No rated movies yet!", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn {
                items(ratedMovies, key = { it.id }) { movie ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMovieClick(movie) }
                            .padding(8.dp)
                    ) {
                        movie.posterPath?.let { path ->
                            AsyncImage(
                                model = "${TmdbClient.BASE_IMAGE_URL}$path",
                                contentDescription = "Poster of ${movie.title}",
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(120.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "${movie.userRating}%",
                                style = MaterialTheme.typography.bodyMedium,
                                color = getRatingColor(movie.userRating ?: 0)
                            )
                            Text(
                                text = movie.releaseDate ?: "Unknown Year",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
