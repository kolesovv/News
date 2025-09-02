package com.github.kolesovv.news.domain.repository

import com.github.kolesovv.news.domain.entity.Interval
import com.github.kolesovv.news.domain.entity.Language
import com.github.kolesovv.news.domain.entity.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettings(): Flow<Settings>

    suspend fun updateLanguage(language: Language)

    suspend fun updateInterval(interval: Interval)

    suspend fun updateNotificationsEnables(boolean: Boolean)

    suspend fun updateWifiOnly(boolean: Boolean)
}