package dev.mirosh.topusers.data.repository

import android.util.Log
import dev.mirosh.topusers.data.network.StackExchangeApi
import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.model.User
import dev.mirosh.topusers.domain.repository.StackExchangeRepository
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class StackExchangeRepositoryImpl @Inject constructor(
    private val stackExchangeApi: StackExchangeApi
) : StackExchangeRepository {
    override suspend fun getTopUsers(): Result<List<User>> =
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