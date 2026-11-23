package com.example.notes.di

import androidx.room.Room
import com.example.notes.data.local.NoteRepositoryImpl
import com.example.notes.data.local.db.AppDatabase
import com.example.notes.data.local.mapper.NoteMapper
import com.example.notes.data.remote.QuoteRepositoryImpl
import com.example.notes.data.remote.api.QuoteApi
import com.example.notes.data.remote.mapper.QuoteMapper
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.domain.repository.QuoteRepository
import com.example.notes.presentation.features.notecreate.NoteCreateViewModel
import com.example.notes.presentation.features.notelist.NoteListViewModel
import com.example.notes.presentation.navigation.AppNavigator
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val DATABASE_NAME = "notes.db"
private const val QUOTE_BASE_URL = "https://dummyjson.com/"
private const val NETWORK_TIMEOUT_SECONDS = 5L

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

    single {
        OkHttpClient.Builder()
            .connectTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    single {
        val json = Json {
            ignoreUnknownKeys = true
        }
        Retrofit.Builder()
            .baseUrl(QUOTE_BASE_URL)
            .client(get())
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(QuoteApi::class.java)
    }

    factory {
        NoteMapper()
    }

    factory {
        QuoteMapper()
    }

    single<NoteRepository> {
        NoteRepositoryImpl(
            dao = get(),
            mapper = get(),
        )
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
