package dev.mirosh.topusers.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.usecase.FollowUserUseCase
import dev.mirosh.topusers.domain.usecase.ObserveUsersUseCase
import dev.mirosh.topusers.ui.model.UserUiModel
import dev.mirosh.topusers.ui.model.UsersList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val followUserUseCase: FollowUserUseCase,
    observeUsersUseCase: ObserveUsersUseCase,
) : ViewModel() {

    val users: StateFlow<UsersList> = observeUsersUseCase()
        .map { result ->
            when (result) {
                is Result.Success -> UsersList(result.data.map { UserUiModel.fromUser(it) }).also { usersList ->
                    usersList.users.filter { it.following }.forEach {
                        Log.d("MainViewModel", "converted to UI ${it.displayName}")
                    }
                }

                is Result.Error -> UsersList(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UsersList(emptyList())
        )

    fun toggleFollow(userId: Long) {
        //TODO
        //immediately setting the user as followed, and then changing it back to unfolowed
        // in case the operation fails

        viewModelScope.launch {
            followUserUseCase(userId)
        }
    }
}