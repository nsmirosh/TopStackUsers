package dev.mirosh.topusers.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// top-level — created once per process
val Context.topUsersDataStore: DataStore<Preferences> by preferencesDataStore(name = "top_users")

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.topUsersDataStore

}
