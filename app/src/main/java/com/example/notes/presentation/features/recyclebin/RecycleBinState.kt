package com.example.notes.presentation.features.recyclebin

import com.example.notes.domain.model.LoadState
import com.example.notes.domain.model.Note

data class RecycleBinState(
    val notesState: LoadState<List<Note>> = LoadState.Loading,
)
