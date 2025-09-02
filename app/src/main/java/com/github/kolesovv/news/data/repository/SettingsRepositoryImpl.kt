package com.github.kolesovv.news.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.github.kolesovv.news.data.mapper.toInterval
import com.github.kolesovv.news.domain.entity.Interval
import com.github.kolesovv.news.domain.entity.Language
import com.github.kolesovv.news.domain.entity.Settings
import com.github.kolesovv.news.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : SettingsRepository {

    private val languageKey = stringPreferencesKey("language")
    private val intervalKey = intPreferencesKey("interval")
    private val notificationsEnableKey = booleanPreferencesKey("notifications_enabled")
    private val wifiOnlyKey = booleanPreferencesKey("wifi_only")

    override fun getSettings(): Flow<Settings> {
        return context.dataStore.data.map { preferences ->
            val languageAsString = preferences[languageKey] ?: Settings.DEFAULT_LANGUAGE.name
            val language = Language.valueOf(languageAsString)
            val interval = preferences[intervalKey]?.toInterval() ?: Settings.DEFAULT_INTERVAL
            val notificationEnable =
                preferences[notificationsEnableKey] ?: Settings.DEFAULT_NOTIFICATION
            val wifiOnly = preferences[wifiOnlyKey] ?: Settings.DEFAULT_WIFI_ONLY

            Settings(
                language = language,
                interval = interval,
                notificationEnable = notificationEnable,
                wifiOnly = wifiOnly
            )
        }
    }

    override suspend fun updateLanguage(language: Language) {
        context.dataStore.edit { settings ->
            settings[languageKey] = language.name
        }
    }

    override suspend fun updateInterval(interval: Interval) {
        context.dataStore.edit { settings ->
            settings[intervalKey] = intervalKey.name.toInt()
        }
    }

    override suspend fun updateNotificationsEnables(boolean: Boolean) {
        context.dataStore.edit { settings ->
            settings[notificationsEnableKey] = boolean
        }
    }

    override suspend fun updateWifiOnly(boolean: Boolean) {
        context.dataStore.edit { settings ->
            settings[wifiOnlyKey] = boolean
        }
    }
}