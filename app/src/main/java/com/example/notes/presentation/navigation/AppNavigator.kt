package com.example.notes.presentation.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

sealed interface NavigationAction {
    data class Navigate(val screen: Screen) : NavigationAction
    data object NavigateBack : NavigationAction
}

class AppNavigator {

    private val _actions = Channel<NavigationAction>(Channel.BUFFERED)
    val actions: Flow<NavigationAction> = _actions.receiveAsFlow()

    fun navigate(screen: Screen) {
        _actions.trySend(NavigationAction.Navigate(screen))
    }

    fun navigateBack() {
        _actions.trySend(NavigationAction.NavigateBack)
    }
}
