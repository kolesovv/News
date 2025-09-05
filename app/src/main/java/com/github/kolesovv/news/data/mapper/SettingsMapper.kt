package com.github.kolesovv.news.data.mapper

import com.github.kolesovv.news.domain.entity.Interval
import com.github.kolesovv.news.domain.entity.Language
import com.github.kolesovv.news.domain.entity.RefreshConfig
import com.github.kolesovv.news.domain.entity.Settings


fun Int.toInterval(): Interval {
    return Interval.entries.first { it.minutes == this }
}

fun Settings.toRefreshConfig(): RefreshConfig {
    return RefreshConfig(
        language = this.language,
        interval = this.interval,
        wifiOnly = this.wifiOnly
    )
}

fun Language.toQueryParam(): String {
    return when (this) {
        Language.ENGLISH -> "en"
        Language.RUSSIAN -> "ru"
        Language.FRENCH -> "fr"
        Language.GERMAN -> "de"
    }
}

fun Language.toReadableFormat(): String {
    return when (this) {
        Language.ENGLISH -> "English"
        Language.RUSSIAN -> "Русский"
        Language.FRENCH -> "Français"
        Language.GERMAN -> "Deutsch"
    }
}

fun Interval.toReadableFormat(): String {
    return when (this) {
        Interval.MIN_15 -> "15 min"
        Interval.MIN_30 -> "30 min"
        Interval.HOUR_1 -> "1 hour"
        Interval.HOUR_4 -> "4 hour"
        Interval.HOUR_8 -> "8 hour"
        Interval.HOUR_24 -> "24 hours"
    }
}