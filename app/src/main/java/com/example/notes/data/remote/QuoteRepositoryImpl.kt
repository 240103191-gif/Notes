package com.example.notes.data.remote

import com.example.notes.domain.model.Quote
import com.example.notes.domain.model.Result
import com.example.notes.domain.repository.QuoteRepository

class QuoteRepositoryImpl : QuoteRepository {

    override suspend fun getQuote(): Result<Quote> = Result.Error()
}
