package com.example.notes.data.remote.api

import com.example.notes.data.remote.api.dto.QuoteDto
import retrofit2.http.GET

interface QuoteApi {

    @GET("quotes/random")
    suspend fun getRandomQuote(): QuoteDto
}
