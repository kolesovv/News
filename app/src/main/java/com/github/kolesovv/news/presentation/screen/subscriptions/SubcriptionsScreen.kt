package com.github.kolesovv.news.presentation.screen.subscriptions


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.kolesovv.news.R
import com.github.kolesovv.news.domain.entity.Article

@Composable
fun SubscriptionsScreen(
    modifier: Modifier = Modifier,
    viewModel: SubscriptionsViewModel = hiltViewModel(),
    onButtonSettingsClick: () -> Unit
) {

    val state = viewModel.state.collectAsState()
    val currentState = state.value

    Scaffold() { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding
        ) {
            item {
                SearchBar(
                    query = currentState.query,
                    onQueryChange = {
                        viewModel.processCommand(SubscriptionsCommand.InputTopic(it))
                    }
                )
            }
            items(currentState.articles) { article ->
                ArticleCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    article = article,
                    backgroundColor = MaterialTheme.colorScheme.surfaceBright,
                    onButtonReadClick = { },
                    onButtonShareClick = { }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .border(
                shape = RoundedCornerShape(10.dp),
                width = 0.dp,
                color = Color.Transparent
            ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search \"News\"",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Search icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
    article: Article,
    backgroundColor: Color,
    onButtonReadClick: () -> Unit,
    onButtonShareClick: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.sourceName
                )
                Text(
                    text = article.publishedAt.toString()
                )
            }
            Button(
                onClick = onButtonReadClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.surfaceDim
                )
            ) {
                Text(
                    text = "Read",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}