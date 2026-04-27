package dev.mirosh.topusers.ui.main

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
    val users: StateFlow<MainScreenUiState> = observeUsersUseCase()
        .map { result ->
            when (result) {
                is Result.Success -> {
                    val usersUiList = result.data.map {
                        UserUiModel.fromUser(
                            it
                        )
                    }
                    MainScreenUiState.Success(UsersList(usersUiList))
                }

                is Result.Error -> MainScreenUiState.Error
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MainScreenUiState.Loading
        )

    fun toggleFollow(userId: Long) {
        viewModelScope.launch {
            followUserUseCase(userId)
        }
    }
}