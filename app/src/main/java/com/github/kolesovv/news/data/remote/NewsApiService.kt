package com.github.kolesovv.news.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/everything?apiKey=")
    suspend fun loadArticles(
        @Query("q") topic: String
    ): NewsResponseDto
}