package com.github.kolesovv.news.domain.usecase

import com.github.kolesovv.news.domain.repository.SettingsRepository
import jakarta.inject.Inject

class UpdateNotificationsEnablesUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend operator fun invoke(boolean: Boolean) {
        settingsRepository.updateNotificationsEnables(boolean)
    }
}