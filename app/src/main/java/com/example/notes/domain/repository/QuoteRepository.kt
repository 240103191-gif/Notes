package com.example.notes.domain.repository

import com.example.notes.domain.model.Quote
import com.example.notes.domain.model.Result

interface QuoteRepository {

    suspend fun getQuote(): Result<Quote>
}
