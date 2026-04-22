package dev.mirosh.topusers.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mirosh.topusers.data.StackExchangeApi
import dev.mirosh.topusers.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val stackExchangeApi: StackExchangeApi
) : ViewModel() {

//    val stateFlow = flow {
//        fetchUsers()
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = 0
//    )

    private val _users = MutableStateFlow<List<User>>(listOf())
    val users: StateFlow<List<User>> = _users.asStateFlow()


    fun fetchUsers() {
        viewModelScope.launch {
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
                _users.value = userList
            } catch (e: Exception) {
                Log.e("MainViewModel", "${e.message}")
            }
        }
    }

    fun onFollowCLicked(userId: Long) {
        val currentUsers = _users.value
        val userToModify = currentUsers.first { it.id == userId }
//        userToModify.following = !userToModify.following
        currentUsers.indexOf(userToModify)
        _users.value = currentUsers
    }

    //Doing manual parsing to avoid using 3rd party libs here
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