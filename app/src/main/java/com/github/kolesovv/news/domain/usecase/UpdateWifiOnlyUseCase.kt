package com.github.kolesovv.news.domain.usecase

import com.github.kolesovv.news.domain.repository.SettingsRepository
import jakarta.inject.Inject

class UpdateWifiOnlyUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend operator fun invoke(boolean: Boolean) {
        settingsRepository.updateWifiOnly(boolean)
    }
}