package dev.mirosh.topusers.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mirosh.topusers.data.local.KeyValueStorageImpl
import dev.mirosh.topusers.data.repository.UserRepositoryImpl
import dev.mirosh.topusers.domain.repository.KeyValueStorage
import dev.mirosh.topusers.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStackExchangeRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindKeyValueStorage(
        keyValueStorageImpl: KeyValueStorageImpl
    ): KeyValueStorage
}
