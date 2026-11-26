package com.example.notes.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notes.data.local.db.entity.NoteEntity

@Database(
    entities = [
        NoteEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}
