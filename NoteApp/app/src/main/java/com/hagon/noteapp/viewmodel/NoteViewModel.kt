package com.hagon.noteapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hagon.noteapp.database.NoteDatabase
import com.hagon.noteapp.database.NoteRepository
import com.hagon.noteapp.model.Note

class NoteViewModel(val repository: NoteRepository) : ViewModel() {
    val list = repository.list

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