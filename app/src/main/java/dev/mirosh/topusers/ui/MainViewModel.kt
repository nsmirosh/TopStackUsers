package dev.mirosh.topusers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mirosh.topusers.data.network.StackExchangeApi
import dev.mirosh.topusers.domain.model.User
import dev.mirosh.topusers.ui.model.UsersList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _users = MutableStateFlow(UsersList(listOf()))
    val users: StateFlow<UsersList> = _users.asStateFlow()


    fun fetchUsers() {
        viewModelScope.launch {
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