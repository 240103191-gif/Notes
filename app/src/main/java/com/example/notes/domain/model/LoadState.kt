package com.example.notes.domain.model

sealed interface LoadState<out T> {

    data object Loading : LoadState<Nothing>

    data class Success<out T>(
        val data: T,
    ) : LoadState<T>

    data class Error(
        val message: String? = null,
    ) : LoadState<Nothing>
}

fun <T> Result<T>.toLoadState(): LoadState<T> = when (this) {
    is Result.Success -> LoadState.Success(data)
    is Result.Error -> LoadState.Error(message)
}
