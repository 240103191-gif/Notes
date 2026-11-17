package com.example.notes.presentation.features.notelist

import com.example.notes.domain.model.Note
import com.example.notes.domain.model.Quote

data class NoteListState(
    val isLoading: Boolean = true,
    val quote: Quote? = null,
    val notes: List<Note> = emptyList(),
)
