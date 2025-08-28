package com.github.kolesovv.news.domain.usecase
import com.github.kolesovv.news.domain.entity.Article
import com.github.kolesovv.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArticlesForTopicsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    operator fun invoke(topics: List<String>): Flow<List<Article>> {
        return newsRepository.getArticlesForTopics(topics)
    }
}