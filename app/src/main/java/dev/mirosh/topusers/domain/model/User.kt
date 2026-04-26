package dev.mirosh.topusers.domain.model

data class User(
    val id: Long,
    val displayName: String = "",
    val reputation: Int = 0,
    val profileImage: String = "",
    val following: Boolean = false
)