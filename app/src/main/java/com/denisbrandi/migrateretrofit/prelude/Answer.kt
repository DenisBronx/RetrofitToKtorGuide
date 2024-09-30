package com.denisbrandi.migrateretrofit.prelude

sealed class Answer<out T, out E> {
    data class Success<out T>(val data: T) : Answer<T, Nothing>()

    data class Error<out E>(val reason: E) : Answer<Nothing, E>()
}