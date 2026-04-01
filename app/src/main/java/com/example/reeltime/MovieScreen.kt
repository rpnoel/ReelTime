package com.example.reeltime

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reeltime.ui.MovieViewModel

@Composable
fun MovieScreen(viewModel: MovieViewModel) {
    val movies by viewModel.movies.observeAsState(emptyList())
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("New Movie") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            if (input.isNotBlank()) {
                viewModel.addMovie(input)
                input = ""
            }
        }) {
            Text("Add")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(movies) { movie ->
                Text(
                    text = movie.text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.deleteMovie(movie) }
                        .padding(8.dp)
                )
            }
        }
    }
}