package com.example.notes.presentation.features.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.domain.model.LoadState
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.Result
import com.example.notes.domain.model.toLoadState
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.presentation.components.withMinLoadingDelay
import com.example.notes.presentation.navigation.AppNavigator
import com.example.notes.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val noteRepository: NoteRepository,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val _state = MutableStateFlow(FavouritesState())
    val state: StateFlow<FavouritesState> = _state
        .onStart { loadFavouriteNotes() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = FavouritesState(),
        )

    fun onBackClick() {
        navigator.navigateBack()
    }

    fun onNoteClick(noteId: Long) {
        navigator.navigate(Screen.NoteDetailRoute(noteId = noteId))
    }

    fun onRetryClick() {
        loadFavouriteNotes()
    }

    fun onRemoveFavouriteClick(note: Note) {
        viewModelScope.launch {
            val updatedNote = note.copy(isFavourite = false)
            if (noteRepository.updateNote(updatedNote) is Result.Success) {
                val notesResult = noteRepository.getFavouriteNotes()
                _state.update {
                    it.copy(notesState = notesResult.toLoadState(List<Note>::isEmpty))
                }
            }
        }
    }

    private fun loadFavouriteNotes() {
        viewModelScope.launch {
            _state.update { it.copy(notesState = LoadState.Loading) }

            val notesState = withMinLoadingDelay {
                noteRepository.getFavouriteNotes().toLoadState(List<Note>::isEmpty)
            }

            _state.update { it.copy(notesState = notesState) }
        }
    }
}
