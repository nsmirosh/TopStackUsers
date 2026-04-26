package dev.mirosh.topusers.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.repository.UserKeyValueStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


const val TAG = "KeyValueStorageImpl"

class UserKeyValueStorageImpl @Inject constructor(
    private val topUsersDataStore: DataStore<Preferences>
) : UserKeyValueStorage {

    override fun getFollowedUserIds(): Flow<Set<Long>> =
        topUsersDataStore.data.map { preferences ->
            preferences[FOLLOWED_USER_IDS].orEmpty().map { it.toLong() }.toSet()
        }

    override suspend fun toggleFollow(
        userId: Long,
    ): Result<Unit> = try {
        //TODO implement an optimistic update without disk write
        topUsersDataStore.edit { preferences ->
            //initializing the list in case it doesn't exist with .orEmpty()
            val followedUserIds = preferences[FOLLOWED_USER_IDS].orEmpty()
            val id = userId.toString()
            preferences[FOLLOWED_USER_IDS] = if (id in followedUserIds) {
                followedUserIds - id
            } else {
                followedUserIds + id
            }
        }
        Result.Success(Unit)
    } catch (error: IOException) {
        Log.e(TAG, "couldn't save to followed user ids = ${error.message}")
        Result.Error
    }


    companion object {
        val FOLLOWED_USER_IDS = stringSetPreferencesKey("followed_user_ids")
    }

}