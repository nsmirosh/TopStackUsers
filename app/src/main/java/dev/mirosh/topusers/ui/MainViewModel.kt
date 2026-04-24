package dev.mirosh.topusers.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.repository.KeyValueStorage
import dev.mirosh.topusers.domain.usecase.FollowUserUseCase
import dev.mirosh.topusers.domain.usecase.GetTopUsersUseCase
import dev.mirosh.topusers.ui.model.UserUiModel
import dev.mirosh.topusers.ui.model.UsersList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTopUsersUseCase: GetTopUsersUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val keyValueStorage: KeyValueStorage
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
            when (val result = getTopUsersUseCase()) {
                is Result.Success -> {
                    _users.value = UsersList(result.data.map { UserUiModel.fromUser(it) })
                }

                is Result.Error -> {
                    //display error
                }

            }
        }
    }

    fun onFollowCLicked(userId: Long) {
        viewModelScope.launch {
            followUserUseCase()

            keyValueStorage.counterFlow().collect {
                Log.d("UserRepositoryImpl", "$it")
            }
        }

//        val currentUsers = _users.value
//        val userToModify = currentUsers.first { it.id == userId }
//        userToModify.following = !userToModify.following
//        currentUsers.indexOf(userToModify)
//        _users.value = currentUsers
    }


}