package com.hfad.noteapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.hfad.noteapp.databinding.NewNoteBinding

class NewNote:DialogFragment() {
private var _binding:NewNoteBinding? = null
private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val callingActivity = activity as MainActivity
        val inflater= callingActivity.layoutInflater
        _binding = NewNoteBinding.inflate(inflater)

        val builder = AlertDialog.Builder(callingActivity).setView(binding.root).setMessage(resources.getString(R.string.add_new_note))
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnOk.setOnClickListener {
        val title = binding.titleText.text
        val contents = binding.contentText.text

            if(title.isNotEmpty()  && contents.isNotEmpty()){
                val note = Note(title.toString(), contents.toString())
                callingActivity.createNewNote(note)
                Toast.makeText(callingActivity, "Note Saved",Toast.LENGTH_SHORT)
                dismiss()
            }else {
                Toast.makeText(callingActivity, "Please fill in title and content",Toast.LENGTH_LONG)
                }
        }

        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//super.onCreateDialog(savedInstanceState)