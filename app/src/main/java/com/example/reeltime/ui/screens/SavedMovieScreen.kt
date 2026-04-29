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
fun SavedMovieScreen(
    viewModel: MovieViewModel,
    onMovieClick: (Movie) -> Unit
) {
    val allMovies by viewModel.movies.observeAsState(emptyList())
    // Filter for movies that DON'T have a rating yet
    val watchlist = remember(allMovies) { 
        allMovies.filter { it.userRating == null } 
    }

    Column {
        Text(
            text = "Watchlist", 
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            items(watchlist, key = { it.id }) { movie ->
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
                                .height(120.dp)
                                .clickable { viewModel.deleteMovie(movie) },
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
                        if (movie.userRating != null) {
                            Text(
                                text = "${movie.userRating}%",
                                style = MaterialTheme.typography.bodySmall,
                                color = getRatingColor(movie.userRating)
                            )
                        } else {
                            Text(
                                text = "Rate this movie!",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Text(
                            text = movie.releaseDate ?: "No release date",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Click poster to remove",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                HorizontalDivider()
            }
        }
    }
}
