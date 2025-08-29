package com.github.kolesovv.news.data.mapper

import android.util.Log
import com.github.kolesovv.news.data.local.ArticleDbModel
import com.github.kolesovv.news.data.remote.NewsResponseDto
import com.github.kolesovv.news.domain.entity.Article
import java.text.SimpleDateFormat
import java.util.Locale

fun NewsResponseDto.toDbModels(topic: String): List<ArticleDbModel> {

    return articles.map {
        Log.d("Mapper", "toDbModels: ${it.sourceDto.name}")
        ArticleDbModel(
            title = it.title,
            description = it.description,
            imageUrl = it.urlToImage,
            sourceName = it.sourceDto.name,
            publishedAt = it.publishedAt.toTimeStamp(),
            url = it.url,
            topic = topic
        )
    }
}

fun List<ArticleDbModel>.toEntities(): List<Article> {
    return this.map {
        Log.d("Mapper", "toEntities: ${it.sourceName}")
        Article(
            title = it.title,
            description = it.description,
            imageUrl = it.imageUrl,
            sourceName = it.sourceName,
            publishedAt = it.publishedAt,
            url = it.url,
            topic = it.topic
        )
    }.distinct()
}

fun String.toTimeStamp(): Long {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return dateFormatter.parse(this)?.time ?: System.currentTimeMillis()
}