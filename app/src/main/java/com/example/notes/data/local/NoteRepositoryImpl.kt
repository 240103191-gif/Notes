package com.example.notes.data.local

import com.example.notes.domain.model.Note
import com.example.notes.domain.model.Result
import com.example.notes.domain.repository.NoteRepository

class NoteRepositoryImpl : NoteRepository {

    override suspend fun getAllNotes(): Result<List<Note>> = Result.Success(emptyList())

    override suspend fun getNoteById(id: Long): Result<Note> = Result.Error()

    override suspend fun saveNote(note: Note): Result<Unit> = Result.Success(Unit)

    override suspend fun deleteNote(id: Long): Result<Unit> = Result.Success(Unit)
}
