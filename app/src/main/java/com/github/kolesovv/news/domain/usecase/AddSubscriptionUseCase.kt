package com.github.kolesovv.news.domain.usecase

import com.github.kolesovv.news.domain.repository.NewsRepository
import javax.inject.Inject

class AddSubscriptionUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(topic: String) {
        newsRepository.addSubscription(topic)
        newsRepository.updatedArticlesForTopic(topic)
    }
}