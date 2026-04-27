package dev.mirosh.topusers.ui.main

import dev.mirosh.topusers.ui.model.UsersList

sealed interface MainScreenUiState {

    data object Error : MainScreenUiState

    data object Loading : MainScreenUiState

    data class Success(
        val usersList: UsersList,
    ) : MainScreenUiState
}
