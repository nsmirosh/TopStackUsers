package dev.mirosh.topusers.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UsersResponseDto(
    val items: List<UserDto> = emptyList()
)