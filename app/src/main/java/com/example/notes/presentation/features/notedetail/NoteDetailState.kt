package com.example.notes.presentation.features.notedetail

import com.example.notes.domain.model.LoadState
import com.example.notes.domain.model.Note

data class NoteDetailState(
    val noteState: LoadState<Note> = LoadState.Loading,
    val title: String = "",
    val content: String = "",
    val isFavourite: Boolean = false,
)
