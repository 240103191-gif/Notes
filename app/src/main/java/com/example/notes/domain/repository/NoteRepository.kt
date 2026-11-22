package com.example.notes.domain.repository

import com.example.notes.domain.model.Note
import com.example.notes.domain.model.Result

interface NoteRepository {

    suspend fun getAllNotes(): Result<List<Note>>

    suspend fun getNoteById(id: Long): Result<Note>

    suspend fun saveNote(note: Note): Result<Unit>

    suspend fun deleteNote(id: Long): Result<Unit>
}
