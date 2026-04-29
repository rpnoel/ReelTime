package com.example.reeltime.ui

import androidx.compose.ui.graphics.Color

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
