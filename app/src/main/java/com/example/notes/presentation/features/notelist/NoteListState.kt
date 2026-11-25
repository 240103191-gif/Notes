package com.example.notes.presentation.features.notelist

import com.example.notes.domain.model.LoadState
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.Quote

data class NoteListState(
    val quoteState: LoadState<Quote> = LoadState.Loading,
    val notesState: LoadState<List<Note>> = LoadState.Loading,
    val isThemeSheetVisible: Boolean = false,
)
