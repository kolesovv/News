package com.github.kolesovv.news.domain.usecase

import com.github.kolesovv.news.domain.entity.Interval
import com.github.kolesovv.news.domain.repository.SettingsRepository
import jakarta.inject.Inject

class UpdateIntervalUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend operator fun invoke(interval: Interval) {
        settingsRepository.updateInterval(interval)
    }
}