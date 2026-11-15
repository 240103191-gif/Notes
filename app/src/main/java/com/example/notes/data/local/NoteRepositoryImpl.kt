package com.example.notes.data.local

import com.example.notes.domain.model.Note
import com.example.notes.domain.repository.NoteRepository

class NoteRepositoryImpl : NoteRepository {

    override suspend fun getAllNotes(): List<Note> = emptyList()

    override suspend fun getNoteById(id: Long): Note? = null

    override suspend fun saveNote(note: Note) {}

    override suspend fun deleteNote(id: Long) {}
}
