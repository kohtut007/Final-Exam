package com.homeworks.finalexam

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepository(private val api: TmdbApi) {
    suspend fun fetchPopular(): List<Movie> = withContext(Dispatchers.IO) {
        api.getPopular().results
    }

    suspend fun fetchNowPlaying(): List<Movie> = withContext(Dispatchers.IO) {
        api.getNowPlaying().results
    }

    suspend fun fetchUpcoming(): List<Movie> = withContext(Dispatchers.IO) {
        api.getUpcoming().results
    }

    suspend fun fetchDetail(id: Int): MovieDetail = withContext(Dispatchers.IO) {
        api.getDetails(id)
    }

    suspend fun fetchCredits(id: Int): CreditsResponse = withContext(Dispatchers.IO) {
        api.getCredits(id)
    }
}


