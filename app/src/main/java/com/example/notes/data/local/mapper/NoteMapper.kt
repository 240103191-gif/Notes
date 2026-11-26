package com.example.notes.data.local.mapper

import com.example.notes.data.local.db.entity.NoteEntity
import com.example.notes.domain.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DATE_PATTERN = "dd.MM.yyyy, HH:mm"

fun Long.toFormattedDate(): String {
    val formatter = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
    return formatter.format(Date(this))
}

class NoteMapper {

    fun toDomain(entity: NoteEntity): Note = Note(
        id = entity.id,
        title = entity.title,
        content = entity.content,
        createdAt = entity.createdAt,
        formattedDate = entity.createdAt.toFormattedDate(),
        isFavourite = entity.isFavourite,
        isDeleted = entity.isDeleted,
    )

    fun toEntity(domain: Note): NoteEntity = NoteEntity(
        id = domain.id,
        title = domain.title,
        content = domain.content,
        createdAt = domain.createdAt,
        isFavourite = domain.isFavourite,
        isDeleted = domain.isDeleted,
    )
}
