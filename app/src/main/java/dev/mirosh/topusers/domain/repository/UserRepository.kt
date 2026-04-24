package dev.mirosh.topusers.domain.repository

import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.model.User

interface UserRepository {
    suspend fun getTopUsers(): Result<List<User>>
    suspend fun unFollowUser(userId: Long): Result<Unit>
    suspend fun followUser(userId: Long): Result<Unit>
}