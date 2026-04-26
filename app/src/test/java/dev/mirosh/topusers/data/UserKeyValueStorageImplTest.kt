package dev.mirosh.topusers.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import dev.mirosh.topusers.data.local.UserKeyValueStorageImpl
import dev.mirosh.topusers.domain.repository.UserKeyValueStorage
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class FakeDataStore : DataStore<Preferences> {

    private val fakeState = MutableStateFlow(emptyPreferences())

    override val data: Flow<Preferences> = fakeState

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val updated = transform(fakeState.value)
        fakeState.value = updated
        return updated
    }
}

class UserKeyValueStorageImplTest {


    lateinit var storage: UserKeyValueStorage

    @Before
    fun setUpFakes() {
        storage = UserKeyValueStorageImpl(FakeDataStore())
    }

    @After
    fun cleanUp() {

    }

    @Test
    fun `getFollowedUserIds() returns stored id after toggleFollow() was clicked`() = runTest {
        //Arrange
        storage.toggleFollow(9L)

        //Act
        val result = storage.getFollowedUserIds().first()

        //Assert
        assertEquals(setOf(2L), result)
    }
}