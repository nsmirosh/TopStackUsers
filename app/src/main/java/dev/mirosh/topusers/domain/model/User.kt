package dev.mirosh.topusers.domain.model

data class User(
    val id: Long,
    val displayName: String,
    val reputation: Int,
    val profileImage: String,
)