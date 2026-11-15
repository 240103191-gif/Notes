package com.example.notes.domain.repository

import com.example.notes.domain.model.Quote

interface QuoteRepository {

    suspend fun getQuote(): Quote?
}
