package dev.mirosh.topusers.domain.repository

import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeTopUsers(): Flow<Result<List<User>>>
    suspend fun toggleFollow(userId: Long): Result<Unit>
}