package com.example.reeltime.api

import com.example.reeltime.api.MovieDTO

interface MovieAPI {

    suspend fun fetchDiscoverMovie(): MovieDTO
}