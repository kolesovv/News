package com.github.kolesovv.news.domain.usecase

import com.github.kolesovv.news.domain.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class AddSubscriptionUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(topic: String) {
        newsRepository.addSubscription(topic)

        CoroutineScope(context = coroutineContext).launch {
            newsRepository.updatedArticlesForTopic(topic)
        }
    }
}