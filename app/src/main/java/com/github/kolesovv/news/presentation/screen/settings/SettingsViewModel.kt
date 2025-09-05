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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        viewModelScope.launch {
            when (settingsCommand) {
                is SettingsCommand.ChooseInterval -> {
                    updateIntervalUseCase(settingsCommand.interval)
                }

                is SettingsCommand.ChooseLanguage -> {
                    updateLanguageUseCase(settingsCommand.language)
                }

                is SettingsCommand.SwitchNotificationEnable -> {
                    updateNotificationsEnablesUseCase(settingsCommand.boolean)
                }

                is SettingsCommand.SwitchWifiOnly -> {
                    updateWifiOnlyUseCase(settingsCommand.boolean)
                }
            }
        }
    }

    private fun observeSettings() {
        getSettingsUseCase().onEach { actualSettings ->
            _state.update {
                val settings = Settings(
                    language = actualSettings.language,
                    interval = actualSettings.interval,
                    notificationEnable = actualSettings.notificationEnable,
                    wifiOnly = actualSettings.wifiOnly
                )
                EditSettingsState.Editing(settings)
            }
        }.launchIn(viewModelScope)
    }
}

sealed interface SettingsCommand {

    data class ChooseLanguage(val language: Language) : SettingsCommand

    data class ChooseInterval(val interval: Interval) : SettingsCommand

    data class SwitchNotificationEnable(val boolean: Boolean) : SettingsCommand

    data class SwitchWifiOnly(val boolean: Boolean) : SettingsCommand


}

sealed interface EditSettingsState {

    data object Initial : EditSettingsState

    data class Editing(
        val settings: Settings,
        val language: List<Language> = Language.entries,
        val interval: List<Interval> = Interval.entries
    ) : EditSettingsState
}