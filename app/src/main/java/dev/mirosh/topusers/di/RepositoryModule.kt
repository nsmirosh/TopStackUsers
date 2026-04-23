package dev.mirosh.topusers.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mirosh.topusers.data.repository.StackExchangeRepositoryImpl
import dev.mirosh.topusers.domain.repository.StackExchangeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStackExchangeRepository(
        stackExchangeRepositoryImpl: StackExchangeRepositoryImpl
    ): StackExchangeRepository
}
