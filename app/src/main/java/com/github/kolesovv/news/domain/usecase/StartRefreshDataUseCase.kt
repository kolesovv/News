package com.github.kolesovv.news.domain.usecase

import com.github.kolesovv.news.data.mapper.toRefreshConfig
import com.github.kolesovv.news.domain.repository.NewsRepository
import com.github.kolesovv.news.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class StartRefreshDataUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke() {
        settingsRepository.getSettings()
            .map { it.toRefreshConfig() }
            .distinctUntilChanged()
            .onEach { newsRepository.startBackgroundRefresh(it) }
            .collect()
    }
}