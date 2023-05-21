package com.hagon.noteapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hagon.noteapp.R
import com.hagon.noteapp.adapter.NoteAdapter
import com.hagon.noteapp.databinding.ActivityMainBinding
import com.hagon.noteapp.model.Note
import com.hagon.noteapp.viewmodel.NoteViewModel
import com.hagon.noteapp.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity(), NoteAdapter.OnClicked {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NoteAdapter
    private var list = listOf<Note>()
    private lateinit var factory: NoteViewModelFactory
    private lateinit var getContent: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        factory = NoteViewModelFactory(this)
        binding.recycleView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]
        adapter = NoteAdapter(list, this)
        binding.recycleView.adapter = adapter

        val data = viewModel.getAll()
        data.observe(this) {
            val text = binding.searchView.query.toString()
            list = if (text != "") {
                viewModel.search(text)
            } else it
            adapter.updateList(list)
        }


        getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val id = result.data?.getIntExtra("id", -2)
                    val title = result.data?.getStringExtra("title")
                    val note = result.data?.getStringExtra("note")
                    val date = result.data?.getStringExtra("date")
                    Toast.makeText(
                        this,
                        "Updating ${id.toString()} MainActivity",
                        Toast.LENGTH_SHORT
                    ).show()

                    when (id) {
                        -2 -> Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show()
                        -1 -> {
                            viewModel.insert(Note(null, title, note, date))
                            Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            viewModel.update(Note(id, title, note, date))
                            Toast.makeText(this, "Updated $title", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null) {
                    list = viewModel.search(p0)
                    adapter.updateList(list)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null) {
                    list = viewModel.search(p0)
                    adapter.updateList(list)
                }
                return true
            }
        })
    }

    override fun onItemClicked(position: Int) {
        val intent = Intent(this, AddActivity::class.java)
        val note = list[position]
        val bundle = Bundle()
        bundle.putInt("id", note.id!!)
        bundle.putString("title", note.title)
        bundle.putString("note", note.note)
        bundle.putString("date", note.date)
        intent.putExtras(bundle)
        getContent.launch(intent)
    }

    override fun onItemLongClicked(position: Int, view: View) {
        popUpMenu(list[position], view)
    }

    private fun popUpMenu(note: Note, view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_rv)
        popupMenu.setOnMenuItemClickListener { p0 ->
            when (p0.itemId) {
                R.id.mnuDelete -> showDialogDelete(note)
            }
            true
        }
        popupMenu.show()
    }

    private fun showDialogDelete(note: Note) {
        val dialog = AlertDialog.Builder(this)
        dialog.apply {
            setTitle("Warring!!!")
            setMessage("Do you want to delete ${note.title}")
            setPositiveButton("Yes") { dialog, _ ->
                viewModel.delete(note)
                dialog.dismiss()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        }.create()
        dialog.show()
    }
}