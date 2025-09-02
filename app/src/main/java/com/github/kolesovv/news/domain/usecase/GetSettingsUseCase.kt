package com.github.kolesovv.news.domain.usecase

import com.github.kolesovv.news.domain.entity.Settings
import com.github.kolesovv.news.domain.repository.SettingsRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    operator fun invoke(): Flow<Settings> {
        return settingsRepository.getSettings()
    }
}