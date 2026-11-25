package com.example.notes.domain.repository

import com.example.notes.domain.model.Note
import com.example.notes.domain.model.Result

interface NoteRepository {

    suspend fun getAllNotes(): Result<List<Note>>

    suspend fun getNoteById(id: Long): Result<Note>

    suspend fun insertNote(note: Note): Result<Unit>

    suspend fun updateNote(note: Note): Result<Unit>

    suspend fun deleteNote(note: Note): Result<Unit>
}
