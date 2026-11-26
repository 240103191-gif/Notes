package com.example.notes.presentation.features.notecreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.Result
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.presentation.navigation.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteCreateViewModel(
    private val noteRepository: NoteRepository,
    private val navigator: AppNavigator,
) : ViewModel() {

    private val _state = MutableStateFlow(NoteCreateState())
    val state: StateFlow<NoteCreateState> = _state.asStateFlow()

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

    fun onSaveClick() {
        val currentState = _state.value
        if (currentState.title.isBlank() && currentState.content.isBlank()) return

        viewModelScope.launch {
            val note = Note(
                id = 0L,
                title = currentState.title.trim(),
                content = currentState.content.trim(),
                createdAt = System.currentTimeMillis(),
                isFavourite = currentState.isFavourite,
            )
            if (noteRepository.insertNote(note) is Result.Success) navigator.navigateBack()
        }
    }
}
