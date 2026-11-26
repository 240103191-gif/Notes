package com.example.notes.presentation.features.recyclebin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.domain.model.LoadState
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.Result
import com.example.notes.domain.model.toLoadState
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.presentation.components.withMinLoadingDelay
import com.example.notes.presentation.navigation.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecycleBinViewModel(
    private val noteRepository: NoteRepository,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val _state = MutableStateFlow(RecycleBinState())
    val state: StateFlow<RecycleBinState> = _state
        .onStart { loadDeletedNotes() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = RecycleBinState(),
        )

    fun onBackClick() {
        navigator.navigateBack()
    }

    fun onRetryClick() {
        loadDeletedNotes()
    }

    fun onRestoreClick(note: Note) {
        viewModelScope.launch {
            val restoredNote = note.copy(isDeleted = false)
            if (noteRepository.updateNote(restoredNote) is Result.Success) {
                val notesResult = noteRepository.getDeletedNotes()
                _state.update {
                    it.copy(notesState = notesResult.toLoadState(List<Note>::isEmpty))
                }
            }
        }
    }

    fun onDeletePermanentlyClick(note: Note) {
        viewModelScope.launch {
            if (noteRepository.deleteNote(note) is Result.Success) {
                val notesResult = noteRepository.getDeletedNotes()
                _state.update {
                    it.copy(notesState = notesResult.toLoadState(List<Note>::isEmpty))
                }
            }
        }
    }

    private fun loadDeletedNotes() {
        viewModelScope.launch {
            _state.update { it.copy(notesState = LoadState.Loading) }

            val notesState = withMinLoadingDelay {
                noteRepository.getDeletedNotes().toLoadState(List<Note>::isEmpty)
            }

            _state.update { it.copy(notesState = notesState) }
        }
    }
}
