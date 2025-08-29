package com.github.kolesovv.news.presentation.screen.subscriptions


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.kolesovv.news.presentation.ui.components.ArticleCard
import com.github.kolesovv.news.presentation.ui.components.FilterChip
import com.github.kolesovv.news.presentation.ui.components.NavigationBar
import com.github.kolesovv.news.presentation.ui.components.SearchBar

@Composable
fun SubscriptionsScreen(
    modifier: Modifier = Modifier,
    viewModel: SubscriptionsViewModel = hiltViewModel(),
    onButtonSettingsClick: () -> Unit
) {

    val state = viewModel.state.collectAsState()
    val currentState = state.value

    Scaffold(
        bottomBar = {
            NavigationBar(
                onRefresh = { viewModel.processCommand(SubscriptionsCommand.RefreshData) },
                onClear = { viewModel.processCommand(SubscriptionsCommand.ClearArticles) },
                onSettings = { }
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
                FlowRow(
                    modifier = Modifier
                        .padding(0.dp)
                        .wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    currentState.subscriptions.entries.toList()
                        .forEach { (topic, isSelected) ->
                            FilterChip(
                                topic = topic,
                                isSelected = isSelected,
                                onChipClick = {
                                    viewModel.processCommand(
                                        SubscriptionsCommand.ToggleTopicSelection(
                                            topic
                                        )
                                    )
                                }
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
                        onClick = {
                            viewModel.processCommand(SubscriptionsCommand.ClickSubscribe)
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "Add Topic",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                }
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
                    modifier = modifier.fillMaxWidth(),
                    topic = article.topic,
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