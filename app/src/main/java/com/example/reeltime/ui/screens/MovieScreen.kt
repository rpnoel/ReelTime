package com.example.reeltime.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reeltime.model.Movie
import com.example.reeltime.ui.MovieViewModel

@Composable
fun MovieScreen(viewModel: MovieViewModel) {
    var showSaved by rememberSaveable { mutableStateOf(false) }
    var editingMovie by rememberSaveable { mutableStateOf<Movie?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(Modifier.height(32.dp))

        if (editingMovie == null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Watchlist")
                Switch(checked = showSaved, onCheckedChange = { showSaved = it })
                Text("Search")
            }

            Spacer(Modifier.height(16.dp))

            if (!showSaved) {
                SearchScreen(viewModel = viewModel)
            } else {
                SavedMovieScreen(
                    viewModel = viewModel,
                    onMovieClick = { editingMovie = it }
                )
            }
        } else {
            RatingScreen(
                movie = editingMovie!!,
                viewModel = viewModel,
                onBack = { editingMovie = null }
            )
        }
    }
}
