package dev.mirosh.topusers.domain

import androidx.compose.runtime.Immutable

@Immutable
data class User(
    val id: Long,
    val displayName: String,
    val reputation: Int,
    val profileImage: String,
    val following: Boolean = false
)