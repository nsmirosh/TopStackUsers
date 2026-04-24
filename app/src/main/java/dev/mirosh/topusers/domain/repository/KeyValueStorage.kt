package dev.mirosh.topusers.domain.repository

import kotlinx.coroutines.flow.Flow

interface KeyValueStorage {
     suspend fun incrementCounter()
     fun counterFlow(): Flow<Int>
}