package com.example.reeltime.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.reeltime.api.TmdbClient
import com.example.reeltime.ui.MovieViewModel
import com.example.reeltime.ui.getRatingColor
import kotlin.math.roundToInt

@Composable
fun SearchScreen(viewModel: MovieViewModel) {
    val results = viewModel.searchResults.value
    val isLoading = viewModel.isLoading.value
    val error = viewModel.errorMessage.value
    val savedMovies by viewModel.movies.observeAsState(emptyList())
    val savedMovieIds = remember(savedMovies) { savedMovies.map { it.tmdbId }.toSet() }
    
    var input by rememberSaveable { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { viewModel.searchTmdb(input) }
        ) {
            Text("Search")
        }

        Spacer(Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        }

        error?.let {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }

        LazyColumn {
            items(results, key = { it.id }) { movie ->
                val rating = (movie.vote_average * 10).roundToInt()
                val isSaved = savedMovieIds.contains(movie.id)
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isSaved) { viewModel.addMovie(movie) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    movie.poster_path?.let { path ->
                        AsyncImage(
                            model = "${TmdbClient.BASE_IMAGE_URL}$path",
                            contentDescription = "Poster of ${movie.title}",
                            modifier = Modifier
                                .width(70.dp)
                                .height(100.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = movie.title + " (${(movie.release_date)?.take(4)})",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "$rating%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = getRatingColor(rating),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = (movie.release_date)?.take(4) ?: "No release date",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    if (isSaved) {
                        Spacer(Modifier.weight(1f))
                    }

                }
                HorizontalDivider()
            }
        }
    }
}
