package com.unsplash.shared.data.utils

sealed class Result<out T : Any> {

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val error: Throwable) : Result<Nothing>()

    fun exceptionOrNull(): Throwable? =
        when (this) {
            is Error -> error
            else -> null
        }

    fun dataOrNull(): T? =
        when (this) {
            is Success -> data
            else -> null
        }
}

inline fun <T : Any> Result<T>.onFailure(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) {
        action(error)
    }
    return this
}

inline fun <T : Any> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

suspend fun <T : Any> getResult(action: suspend () -> T): Result<T> =
    try {
        Result.Success(action())
    } catch (e: Throwable) {
        Result.Error(e)
    }
