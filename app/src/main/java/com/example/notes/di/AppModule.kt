package com.example.notes.di

import androidx.room.Room
import com.example.notes.data.local.NoteRepositoryImpl
import com.example.notes.data.local.db.AppDatabase
import com.example.notes.data.local.mapper.NoteMapper
import com.example.notes.data.remote.QuoteRepositoryImpl
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.domain.repository.QuoteRepository
import com.example.notes.presentation.features.notecreate.NoteCreateViewModel
import com.example.notes.presentation.features.notelist.NoteListViewModel
import com.example.notes.presentation.navigation.AppNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private const val DATABASE_NAME = "notes.db"
private const val QUOTE_BASE_URL = "https://dummyjson.com/"

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME,
        ).build()
    }

    single {
        get<AppDatabase>().noteDao()
    }

    factory {
        NoteMapper()
    }

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
