package com.hagon.noteapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hagon.noteapp.database.NoteDatabase
import com.hagon.noteapp.model.Note

class NoteViewModel(context: Context) : ViewModel() {
    private val noteDao = NoteDatabase.getDatabase(context).noteDao()
    private lateinit var list: LiveData<List<Note>>

    fun getAll(): LiveData<List<Note>> {
        list = noteDao.getAll()
        return list
    }

    fun insert(note: Note) {
        noteDao.insert(note)
    }

    fun delete(note: Note) {
        noteDao.delete(note)
    }

    fun update(note: Note) {
        noteDao.update(note)
    }

    fun search(string: String): List<Note> {
        val listResult = mutableListOf<Note>()
        for (item in list.value as List<Note>) {
            if (item.title?.contains(string) == true || item.note?.contains(string) == true) {
                listResult.add(item)
            }
        }
        return listResult
    }
}