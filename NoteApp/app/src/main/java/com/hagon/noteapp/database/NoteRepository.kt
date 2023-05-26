package com.hagon.noteapp.database

import com.hagon.noteapp.model.Note

class NoteRepository(private val noteDao: NoteDao) {

    var list = noteDao.getAll()

    fun insert(note: Note){
        noteDao.insert(note)
    }
    fun delete(note: Note){
        noteDao.delete(note)
    }
    fun update(note: Note){
        noteDao.update(note)
    }
}