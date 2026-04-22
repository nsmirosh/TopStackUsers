package dev.mirosh.topusers.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UsersList(
    val users: List<UserUiModel>
)