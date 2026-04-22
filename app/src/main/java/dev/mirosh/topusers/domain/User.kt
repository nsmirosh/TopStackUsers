package dev.mirosh.topusers.domain

data class User(
    val id: Long,
    val displayName: String,
    val reputation: Int,
    val profileImage: String,
)