package com.example.reeltime.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.reeltime.ui.screens.SearchScreen
import com.example.reeltime.ui.screens.SavedMovieScreen
import com.example.reeltime.ui.screens.RatingScreen
import com.example.reeltime.ui.screens.LibraryScreen
import com.example.reeltime.model.Movie
import com.example.reeltime.ui.screens.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: MovieViewModel = viewModel()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var editingMovie by remember { mutableStateOf<Movie?>(null) }

    Scaffold(
        bottomBar = {
            AppBottomNavigation(
                currentDestination = when (currentRoute) {
                    "saved" -> NavigationDestination.SAVED
                    "library" -> NavigationDestination.LIBRARY
                    "search" -> NavigationDestination.SEARCH
                    else -> NavigationDestination.HOME
                },
                onNavigateToHome= {
                    editingMovie = null
                    navController.navigate("home") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToSearch = {
                    editingMovie = null
                    navController.navigate("search") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToSaved = {
                    editingMovie = null
                    navController.navigate("saved") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToLibrary = {
                    editingMovie = null
                    navController.navigate("library") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(0)) },
            exitTransition = { fadeOut(animationSpec = tween(0)) }
        ) {
            composable("search") {
                SearchScreen(viewModel = viewModel)
            }
            composable("saved") {
                if (editingMovie == null) {
                    SavedMovieScreen(
                        viewModel = viewModel,
                        onMovieClick = { editingMovie = it }
                    )
                } else {
                    RatingScreen(
                        movie = editingMovie!!,
                        viewModel = viewModel,
                        onBack = { editingMovie = null }
                    )
                }
            }
            composable("library") {
                if (editingMovie == null) {
                    LibraryScreen(
                        viewModel = viewModel,
                        onMovieClick = { editingMovie = it }
                    )
                } else {
                    RatingScreen(
                        movie = editingMovie!!,
                        viewModel = viewModel,
                        onBack = { editingMovie = null }
                    )
                }
            }
            composable("home") {
                if (editingMovie == null) {
                    HomeScreen(
                        viewModel = viewModel,
                        onMovieClick = { editingMovie = it }
                    )
                } else {
                    RatingScreen(
                        movie = editingMovie!!,
                        viewModel = viewModel,
                        onBack = { editingMovie = null }
                    )
                }
            }
        }
    }
}
