package com.example.notes.data.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteDto(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("quote")
    val quote: String? = null,

    @SerialName("author")
    val author: String? = null,
)
