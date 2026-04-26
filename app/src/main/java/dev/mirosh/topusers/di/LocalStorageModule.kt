package dev.mirosh.topusers.di


import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mirosh.topusers.domain.model.Result
import dev.mirosh.topusers.domain.repository.UserKeyValueStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// top-level — created once per process
val Context.topUsersDataStore: DataStore<Preferences> by preferencesDataStore(name = "top_users")

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    @Provides
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.topUsersDataStore

}
