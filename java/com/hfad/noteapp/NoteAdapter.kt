package com.hfad.noteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.noteapp.NoteAdapter.NoteViewHolder

class NoteAdapter(private val mainActivity:MainActivity):RecyclerView.Adapter<NoteViewHolder>() {
var noteList = mutableListOf<Note>()

   inner class NoteViewHolder(view:View): RecyclerView.ViewHolder(view), View.OnClickListener{
   internal var mTitle = view.findViewById<View>(R.id.viewTitle) as TextView
   internal var mContents = view.findViewById<View>(R.id.viewContents) as TextView
        init {
            view.isClickable
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            mainActivity.showNote(layoutPosition)
        } }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_preview,parent,false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.mTitle.text = note.title
        holder.mContents.text = if(note.contents.length<25) note.contents else note.contents.substring(0,25) + "..."
    }

    override fun getItemCount(): Int = noteList.size
}