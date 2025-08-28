package com.github.kolesovv.news.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NewsResponseDto(

    @SerialName("articles")
    val articles: ArrayList<ArticleDto> = arrayListOf()
)
