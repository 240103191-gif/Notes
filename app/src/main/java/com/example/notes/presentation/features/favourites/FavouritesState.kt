package com.example.notes.presentation.features.favourites

import com.example.notes.domain.model.LoadState
import com.example.notes.domain.model.Note

data class FavouritesState(
    val notesState: LoadState<List<Note>> = LoadState.Loading,
)
