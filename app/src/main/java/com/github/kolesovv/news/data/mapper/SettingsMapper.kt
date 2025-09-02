package com.github.kolesovv.news.data.mapper

import com.github.kolesovv.news.domain.entity.Interval
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