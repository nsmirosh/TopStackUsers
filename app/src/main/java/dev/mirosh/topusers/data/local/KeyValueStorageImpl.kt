package dev.mirosh.topusers.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.mirosh.topusers.domain.repository.KeyValueStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class KeyValueStorageImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : KeyValueStorage {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val EXAMPLE_COUNTER = intPreferencesKey("example_counter")

    override fun counterFlow(): Flow<Int> = context.dataStore.data.map { preferences ->
        val result = preferences[EXAMPLE_COUNTER] ?: 0
        Log.d("KeyValueStorage", "result = $result")
        result
    }


    override suspend fun incrementCounter() {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->

                Log.d("KeyValueStorage", "incrementing...")
                preferences[EXAMPLE_COUNTER] = (preferences[EXAMPLE_COUNTER] ?: 0) + 1
            }
        }
    }

}