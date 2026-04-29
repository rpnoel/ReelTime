package com.example.reeltime.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.reeltime.model.Movie
import com.example.reeltime.ui.MovieViewModel

@Composable
fun RatingScreen(
    movie: Movie,
    viewModel: MovieViewModel,
    onBack: () -> Unit
) {
    var rating by remember(movie.id) { mutableStateOf(movie.userRating?.toString() ?: "") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Rating for ${movie.title}", style = MaterialTheme.typography.headlineSmall)
        
        Spacer(Modifier.height(16.dp))
        
        OutlinedTextField(
            value = rating,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    rating = newValue
                }
            },
            label = { Text("Enter Rating (0-100)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    val ratingInt = rating.toIntOrNull()
                    val updatedMovie = movie.copy(userRating = ratingInt)
                    viewModel.updateMovie(updatedMovie)
                    onBack()
                }
            ) {
                Text("Save")
            }
            TextButton(onClick = onBack) {
                Text("Cancel")
            }
        }
    }
}
