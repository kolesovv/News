package com.github.kolesovv.news.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.kolesovv.news.R

@Composable
fun SubscriptionChip(
    modifier: Modifier = Modifier,
    topic: String,
    isSelected: Boolean,
    onSubscriptionClick: (String) -> Unit,
    onDeleteSubscription: (String) -> Unit
) {
    var selected by remember { mutableStateOf(isSelected) }

    FilterChip(
        modifier = modifier,
        onClick = {
            selected = !selected
            onSubscriptionClick(topic)
        },
        label = {
            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = topic,
                maxLines = 1,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        },
        selected = selected,
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = { onDeleteSubscription(topic) }),
                imageVector = Icons.Default.Close,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = stringResource(R.string.icon_delete_subscription)
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.secondary,
            selectedLabelColor = MaterialTheme.colorScheme.onBackground,
            containerColor = MaterialTheme.colorScheme.background,
            labelColor = MaterialTheme.colorScheme.onSurface
        )
    )
}