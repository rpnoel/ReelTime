package com.example.reeltime

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.reeltime.ui.MovieViewModel
import kotlin.math.roundToInt

@Composable
fun MovieScreen(viewModel: MovieViewModel) {
    val savedMovies by viewModel.movies.observeAsState(emptyList())
    val results = viewModel.searchResults.value
    val isLoading = viewModel.isLoading.value
    val error = viewModel.errorMessage.value

    var input by remember { mutableStateOf("") }
    var showSaved by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(Modifier.height(32.dp))

        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Watchlist")
            Switch(checked = showSaved, onCheckedChange = { showSaved = it })
            Text("Search")
        }

        if (!showSaved) {
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
                Text("Error: $it")
            }

            LazyColumn {
                items(results) { movie ->
                    val rating = (movie.vote_average * 10).roundToInt()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.addMovie(movie) }
                            .padding(8.dp)
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
                            Text(text = movie.title + " (${(movie.release_date)?.substring(0, 4)})", style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = "$rating%",
                                style = MaterialTheme.typography.bodyMedium,
                                color = getRatingColor(rating),
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = (movie.release_date)?.substring(0, 4) ?: "No release date", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    HorizontalDivider()
                }
            }
        } else {
            Text("My Watchlist:", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            LazyColumn {
                items(savedMovies) { movie ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.deleteMovie(movie) }
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
                                text = "Rating: ${movie.voteAverage}%", 
                                style = MaterialTheme.typography.bodySmall,
                                color = getRatingColor(movie.voteAverage)
                            )
                            Text(text = movie.releaseDate ?: "No release date", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = "Click to remove", 
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
}

fun getRatingColor(rating: Int): Color {
    return when {
        rating >= 90 -> Color(0xFF00FF14)
        rating >= 80 -> Color(0xFF4CAF50)
        rating >= 70 -> Color(0xFFCDDC39)
        rating >= 60 -> Color(0xFFFFC107)
        rating >= 50 -> Color(0xFFC04400)
        rating >= 40 -> Color(0xFFAD0E00)
        rating >= 30 -> Color(0xFFAD0E00)
        else -> Color(0xFFD32F2F)
    }
}