package dev.mirosh.topusers.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class UserUiModel(
    val id: Long,
    val displayName: String,
    val reputation: Int,
    val profileImage: String,
    val following: Boolean = false
)