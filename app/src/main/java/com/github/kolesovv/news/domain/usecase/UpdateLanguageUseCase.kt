package com.github.kolesovv.news.domain.usecase

import com.github.kolesovv.news.domain.entity.Language
import com.github.kolesovv.news.domain.repository.SettingsRepository
import jakarta.inject.Inject

class UpdateLanguageUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend operator fun invoke(language: Language) {
        settingsRepository.updateLanguage(language)
    }
}