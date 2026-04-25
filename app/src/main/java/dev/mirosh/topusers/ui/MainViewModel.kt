package dev.mirosh.topusers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.usecase.FollowUserUseCase
import dev.mirosh.topusers.domain.usecase.GetTopUsersUseCase
import dev.mirosh.topusers.domain.usecase.ObserveUsersUseCase
import dev.mirosh.topusers.ui.model.UserUiModel
import dev.mirosh.topusers.ui.model.UsersList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
//    private val getTopUsersUseCase: GetTopUsersUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val observeUsersUseCase: ObserveUsersUseCase,
) : ViewModel() {

    val users: StateFlow<UsersList> = observeUsersUseCase()
        .map { result ->
            when (result) {
                is Result.Success -> UsersList(result.data.map { UserUiModel.fromUser(it) })
                is Result.Error -> UsersList(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UsersList(emptyList())
        )

//    private val _usersFlow = MutableStateFlow(UsersList(listOf()))
//    val users: StateFlow<UsersList> = _usersFlow.asStateFlow()


//    fun fetchUsers() {
//        viewModelScope.launch {
//            when (val result = getTopUsersUseCase()) {
//                is Result.Success -> {
//                    _usersFlow.value = UsersList(result.data.map { UserUiModel.fromUser(it) })
//                }
//
//                is Result.Error -> {
//                    //display error
//                }
//
//            }
//        }
//    }

    fun toggleFollow(userId: Long) {

        //TODO
        //immediately setting the user as followed, and then changing it back to unfolowed
        // in case the operation fails

        viewModelScope.launch {
            when (val result = followUserUseCase(userId)) {
                is Result.Success -> {

//                    val oldList = _usersFlow.value
//
//                    val listCopy = UsersList(
//                        oldList.users.map {
//                            if (it.id == userId) it.copy(following = true) else it
//                        })
//
//                    _usersFlow.value = listCopy
                }

                is Result.Error -> {
                    //reverting to the old list in case the operation failed
//                    _usersFlow.value = oldList

                }
            }

        }

//        val currentUsers = _users.value
//        val userToModify = currentUsers.first { it.id == userId }
//        userToModify.following = !userToModify.following
//        currentUsers.indexOf(userToModify)
//        _users.value = currentUsers
    }


}