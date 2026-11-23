package com.example.notes.data.remote

import com.example.notes.data.remote.api.QuoteApi
import com.example.notes.data.remote.mapper.QuoteMapper
import com.example.notes.domain.model.Quote
import com.example.notes.domain.model.Result
import com.example.notes.domain.repository.QuoteRepository

class QuoteRepositoryImpl(
    private val api: QuoteApi,
    private val mapper: QuoteMapper,
) : QuoteRepository {

    override suspend fun getQuote(): Result<Quote> = try {
        val dto = api.getRandomQuote()
        val quote = mapper.toDomain(dto)
        if (quote.quote.isNotBlank()) Result.Success(quote) else Result.Error()
    } catch (e: Exception) {
        Result.Error(e.message)
    }
}
