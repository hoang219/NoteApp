package com.hagon.noteapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hagon.noteapp.databinding.ItemNoteBinding
import com.hagon.noteapp.model.Note

class NoteAdapter(list: List<Note>, private val onClicked: OnClicked) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var list: List<Note>
    private var listResult: List<Note>

    interface OnClicked {
        fun onItemClicked(position: Int)
        fun onItemLongClicked(position: Int, view: View)
    }

    init {
        this.list = list
        this.listResult = list
    }

    class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.binding.tvNote.text = list[position].note
        holder.binding.tvTitle.text = list[position].title
        holder.binding.tvDate.text = list[position].date
        holder.binding.root.setOnLongClickListener {
            onClicked.onItemLongClicked(position, holder.binding.root)
            true
        }
        holder.binding.root.setOnClickListener {
            onClicked.onItemClicked(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<Note>) {
        this.list = list
        notifyDataSetChanged()
    }
}