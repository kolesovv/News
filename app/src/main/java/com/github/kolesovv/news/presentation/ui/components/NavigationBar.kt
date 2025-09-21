package com.github.kolesovv.news.presentation.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.kolesovv.news.R

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    onClear: () -> Unit,
    onSettings: () -> Unit
) {

    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar(
        modifier = modifier
            .height(90.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = {
                selectedItem = 0
                onRefresh()
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_download),
                    contentDescription = stringResource(R.string.refresh),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.background,
            )
        )
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = {
                selectedItem = 1
                onClear()
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_archive_minus),
                    contentDescription = stringResource(R.string.clear),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.background,
            )
        )
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = {
                selectedItem = 2
                onSettings()
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_setting),
                    contentDescription = stringResource(R.string.settings),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.background,
            )
        )
    }
}