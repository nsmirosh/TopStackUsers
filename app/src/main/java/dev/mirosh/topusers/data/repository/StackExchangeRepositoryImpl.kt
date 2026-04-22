package dev.mirosh.topusers.data.repository

import android.util.Log
import dev.mirosh.topusers.data.model.UserDTO
import dev.mirosh.topusers.data.network.StackExchangeApi
import dev.mirosh.topusers.domain.repository.StackExchangeRepository

import dev.mirosh.topusers.domain.model.User
import dev.mirosh.topusers.ui.model.UsersList
import org.json.JSONException
import org.json.JSONObject

class StackExchangeRepositoryImpl(
    private val stackExchangeApi: StackExchangeApi
) : StackExchangeRepository {
    override suspend fun getTopUsers(): List<UserDTO> {
        try {
            val response = stackExchangeApi.getUsers()
            val users = JSONObject(response.string()).getJSONArray("items")
            Log.d("MainViewModel", "response = $response")
            val userList: List<User> = mutableListOf()

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

            _users.value = UsersList(userList.to)
        } catch (e: Exception) {
            Log.e("MainViewModel", "${e.message}")
        }
    }
}