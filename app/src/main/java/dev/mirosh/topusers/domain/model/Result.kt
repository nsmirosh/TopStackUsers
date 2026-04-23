package dev.mirosh.topusers.domain.model

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data object Error : Result<Nothing>()
}