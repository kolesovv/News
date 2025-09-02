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

    ENGLISH, RUSSIAN, FRENCH, GERMAN
}


enum class Interval(val minutes: Int) {

    MIN_15(15),
    MIN_30(30),
    HOUR_1(60),
    HOUR_4(240),
    HOUR_8(480),
    HOUR_24(1400)
}
