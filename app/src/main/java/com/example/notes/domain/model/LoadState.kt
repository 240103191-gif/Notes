package com.example.notes.domain.model

sealed interface LoadState<out T> {

    data object Loading : LoadState<Nothing>

    data object Empty : LoadState<Nothing>

    data class Success<out T>(
        val data: T,
    ) : LoadState<T>

    data class Error(
        val message: String? = null,
    ) : LoadState<Nothing>
}

fun <T> Result<T>.toLoadState(
    isEmpty: (T) -> Boolean = { false },
): LoadState<T> = when (this) {
    is Result.Success -> if (isEmpty(data)) LoadState.Empty else LoadState.Success(data)
    is Result.Error -> LoadState.Error(message)
}

fun <T : Any> Result<T?>.toItemLoadState(): LoadState<T> = when (this) {
    is Result.Success -> if (data == null) LoadState.Empty else LoadState.Success(data)
    is Result.Error -> LoadState.Error(message)
}
