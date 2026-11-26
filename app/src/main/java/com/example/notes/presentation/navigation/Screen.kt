package com.example.notes.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object NoteListRoute : Screen()

    @Serializable
    data object NoteCreateRoute : Screen()

    @Serializable
    data class NoteDetailRoute(
        val noteId: Long,
    ) : Screen()

    @Serializable
    data object RecycleBinRoute : Screen()

    @Serializable
    data object FavouritesRoute : Screen()
}
