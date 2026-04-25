package dev.mirosh.topusers.domain.repository

import kotlinx.coroutines.flow.Flow

import dev.mirosh.topusers.domain.model.Result

interface UserKeyValueStorage {
    suspend fun followUser(userId: Long): Result<Unit>
    suspend fun unFollowUser(userId: Long): Result<Unit>

    fun getFollowedUserIds(): Flow<Set<String>>
}