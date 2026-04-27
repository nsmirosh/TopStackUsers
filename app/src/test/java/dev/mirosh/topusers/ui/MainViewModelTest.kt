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
import dev.mirosh.topusers.ui.main.UserList
import dev.mirosh.topusers.ui.model.UserUiModel
import dev.mirosh.topusers.ui.model.UsersList
import kotlinx.coroutines.flow.first
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

//    @Test
//    fun test() = runTest {
//        //Arrange
//        val users = listOf(User(1L), User(2L))
//        val expectedResult = UsersList(listOf(UserUiModel(id = 1L), ())
//
//        val followUserUseCase: FollowUserUseCase = mockk(relaxed = true)
//        val observeUsersUseCase: ObserveUsersUseCase = mockk()
//        every { observeUsersUseCase() } returns flowOf(Result.Success(users))
//
//        val viewModel = MainViewModel(followUserUseCase, observeUsersUseCase)
//
//        //Act
//        val result = viewModel.users.first()
//
//
//        //Assert
//        assertEquals(UsersList(users), result)
//
//
//    }
}