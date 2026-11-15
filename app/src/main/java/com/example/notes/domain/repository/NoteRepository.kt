package com.example.notes.domain.repository

import com.example.notes.domain.model.Note

interface NoteRepository {

    suspend fun getAllNotes(): List<Note>

    suspend fun getNoteById(id: Long): Note?

    suspend fun saveNote(note: Note)

    suspend fun deleteNote(id: Long)
}
