package com.example.notes.domain.model

data class Note(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val createdAt: Long = 0L,
    val formattedDate: String = "",
    val isFavourite: Boolean = false,
    val isDeleted: Boolean = false,
)
