package dev.mirosh.topusers.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.repository.UserKeyValueStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject


const val TAG = "KeyValueStorageImpl"

class KeyValueStorageImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : UserKeyValueStorage {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "top_users")

    override fun getFollowedUserIds(): Flow<Set<String>> =
        context.dataStore.data.map { preferences ->
            preferences[FOLLOWED_USER_IDS].orEmpty()
        }

    override suspend fun toggleFollow(
        userId: Long,
    ): Result<Unit> = try {
        context.dataStore.edit { preferences ->
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