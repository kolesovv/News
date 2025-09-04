package com.github.kolesovv.news.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kolesovv.news.domain.entity.Interval
import com.github.kolesovv.news.domain.entity.Language
import com.github.kolesovv.news.domain.entity.Settings
import com.github.kolesovv.news.domain.usecase.GetSettingsUseCase
import com.github.kolesovv.news.domain.usecase.UpdateIntervalUseCase
import com.github.kolesovv.news.domain.usecase.UpdateLanguageUseCase
import com.github.kolesovv.news.domain.usecase.UpdateNotificationsEnablesUseCase
import com.github.kolesovv.news.domain.usecase.UpdateWifiOnlyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val updateIntervalUseCase: UpdateIntervalUseCase,
    private val updateNotificationsEnablesUseCase: UpdateNotificationsEnablesUseCase,
    private val updateWifiOnlyUseCase: UpdateWifiOnlyUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<EditSettingsState>(EditSettingsState.Initial)
    val state = _state.asStateFlow()

    init {
        observeSettings()
    }

    fun processCommand(settingsCommand: SettingsCommand) {
        when (settingsCommand) {
            is SettingsCommand.ChooseInterval -> {
                viewModelScope.launch {
                    updateIntervalUseCase(settingsCommand.interval)
                    _state.update { previousState ->
                        if (previousState is EditSettingsState.Editing) {
                            val newSettings =
                                previousState.settings.copy(interval = settingsCommand.interval)
                            previousState.copy(newSettings)
                        } else {
                            previousState
                        }
                    }
                }
            }

            is SettingsCommand.ChooseLanguage -> {
                viewModelScope.launch {
                    updateLanguageUseCase(settingsCommand.language)
                    _state.update { previousState ->
                        if (previousState is EditSettingsState.Editing) {
                            val newSettings =
                                previousState.settings.copy(language = settingsCommand.language)
                            previousState.copy(newSettings)
                        } else {
                            previousState
                        }
                    }
                }
            }

            SettingsCommand.SwitchNotificationEnable -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is EditSettingsState.Editing) {
                            val isEnabled = !previousState.isNotificationEnable
                            updateNotificationsEnablesUseCase(isEnabled)
                            val newSettings =
                                previousState.settings.copy(notificationEnable = isEnabled)
                            previousState.copy(newSettings)
                        } else {
                            previousState
                        }
                    }
                }
            }

            SettingsCommand.SwitchWifiOnly -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is EditSettingsState.Editing) {
                            val isEnabled = !previousState.isWifiOnly
                            updateNotificationsEnablesUseCase(isEnabled)
                            val newSettings =
                                previousState.settings.copy(wifiOnly = isEnabled)
                            previousState.copy(newSettings)
                        } else {
                            previousState
                        }
                    }
                }
            }
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            val actualSettings = getSettingsUseCase().first()
            _state.update {
                val settings = Settings(
                    language = actualSettings.language,
                    interval = actualSettings.interval,
                    notificationEnable = actualSettings.notificationEnable,
                    wifiOnly = actualSettings.wifiOnly
                )
                EditSettingsState.Editing(settings)
            }
        }
    }
}

sealed interface SettingsCommand {

    data class ChooseLanguage(val language: Language) : SettingsCommand

    data class ChooseInterval(val interval: Interval) : SettingsCommand

    data object SwitchNotificationEnable : SettingsCommand

    data object SwitchWifiOnly : SettingsCommand


}

data class SettingsState(
    val language: Language = Settings.DEFAULT_LANGUAGE,
    val interval: Interval = Settings.DEFAULT_INTERVAL,
    val notificationEnable: Boolean = Settings.DEFAULT_NOTIFICATION,
    val wifiOnly: Boolean = Settings.DEFAULT_WIFI_ONLY
)

sealed interface EditSettingsState {

    data object Initial : EditSettingsState

    data class Editing(val settings: Settings) : EditSettingsState {

        val isNotificationEnable: Boolean
            get() = settings.notificationEnable

        val isWifiOnly: Boolean
            get() = settings.wifiOnly
    }
}