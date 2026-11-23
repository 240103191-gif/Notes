package com.example.notes.data.remote.mapper

import com.example.notes.data.remote.api.dto.QuoteDto
import com.example.notes.domain.model.Quote

class QuoteMapper {

    fun toDomain(dto: QuoteDto): Quote = Quote(
        id = dto.id ?: 0L,
        quote = dto.quote.orEmpty(),
        author = dto.author.orEmpty(),
    )
}
