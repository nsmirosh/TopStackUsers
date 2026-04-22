package dev.mirosh.topusers.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mirosh.topusers.data.network.StackExchangeApi
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.stackexchange.com/2.2/")
            .build()
    }

    @Provides
    fun provideStackExchangeApi(retrofit: Retrofit): StackExchangeApi {
        return retrofit.create(StackExchangeApi::class.java)
    }
}
