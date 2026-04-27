package dev.mirosh.topusers.ui

import dev.mirosh.topusers.domain.usecase.FollowUserUseCase
import dev.mirosh.topusers.domain.usecase.ObserveUsersUseCase
import dev.mirosh.topusers.ui.main.MainViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.model.User
import dev.mirosh.topusers.ui.model.UserUiModel
import dev.mirosh.topusers.ui.model.UsersList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals


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
    fun `MainViewModel users flow emits an empty result, followed by mapped Users to UserUiModel `() =
        runTest {
            //Arrange
            val users = listOf(User(5L, reputation = 125398), User(7L, reputation = 31090))
            val expectedResult = UsersList(listOf())
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
            val result = mutableListOf<UsersList>()
            viewModel.users.take(2).toList(result)

            //Assert
            assertEquals(expectedResult, result[0])
            assertEquals(expectedResult2, result[1])
        }

}