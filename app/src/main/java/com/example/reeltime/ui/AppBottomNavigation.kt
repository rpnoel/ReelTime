package com.example.reeltime.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home

enum class NavigationDestination {
    HOME,
    SEARCH,
    SAVED,
    LIBRARY,
}

@Composable
fun AppBottomNavigation(
    currentDestination: NavigationDestination,
    onNavigateToHome: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSaved: () -> Unit,
    onNavigateToLibrary: () -> Unit,
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentDestination == NavigationDestination.HOME,
            onClick = onNavigateToHome,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentDestination == NavigationDestination.SEARCH,
            onClick = onNavigateToSearch,
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") }
        )
        NavigationBarItem(
            selected = currentDestination == NavigationDestination.SAVED,
            onClick = onNavigateToSaved,
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Watchlist") },
            label = { Text("Watchlist") }
        )
        NavigationBarItem(
            selected = currentDestination == NavigationDestination.LIBRARY,
            onClick = onNavigateToLibrary,
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Library") },
            label = { Text("Library") }
        )
    }
}
