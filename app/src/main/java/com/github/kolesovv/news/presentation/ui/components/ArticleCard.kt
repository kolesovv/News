package com.github.kolesovv.news.presentation.ui.components

import DateFormater
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.github.kolesovv.news.domain.entity.Article

@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
    topic: String,
    article: Article,
    backgroundColor: Color,
    onButtonReadClick: () -> Unit,
    onButtonShareClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.sourceName,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = DateFormater.formatDateToString(article.publishedAt),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Button(
                onClick = onButtonReadClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(
                    text = "Read",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            IconButton(
                onClick = onButtonShareClick
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Share button"
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = article.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(start = 10.dp, top = 6.dp, end = 10.dp, bottom = 6.dp),
            text = topic,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )

        val loadingState = remember { mutableStateOf(true) }
        if (!loadingState.value) {
            Spacer(modifier = Modifier.height(12.dp))
        }

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .clip(RoundedCornerShape(8.dp)),
            model = article.imageUrl,
            onLoading = { loadingState.value = true },
            onSuccess = { loadingState.value = false },
            contentDescription = "Image's article",
            contentScale = ContentScale.FillWidth
        )
    }
}