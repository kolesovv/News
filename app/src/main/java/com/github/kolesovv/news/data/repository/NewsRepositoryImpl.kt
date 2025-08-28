package com.github.kolesovv.news.data.repository

import android.util.Log
import com.github.kolesovv.news.data.local.ArticleDbModel
import com.github.kolesovv.news.data.local.NewsDao
import com.github.kolesovv.news.data.local.SubscriptionDbModel
import com.github.kolesovv.news.data.mapper.toDbModels
import com.github.kolesovv.news.data.mapper.toEntities
import com.github.kolesovv.news.data.remote.NewsApiService
import com.github.kolesovv.news.domain.entity.Article
import com.github.kolesovv.news.domain.repository.NewsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val newsApiService: NewsApiService
) : NewsRepository {

    override fun getAllSubscriptions(): Flow<List<String>> {
        return newsDao.getAllSubscriptions()
            .map { subscriptions -> subscriptions.map { it.topic } }
    }

    override suspend fun addSubscription(topic: String) {
        val subscriptionDbModel = SubscriptionDbModel(topic)
        newsDao.addSubscription(subscriptionDbModel)
    }

    override suspend fun updatedArticlesForTopic(topic: String) {
        val articles = loadArticles(topic)
        newsDao.addArticles(articles)
    }

    override suspend fun removeSubscription(topic: String) {
        val subscriptionDbModel = SubscriptionDbModel(topic)
        newsDao.removeSubscription(subscriptionDbModel)
    }

    override suspend fun updateArticlesForAllSubscription() {
        val subscriptions = newsDao.getAllSubscriptions().first()

        coroutineScope {
            subscriptions.forEach {
                launch {
                    updatedArticlesForTopic(it.topic)
                }
            }
        }
    }

    override fun getArticlesForTopics(topics: List<String>): Flow<List<Article>> {
        return newsDao.getAllArticlesByTopics(topics).map { it.toEntities() }
    }

    override suspend fun clearAllArticles(topics: List<String>) {
        newsDao.deleteArticlesByTopics(topics)
    }

    private suspend fun loadArticles(topic: String): List<ArticleDbModel> {
        return try {
            newsApiService.loadArticles(topic).toDbModels(topic)
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            } else {
                Log.e("NewsRepository", e.stackTraceToString())
                listOf()
            }
        }
    }
}