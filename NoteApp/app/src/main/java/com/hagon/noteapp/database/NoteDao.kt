package com.hagon.noteapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hagon.noteapp.model.Note

@Dao
interface NoteDao {
    @Insert
    fun insert(note: Note)

    @Delete
    fun delete(note: Note)

    @Update
    fun update(note: Note)

    @Query("SELECT * FROM note_tb")
    fun getAll(): LiveData<List<Note>>
}