package com.github.kolesovv.news.domain.entity

data class Settings(
    val language: Language,
    val interval: Interval,
    val notificationEnable: Boolean,
    val wifiOnly: Boolean
) {
    companion object {
        val DEFAULT_LANGUAGE = Language.ENGLISH
        val DEFAULT_INTERVAL = Interval.MIN_15
        const val DEFAULT_NOTIFICATION = false
        const val DEFAULT_WIFI_ONLY = false
    }
}

enum class Language {

    ENGLISH, RUSSIAN, FRENCH, GERMAN;

    val displayName
        get() = name.lowercase().replaceFirstChar { it.uppercaseChar() }
}


enum class Interval(val label: String, val minutes: Int) {

    MIN_15("15 min", 15),
    MIN_30("30 min", 30),
    HOUR_1("1 hour", 60),
    HOUR_4("4 hour", 240),
    HOUR_8("8 hour", 480),
    HOUR_24("24 hours", 1400)
}
