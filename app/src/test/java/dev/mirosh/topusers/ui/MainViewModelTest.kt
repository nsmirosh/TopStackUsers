package dev.mirosh.topusers.ui

import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.model.User
import dev.mirosh.topusers.domain.usecase.FollowUserUseCase
import dev.mirosh.topusers.domain.usecase.ObserveUsersUseCase
import dev.mirosh.topusers.ui.main.MainScreenUiState
import dev.mirosh.topusers.ui.main.MainViewModel
import dev.mirosh.topusers.ui.model.UserUiModel
import dev.mirosh.topusers.ui.model.UsersList
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `MainViewModel uiState flow emits Loading state, followed by Success`() =
        runTest {
            //Arrange
            val users = listOf(User(5L, reputation = 125398), User(7L, reputation = 31090))
            val expectedResult1 = MainScreenUiState.Loading
            val expectedResult2 =
                UsersList(
                    listOf(
                        UserUiModel(id = 5L, reputation = "125k"),
                        UserUiModel(id = 7L, reputation = "31k")
                    )
                )

            val followUserUseCase: FollowUserUseCase = mockk(relaxed = true)
            val observeUsersUseCase: ObserveUsersUseCase = mockk()
            every { observeUsersUseCase() } returns flowOf(Result.Success(users))

            val viewModel = MainViewModel(followUserUseCase, observeUsersUseCase)
            advanceUntilIdle()

            //Act
            val result = mutableListOf<MainScreenUiState>()
            viewModel.users.take(2).toList(result)

            //Assert
            assertEquals(expectedResult1, result[0])
            assertTrue(result[1] is MainScreenUiState.Success)
            val successResult = result[1] as MainScreenUiState.Success
            assertEquals(expectedResult2, successResult.usersList)
        }


    @Test
    fun `MainViewModel uiState flow emits Loading state, followed by Error`() =
        runTest {
            //Arrange
            val expectedResult1 = MainScreenUiState.Loading
            val followUserUseCase: FollowUserUseCase = mockk(relaxed = true)
            val observeUsersUseCase: ObserveUsersUseCase = mockk()
            every { observeUsersUseCase() } returns flowOf(Result.Error)

            val viewModel = MainViewModel(followUserUseCase, observeUsersUseCase)
            advanceUntilIdle()

            //Act
            val result = mutableListOf<MainScreenUiState>()
            viewModel.users.take(2).toList(result)

            //Assert
            assertEquals(expectedResult1, result[0])
            assertTrue(result[1] is MainScreenUiState.Error)
        }

    @Test
    fun `toggleFollow() invokes followUserUseCase with Success`() =
        runTest {
            //Arrange
            val followUserUseCase: FollowUserUseCase = mockk()
            val observeUsersUseCase: ObserveUsersUseCase = mockk(relaxed = true)
            val viewModel = MainViewModel(followUserUseCase, observeUsersUseCase)

            coEvery { followUserUseCase(any()) } returns Result.Success(Unit)

            //Act
            viewModel.toggleFollow(123L)
            advanceUntilIdle()

            //Assert
            coVerify { followUserUseCase(123L) }
        }

    @Test
    fun `toggleFollow() invokes followUserUseCase with Error and emits event`() =
        runTest {
            //Arrange
            val followUserUseCase: FollowUserUseCase = mockk()
            val observeUsersUseCase: ObserveUsersUseCase = mockk(relaxed = true)
            val viewModel = MainViewModel(followUserUseCase, observeUsersUseCase)
            coEvery { followUserUseCase(any()) } returns Result.Error

            //Act
            viewModel.toggleFollow(123L)
            val result = viewModel.followFailedEvent.first()
            advanceUntilIdle()

            //Assert
            coVerify { followUserUseCase(123L) }
            assertEquals(Unit, result)
        }


}