package com.hagon.noteapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note_tb")
data class Note(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "note")
    val note: String?,
    @ColumnInfo(name = "date")
    val date: String?
) : Serializable