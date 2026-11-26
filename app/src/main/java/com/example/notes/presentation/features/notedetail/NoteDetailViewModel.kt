package com.example.notes.presentation.features.notedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.notes.domain.model.LoadState
import com.example.notes.domain.model.Result
import com.example.notes.domain.model.toItemLoadState
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.presentation.components.withMinLoadingDelay
import com.example.notes.presentation.navigation.AppNavigator
import com.example.notes.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Screen.NoteDetailRoute>()

    private val _state = MutableStateFlow(NoteDetailState())
    val state: StateFlow<NoteDetailState> = _state.asStateFlow()

    init {
        loadNote()
    }

    fun onTitleChange(title: String) {
        _state.update { it.copy(title = title) }
    }

    fun onContentChange(content: String) {
        _state.update { it.copy(content = content) }
    }

    fun onFavouriteClick() {
        _state.update { it.copy(isFavourite = !it.isFavourite) }
    }

    fun onBackClick() {
        navigator.navigateBack()
    }

    fun onRetryClick() {
        loadNote()
    }

    fun onSaveClick() {
        val currentState = _state.value
        val originalNote = (currentState.noteState as? LoadState.Success)?.data ?: return
        if (currentState.title.isBlank() && currentState.content.isBlank()) return

        viewModelScope.launch {
            val updatedNote = originalNote.copy(
                title = currentState.title.trim(),
                content = currentState.content.trim(),
                isFavourite = currentState.isFavourite,
            )
            if (noteRepository.updateNote(updatedNote) is Result.Success) navigator.navigateBack()
        }
    }

    fun onDeleteClick() {
        val note = (_state.value.noteState as? LoadState.Success)?.data ?: return

        viewModelScope.launch {
            val deletedNote = note.copy(isDeleted = true)
            if (noteRepository.updateNote(deletedNote) is Result.Success) navigator.navigateBack()
        }
    }

    private fun loadNote() {
        viewModelScope.launch {
            _state.update { it.copy(noteState = LoadState.Loading) }

            val noteResult = withMinLoadingDelay {
                noteRepository.getNoteById(route.noteId)
            }

            val loadedNote = (noteResult as? Result.Success)?.data
            _state.update {
                it.copy(
                    noteState = noteResult.toItemLoadState(),
                    title = loadedNote?.title.orEmpty(),
                    content = loadedNote?.content.orEmpty(),
                    isFavourite = loadedNote?.isFavourite == true,
                )
            }
        }
    }
}
