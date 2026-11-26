package com.example.notes.data.local

import com.example.notes.data.local.db.NoteDao
import com.example.notes.data.local.mapper.NoteMapper
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.Result
import com.example.notes.domain.repository.NoteRepository

class NoteRepositoryImpl(
    private val dao: NoteDao,
    private val mapper: NoteMapper,
) : NoteRepository {

    override suspend fun getAllNotes(): Result<List<Note>> = try {
        val notes = dao.getAllNotes().map(mapper::toDomain)
        Result.Success(notes)
    } catch (e: Exception) {
        Result.Error(e.message)
    }

    override suspend fun getFavouriteNotes(): Result<List<Note>> = try {
        val notes = dao.getFavouriteNotes().map(mapper::toDomain)
        Result.Success(notes)
    } catch (e: Exception) {
        Result.Error(e.message)
    }

    override suspend fun getDeletedNotes(): Result<List<Note>> = try {
        val notes = dao.getDeletedNotes().map(mapper::toDomain)
        Result.Success(notes)
    } catch (e: Exception) {
        Result.Error(e.message)
    }

    override suspend fun getNoteById(id: Long): Result<Note?> = try {
        val note = dao.getNoteById(id)?.let(mapper::toDomain)
        Result.Success(note)
    } catch (e: Exception) {
        Result.Error(e.message)
    }

    override suspend fun insertNote(note: Note): Result<Unit> = try {
        dao.insertNote(mapper.toEntity(note))
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.message)
    }

    override suspend fun updateNote(note: Note): Result<Unit> = try {
        dao.updateNote(mapper.toEntity(note))
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.message)
    }

    override suspend fun deleteNote(note: Note): Result<Unit> = try {
        dao.deleteNote(mapper.toEntity(note))
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.message)
    }
}
