package com.github.kolesovv.news.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Subscriptions(
    modifier: Modifier = Modifier,
    subscriptions: Map<String, Boolean>,
    isSubscriptionButtonEnabled: Boolean,
    onSubscriptionClick: (String) -> Unit,
    onDeleteSubscriptionClick: (String) -> Unit,
    onAddSubscriptionButtonClick: () -> Unit,
) {
    FlowRow(
        modifier = modifier
            .padding(0.dp)
            .wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        subscriptions.entries.toList()
            .forEach { (topic, isSelected) ->
                SubscriptionChip(
                    topic = topic,
                    isSelected = isSelected,
                    onSubscriptionClick = { onSubscriptionClick(topic) },
                    onDeleteSubscription = { onDeleteSubscriptionClick(topic) }
                )
            }
        Button(
            modifier = Modifier,
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 10.dp,
                end = 16.dp,
                bottom = 10.dp
            ),
            onClick = onAddSubscriptionButtonClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            enabled = isSubscriptionButtonEnabled
        ) {
            Text(
                modifier = Modifier,
                text = "Add Topic",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}