@file:OptIn(ExperimentalCoroutinesApi::class)

package com.github.kolesovv.news.presentation.screen.subscriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kolesovv.news.domain.entity.Article
import com.github.kolesovv.news.domain.usecase.AddSubscriptionUseCase
import com.github.kolesovv.news.domain.usecase.ClearAllArticlesUseCase
import com.github.kolesovv.news.domain.usecase.GetAllSubscriptionsUseCase
import com.github.kolesovv.news.domain.usecase.GetArticlesForTopicsUseCase
import com.github.kolesovv.news.domain.usecase.RemoveSubscriptionUseCase
import com.github.kolesovv.news.domain.usecase.UpdateArticlesForAllSubscriptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    private val addSubscriptionUseCase: AddSubscriptionUseCase,
    private val clearAllArticlesUseCase: ClearAllArticlesUseCase,
    private val getAllSubscriptionsUseCase: GetAllSubscriptionsUseCase,
    private val getArticlesForTopicsUseCase: GetArticlesForTopicsUseCase,
    private val removeSubscriptionUseCase: RemoveSubscriptionUseCase,
    private val updateArticlesForAllSubscriptionUseCase: UpdateArticlesForAllSubscriptionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SubscriptionsState())
    val state = _state.asStateFlow()

    init {
        observeSubscriptions()
        observeSelectedTopics()
    }

    fun processCommand(command: SubscriptionsCommand) {
        when (command) {
            SubscriptionsCommand.ClearArticles -> {
                viewModelScope.launch {
                    val topics = _state.value.selectedTopics
                    clearAllArticlesUseCase(topics)
                }
            }

            SubscriptionsCommand.ClickSubscribe -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        val topic = _state.value.query.trim()
                        addSubscriptionUseCase(topic)
                        previousState.copy(query = "")
                    }
                }
            }

            is SubscriptionsCommand.InputTopic -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        previousState.copy(query = command.query)
                    }
                }
            }

            SubscriptionsCommand.RefreshData -> {
                viewModelScope.launch {
                    updateArticlesForAllSubscriptionUseCase()
                }
            }

            is SubscriptionsCommand.RemoveSubscription -> {
                viewModelScope.launch {
                    removeSubscriptionUseCase(command.topic)
                }
            }

            is SubscriptionsCommand.ToggleTopicSelection -> {
                _state.update { previousState ->
                    val subscriptions = previousState.subscriptions.toMutableMap()
                    val isSelected = subscriptions[command.topic]
                    isSelected?.let { subscriptions[command.topic] = !it }
                    previousState.copy(subscriptions = subscriptions)
                }
            }
        }
    }

    private fun observeSelectedTopics() {
        _state.map { it.selectedTopics }
            .distinctUntilChanged()
            .flatMapLatest { topics ->
                getArticlesForTopicsUseCase(topics)
            }.onEach { articles ->
                _state.update { previousState ->
                    previousState.copy(articles = articles)
                }
            }.launchIn(viewModelScope)
    }

    private fun observeSubscriptions() {
        getAllSubscriptionsUseCase().onEach { subscriptions ->
            _state.update { previousState ->
                val updatedTopics = subscriptions.associateWith { topic ->
                    previousState.subscriptions[topic] ?: true
                }
                previousState.copy(subscriptions = updatedTopics)
            }
        }.launchIn(viewModelScope)
    }
}

sealed interface SubscriptionsCommand {

    data class InputTopic(val query: String) : SubscriptionsCommand

    data object ClickSubscribe : SubscriptionsCommand

    data object RefreshData : SubscriptionsCommand

    data class ToggleTopicSelection(val topic: String) : SubscriptionsCommand

    data object ClearArticles : SubscriptionsCommand

    data class RemoveSubscription(val topic: String) : SubscriptionsCommand
}

data class SubscriptionsState(
    val query: String = "",
    val subscriptions: Map<String, Boolean> = mapOf(),
    val articles: List<Article> = listOf()
) {

    val subscriptionButtonEnabled: Boolean
        get() = query.isNotBlank()

    val selectedTopics: List<String>
        get() = subscriptions.filter { it.value }.map { it.key }
}