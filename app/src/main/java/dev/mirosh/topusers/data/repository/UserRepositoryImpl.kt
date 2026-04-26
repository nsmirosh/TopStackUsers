package dev.mirosh.topusers.data.repository

import android.util.Log
import dev.mirosh.topusers.data.network.StackExchangeApi
import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.model.User
import dev.mirosh.topusers.domain.repository.UserKeyValueStorage
import dev.mirosh.topusers.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import org.json.JSONObject
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
                    }.also {
                        Log.d("UserRepositoryImpl", "emitting list size ${it.size}")
                        Log.d(
                            "UserRepositoryImpl", "user being followed == ${
                                it.filter { user -> user.following }
                                    .map { user -> user.displayName }
                            }")

                    }
                )

                is Result.Error -> getTopUsersResult
            }
        }

    private fun getTopUsersFlow(): Flow<Result<List<User>>> = flow {
        emit(
            try {
                val response = stackExchangeApi.getUsers()
                val users = JSONObject(response.string()).getJSONArray("items")
                Log.d("MainViewModel", "response = $response")
                val userList = mutableListOf<User>()

                for (i in 0 until users.length()) {
                    val userJson = users.getJSONObject(i)
                    try {
                        val parsedUser = parseUser(userJson)
                        Log.d("MainViewModel", "parsedUser = $parsedUser")
                        userList.add(parsedUser)
                    } catch (exception: JSONException) {
                        // if we can't get the id of the user - we'll skip over this user
                        // but we'll continue parsing the rest
                        Log.e("MainViewModel", "exception = ${exception.message}")
                    }
                }
                Result.Success(userList)
            } catch (e: Exception) {
                Log.e("MainViewModel", "${e.message}")
                Result.Error
            }
        )
    }


    //TODO remove this and substitute with some lib
//Doing manual parsing to avoid using 3rd party libs here
//Also, parsing straight to the User instead of the DTO, cause I don't see the point
// if I'm doing manual parsing anyway
    private fun parseUser(userJson: JSONObject) = with(userJson) {
        User(
            displayName = optString("display_name"),
            profileImage = optString("profile_image"),
            reputation = optInt("reputation"),
            // avoiding optional here - cause we won't be able to do anything without the id
            id = getLong("user_id")
        )
    }
}