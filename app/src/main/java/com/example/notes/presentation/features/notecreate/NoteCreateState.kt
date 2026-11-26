package com.example.notes.presentation.features.notecreate

data class NoteCreateState(
    val title: String = "",
    val content: String = "",
    val isFavourite: Boolean = false,
)
