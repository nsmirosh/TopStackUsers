package dev.mirosh.topusers.data.repository

import android.util.Log
import dev.mirosh.topusers.data.network.StackExchangeApi
import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.model.User
import dev.mirosh.topusers.domain.repository.UserKeyValueStorage
import dev.mirosh.topusers.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val stackExchangeApi: StackExchangeApi,
    private val userKeyValueStorage: UserKeyValueStorage
) : UserRepository {

    override suspend fun toggleFollow(userId: Long): Result<Unit> =
        userKeyValueStorage.toggleFollow(userId)

    override fun observeTopUsers(): Flow<Result<List<User>>> =
        combine(
            getTopUsersFlow(),
            userKeyValueStorage.getFollowedUserIds()
        ) { getTopUsersResult, followedIds ->
            when (getTopUsersResult) {
                is Result.Success -> Result.Success(
                    getTopUsersResult.data.map { user ->
                        user.copy(following = user.id in followedIds)
                    }
                )

                is Result.Error -> getTopUsersResult
            }
        }

    private fun getTopUsersFlow(): Flow<Result<List<User>>> = flow {
        emit(
            try {
                val users = stackExchangeApi.getUsers().items.map { it.toDomain() }
                Result.Success(users)
            } catch (e: Exception) {
                Log.e("MainViewModel", "${e.message}")
                Result.Error
            }
        )
    }
}