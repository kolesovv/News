package com.github.kolesovv.news.presentation.screen.subscriptions


import android.content.Intent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.kolesovv.news.presentation.ui.components.ArticleCard
import com.github.kolesovv.news.presentation.ui.components.NavigationBar
import com.github.kolesovv.news.presentation.ui.components.SearchBar
import com.github.kolesovv.news.presentation.ui.components.Subscriptions

@Composable
fun SubscriptionsScreen(
    modifier: Modifier = Modifier,
    viewModel: SubscriptionsViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit
) {

    val state = viewModel.state.collectAsState()
    val currentState = state.value
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            NavigationBar(
                onRefresh = { viewModel.processCommand(SubscriptionsCommand.RefreshData) },
                onClear = { viewModel.processCommand(SubscriptionsCommand.ClearArticles) },
                onSettings = onNavigateToSettings
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(horizontal = 24.dp),
            contentPadding = innerPadding
        ) {
            item {
                SearchBar(
                    modifier = modifier,
                    query = currentState.query,
                    onQueryChange = {
                        viewModel.processCommand(SubscriptionsCommand.InputTopic(it))
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Subscriptions(
                    subscriptions = currentState.subscriptions,
                    isSubscriptionButtonEnabled = currentState.subscriptionButtonEnabled,
                    onSubscriptionClick = {
                        viewModel.processCommand(
                            SubscriptionsCommand.ToggleTopicSelection(it)
                        )
                    },
                    onDeleteSubscriptionClick = {
                        viewModel.processCommand(
                            SubscriptionsCommand.RemoveSubscription(it)
                        )
                    },
                    onAddSubscriptionButtonClick = {
                        viewModel.processCommand(SubscriptionsCommand.ClickSubscribe)
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Text(
                    modifier = modifier,
                    text = "News",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    modifier = modifier,
                    text = "${currentState.articles.size} Results found:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            items(currentState.articles) { article ->
                ArticleCard(
                    article = article,
                    onButtonReadClick = {
                        val intent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                        context.startActivity(intent)
                    },
                    onButtonShareClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            val message =
                                "I would like to share with you an interesting article: \"${article.title}\". You can read it here: ${article.url}"
                            putExtra(Intent.EXTRA_TEXT, message)
                        }
                        context.startActivity(intent)
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}