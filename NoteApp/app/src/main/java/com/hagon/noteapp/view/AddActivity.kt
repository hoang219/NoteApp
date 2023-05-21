package com.hagon.noteapp.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.hagon.noteapp.databinding.ActivityAddBinding
import com.hagon.noteapp.model.Note
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private var isUpdate = false
    private lateinit var format: SimpleDateFormat
    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setData()

        binding.btnCheck.setOnClickListener {
            if (binding.edtTitle.text.isNotEmpty() && binding.edtNote.text.isNotEmpty()) {
                if (isUpdate) {
                    val intent = Intent()
                    intent.putExtra("id", note.id)
                    intent.putExtra("title", binding.edtTitle.text.toString())
                    intent.putExtra("note", binding.edtNote.text.toString())
                    intent.putExtra("date", format.format(Date()))
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    val intent = Intent()
                    intent.putExtra("id", -1)
                    intent.putExtra("title", binding.edtTitle.text.toString())
                    intent.putExtra("note", binding.edtNote.text.toString())
                    intent.putExtra("date", format.format(Date()))
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Please enter data", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            if (isUpdate || binding.edtTitle.text.isNotEmpty() || binding.edtNote.text.isNotEmpty()) showDialogBack()
            else finish()
        }
    }

    private fun showDialogBack() {
        val dialog = AlertDialog.Builder(this)
        dialog.apply {
            setTitle("Warring!!!")
            setMessage("Are you sure?")
            setPositiveButton("Yes") { _, _ ->
                finish()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.create()
        }
        dialog.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setData() {
        try {
            val bundle = intent.extras
            if (bundle != null) {
                val id = bundle.getInt("id")
                val title = bundle.getString("title")
                val note = bundle.getString("note")
                val date = bundle.getString("date")
                binding.edtTitle.setText(title)
                binding.edtNote.setText(note)
                this.note = Note(id, title, note, date)
                isUpdate = true
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
    }
}