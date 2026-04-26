package dev.mirosh.topusers.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import dev.mirosh.topusers.data.local.UserKeyValueStorageImpl
import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.repository.UserKeyValueStorage
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.collections.emptySet

class FakeDataStore : DataStore<Preferences> {

    private val fakeState = MutableStateFlow(emptyPreferences())

    override val data: Flow<Preferences> = fakeState

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val updated = transform(fakeState.value)
        fakeState.value = updated
        return updated
    }
}

class FakeErrorThrowingDataStore : DataStore<Preferences> {
    override val data: Flow<Preferences> = MutableStateFlow(emptyPreferences())
    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences =
        throw androidx.datastore.core.IOException("something happened when writing to the store")
}

class UserKeyValueStorageTest {


    @Before
    fun setUpFakes() {

    }

    @After
    fun cleanUp() {

    }

    @Test
    fun `getFollowedUserIds() returns stored id after toggleFollow() was clicked`() = runTest {
        //Arrange
        val storage: UserKeyValueStorage = UserKeyValueStorageImpl(FakeDataStore())
        storage.toggleFollow(9L)

        //Act
        val result = storage.getFollowedUserIds().first()

        //Assert
        assertEquals(setOf(9L), result)
    }

    @Test
    fun `getFollowedUserIds() returns an empty set when toggleFollow is clicked twice`() = runTest {
        //Arrange
        val storage: UserKeyValueStorage = UserKeyValueStorageImpl(FakeDataStore())
        storage.toggleFollow(9L)
        storage.toggleFollow(9L)

        //Act
        val result = storage.getFollowedUserIds().first()

        //Assert
        assertEquals(emptySet<Long>(), result)
    }

    @Test
    fun `getFollowedUserIds() returns an empty set when toggleFollow is not clicked`() = runTest {
        //Arrange
        val storage: UserKeyValueStorage = UserKeyValueStorageImpl(FakeDataStore())

        //Act
        val result = storage.getFollowedUserIds().first()

        //Assert
        assertEquals(emptySet<Long>(), result)
    }

    @Test
    fun `toggleFollow() does not affect other ids when the same id is toggled`() = runTest {
        //Arrange
        val storage: UserKeyValueStorage = UserKeyValueStorageImpl(FakeDataStore())
        storage.toggleFollow(5L)
        storage.toggleFollow(2L)
        storage.toggleFollow(5L)

        //Act
        val result = storage.getFollowedUserIds().first()

        //Assert
        assertEquals(setOf(2L), result)
    }

    @Test
    fun `toggleFollow() returns an Error`() = runTest {
        //Arrange

        val storage: UserKeyValueStorage = UserKeyValueStorageImpl(FakeErrorThrowingDataStore())

        //Act
        val result = storage.toggleFollow(1L)

        //Assert
        assertEquals(Result.Error, result)
    }
}