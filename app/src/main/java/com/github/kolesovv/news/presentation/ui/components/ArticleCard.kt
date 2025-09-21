package com.github.kolesovv.news.presentation.ui.components

import DateFormater
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.github.kolesovv.news.R
import com.github.kolesovv.news.domain.entity.Article

@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
    article: Article,
    onButtonReadClick: () -> Unit,
    onButtonShareClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                        text = stringResource(R.string.read),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
                IconButton(
                    onClick = onButtonShareClick
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.share_button)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = article.title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
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
                text = article.topic,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )

            article.imageUrl?.let {
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
                    contentDescription = stringResource(R.string.images_article),
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}