package dev.mirosh.topusers.data

import dev.mirosh.topusers.data.model.UserDto
import dev.mirosh.topusers.data.model.UsersResponseDto
import dev.mirosh.topusers.data.network.StackExchangeApi
import dev.mirosh.topusers.data.repository.UserRepositoryImpl
import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.model.User
import dev.mirosh.topusers.domain.repository.UserKeyValueStorage
import dev.mirosh.topusers.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

const val FOLLOWED_USER_ID = 5L

class FakeKeyValueWithFollowedIds : UserKeyValueStorage {
    override suspend fun toggleFollow(userId: Long): Result<Unit> {
        return Result.Success(Unit)
    }

    override fun getFollowedUserIds(): Flow<Set<Long>> =
        flow {
            emit(setOf(4L, FOLLOWED_USER_ID, 6L))
        }
}

class FakeEmptyKeyValueStorage : UserKeyValueStorage {
    override suspend fun toggleFollow(userId: Long): Result<Unit> {
        return Result.Success(Unit)
    }

    override fun getFollowedUserIds(): Flow<Set<Long>> =
        flow {
            emit(setOf())
        }
}

class FakeStackExchangeApi : StackExchangeApi {

    val usersResponseDto = UsersResponseDto(
        items = listOf(
            UserDto(10L),
            UserDto(FOLLOWED_USER_ID)
        )
    )

    override suspend fun getUsers() = usersResponseDto
}

class ThrowableStackExchangeApi : StackExchangeApi {

    override suspend fun getUsers(): UsersResponseDto {
        throw IOException()
    }
}

class UserRepositoryTest {

    @Test
    fun `toggleFollow() returns a successful Result`() = runTest {
        //Arrange
        val repository: UserRepository = UserRepositoryImpl(
            FakeStackExchangeApi(),
            FakeKeyValueWithFollowedIds()
        )

        //Act
        val result = repository.toggleFollow(1L)


        //Assert
        assertEquals(Result.Success(Unit), result)
    }

    @Test
    fun `observeTopUsers() successfully merges Users from the api and followed ids`() = runTest {
        //Arrange
        val repository: UserRepository = UserRepositoryImpl(
            FakeStackExchangeApi(),
            FakeKeyValueWithFollowedIds()
        )

        //Act
        val result: Result<List<User>> = repository.observeTopUsers().first()

        //Assert
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        val followedUser = data.first { it.following }
        assertEquals(FOLLOWED_USER_ID, followedUser.id)
    }

    @Test
    fun `observeTopUsers() returns Error when api throws exception`() = runTest {
        //Arrange
        val repository: UserRepository = UserRepositoryImpl(
            ThrowableStackExchangeApi(),
            FakeKeyValueWithFollowedIds()
        )

        //Act
        val result = repository.observeTopUsers().first()

        //Assert
        assertTrue(result is Result.Error)
    }

    @Test
    fun `observeTopUsers() still emits users even if the followed ids is empty`() = runTest {
        //Arrange
        val repository: UserRepository = UserRepositoryImpl(
            FakeStackExchangeApi(),
            FakeEmptyKeyValueStorage()
        )

        val expectedUsers = listOf(
            User(10L),
            User(FOLLOWED_USER_ID)
        )

        //Act
        val result = repository.observeTopUsers().first()

        //Assert
        assertTrue(result is Result.Success)

        assertEquals(expectedUsers, (result as Result.Success).data)
    }

}