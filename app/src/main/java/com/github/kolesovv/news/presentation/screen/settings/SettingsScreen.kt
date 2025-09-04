@file:OptIn(ExperimentalMaterial3Api::class)

package com.github.kolesovv.news.presentation.screen.settings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.kolesovv.news.domain.entity.Interval
import com.github.kolesovv.news.domain.entity.Language
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateToSubscriptionScreen: () -> Unit
) {

    val state = viewModel.state.collectAsState()
    val currentState = state.value

    when (currentState) {
        EditSettingsState.Initial -> {}
        is EditSettingsState.Editing -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                            actionIconContentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        navigationIcon = {
                            IconButton(
                                modifier = Modifier
                                    .padding(start = 0.dp, end = 0.dp)
                                /*.border(
                                    1.dp,
                                    MaterialTheme.colorScheme.tertiary,
                                    RoundedCornerShape(10.dp)
                                )
                                .size(52.dp)*/,
                                onClick = onNavigateToSubscriptionScreen,
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                content = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back button"
                                    )
                                }
                            )
                        },
                        title = {
                            Text(
                                modifier = modifier,
                                text = "Settings",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 18.dp)
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    SettingsDescription(
                        title = "Search Language",
                        description = "Select language for news search"
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LanguageRadioButtonGroup(
                        currentLanguage = currentState.settings.language,
                        availableLanguages = Language.entries,
                        onLanguageChange = {
                            viewModel.processCommand(SettingsCommand.ChooseLanguage(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    SettingsDescription(
                        title = "Updated Interval",
                        description = "How often to update news"
                    )
                    Spacer(modifier = Modifier.height(16.dp))


                    NotificationIntervalSlider(
                        selectedInterval = currentState.settings.interval,
                        onIntervalChange = {
                            viewModel.processCommand(SettingsCommand.ChooseInterval(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    SettingsDescription(
                        title = "Notifications",
                        description = "Show notifications about new articles"
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Switch(
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            checkedTrackColor = MaterialTheme.colorScheme.secondary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.tertiary
                        ),
                        checked = currentState.settings.notificationEnable,
                        onCheckedChange = {
                            viewModel.processCommand(SettingsCommand.SwitchNotificationEnable)
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    SettingsDescription(
                        title = "Update only via Wi-Fi",
                        description = "Save mobile data"
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Switch(
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            checkedTrackColor = MaterialTheme.colorScheme.secondary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.tertiary
                        ),
                        checked = currentState.settings.wifiOnly,
                        onCheckedChange = {
                            viewModel.processCommand(SettingsCommand.SwitchWifiOnly)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsDescription(
    modifier: Modifier = Modifier,
    title: String,
    description: String
) {
    Text(
        modifier = modifier,
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        modifier = modifier,
        text = description,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.secondary,
    )
}

@Composable
fun LanguageRadioButtonGroup(
    modifier: Modifier = Modifier,
    currentLanguage: Language,
    availableLanguages: List<Language>,
    onLanguageChange: (Language) -> Unit
) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(currentLanguage) }

    Column(
        modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        availableLanguages.forEach { language ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (language == selectedOption),
                        onClick = {
                            onOptionSelected(language)
                            onLanguageChange(language)
                        },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (language == selectedOption),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unselectedColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = language.displayName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun NotificationIntervalSlider(
    selectedInterval: Interval,
    onIntervalChange: (Interval) -> Unit
) {
    Log.d("SettingsScreen", "NotificationIntervalSlider: ${selectedInterval.ordinal.toFloat()}")
    val intervals: Array<Interval> = Interval.entries.toTypedArray()
    var sliderPosition by remember { mutableFloatStateOf(selectedInterval.ordinal.toFloat()) }

    Column {
        Slider(
            value = sliderPosition,
            onValueChange = {
                val index = it.roundToInt().coerceIn(intervals.indices)
                sliderPosition = it
                onIntervalChange(intervals[index])
            },
            thumb = {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            shape = CircleShape,
                        )
                )
            },
            colors = SliderDefaults.colors(
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.tertiary,
                inactiveTickColor = MaterialTheme.colorScheme.secondary,
            ),
            steps = Interval.entries.size - 2,
            valueRange = 0f..(Interval.entries.size - 1).toFloat()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            intervals.forEach {
                Text(
                    modifier = Modifier.weight(1f),
                    text = it.label,
                    style = if (it == selectedInterval) {
                        TextStyle(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    } else {
                        TextStyle(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                )
            }
        }
    }
}