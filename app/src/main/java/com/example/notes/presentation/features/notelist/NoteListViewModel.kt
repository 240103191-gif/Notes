package com.example.notes.presentation.features.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.domain.model.LoadState
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.Quote
import com.example.notes.domain.model.Result
import com.example.notes.domain.model.toLoadState
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.domain.repository.QuoteRepository
import com.example.notes.presentation.components.withMinLoadingDelay
import com.example.notes.presentation.navigation.AppNavigator
import com.example.notes.presentation.navigation.Screen
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val noteRepository: NoteRepository,
    private val quoteRepository: QuoteRepository,
    private val navigator: AppNavigator,
) : ViewModel() {

    private var quoteDeferred: Deferred<Result<Quote>>? = null

    private val _state = MutableStateFlow(NoteListState())
    val state: StateFlow<NoteListState> = _state
        .onStart { loadNotes() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = NoteListState(),
        )

    init {
        quoteDeferred = viewModelScope.async {
            quoteRepository.getQuote()
        }
    }

    fun onCreateClick() {
        navigator.navigate(Screen.NoteCreateRoute)
    }

    fun onNoteClick(noteId: Long) {
        navigator.navigate(Screen.NoteDetailRoute(noteId = noteId))
    }

    fun onRetryNotesClick() {
        loadNotes()
    }

    fun onRetryQuoteClick() {
        viewModelScope.launch {
            _state.update { it.copy(quoteState = LoadState.Loading) }

            val quoteState = withMinLoadingDelay {
                val deferred = viewModelScope.async {
                    quoteRepository.getQuote()
                }
                quoteDeferred = deferred
                deferred.await().toLoadState { it.quote.isBlank() }
            }

            _state.update { it.copy(quoteState = quoteState) }
        }
    }

    fun onThemeClick() {
        _state.update { it.copy(isThemeSheetVisible = true) }
    }

    fun onDismissThemeSheet() {
        _state.update { it.copy(isThemeSheetVisible = false) }
    }

    private fun loadNotes() {
        viewModelScope.launch {
            _state.update { it.copy(notesState = LoadState.Loading) }

            val (quoteState, notesState) = withMinLoadingDelay {
                val quoteResult = quoteDeferred?.await() ?: Result.Error()
                val notesResult = noteRepository.getAllNotes()
                val mappedQuoteState = quoteResult.toLoadState { it.quote.isBlank() }
                val mappedNotesState = notesResult.toLoadState(List<Note>::isEmpty)
                mappedQuoteState to mappedNotesState
            }

            _state.update {
                it.copy(
                    quoteState = quoteState,
                    notesState = notesState,
                )
            }
        }
    }
}
