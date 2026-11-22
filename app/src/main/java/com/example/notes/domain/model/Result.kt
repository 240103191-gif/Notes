package com.example.notes.domain.model

sealed interface Result<out T> {

    data class Success<out T>(
        val data: T,
    ) : Result<T>

    data class Error(
        val message: String? = null,
    ) : Result<Nothing>
}
