package com.example.notes.di

import com.example.notes.data.local.NoteRepositoryImpl
import com.example.notes.data.remote.QuoteRepositoryImpl
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.domain.repository.QuoteRepository
import com.example.notes.presentation.features.notecreate.NoteCreateViewModel
import com.example.notes.presentation.features.notelist.NoteListViewModel
import com.example.notes.presentation.navigation.AppNavigator
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private const val QUOTE_BASE_URL = "https://dummyjson.com/"

val appModule = module {

    single<NoteRepository> {
        NoteRepositoryImpl()
    }

    single<QuoteRepository> {
        QuoteRepositoryImpl()
    }

    single {
        AppNavigator()
    }

    viewModel {
        NoteListViewModel(
            noteRepository = get(),
            quoteRepository = get(),
            navigator = get(),
        )
    }

    viewModel {
        NoteCreateViewModel(
            noteRepository = get(),
            navigator = get(),
        )
    }
}
