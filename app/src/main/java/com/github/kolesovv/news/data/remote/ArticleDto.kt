package com.github.kolesovv.news.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ArticleDto(

    @SerialName("source") val sourceDto: SourceDto = SourceDto(),
    @SerialName("title") val title: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("url") val url: String = "",
    @SerialName("urlToImage") val urlToImage: String? = "",
    @SerialName("publishedAt") val publishedAt: String = ""
)
