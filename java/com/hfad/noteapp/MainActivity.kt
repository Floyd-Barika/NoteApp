package com.hfad.noteapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.hfad.noteapp.databinding.ActivityMainBinding
import java.io.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NoteAdapter
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val FILEPATH = "notes.json"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        adapter = NoteAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.adapter = adapter
        setContentView(view)

        val toolBar = findViewById<MaterialToolbar>(R.id.tool_bar)
        setSupportActionBar(toolBar)
        binding.fab.setOnClickListener {
            NewNote().show(supportFragmentManager, "")
        }

        adapter.noteList = retrieveNotes()
        adapter.notifyItemRangeInserted(0, adapter.noteList.size)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onStart() {
        super.onStart()

        val showDividingLines = sharedPreferences.getBoolean("dividingLines", true)
        if (showDividingLines) binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        else if (binding.recyclerView.itemDecorationCount > 0) binding.recyclerView.removeItemDecorationAt(0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()
        return when(item.itemId){
            R.id.action_mainActivity_to_settings -> {
                val intent = Intent(this,Settings::class.java)
            startActivity(intent)
            true}
            else -> {super.onOptionsItemSelected(item)}
        }
    }

    fun createNewNote(n: Note) {
        adapter.noteList.add(n)
        adapter.notifyItemInserted(adapter.noteList.size - 1)
        saveNotes()
    }

    fun deleteNote(index: Int) {
        adapter.noteList.removeAt(index)
        adapter.notifyItemRemoved(index)
        saveNotes()
    }

    fun showNote(index: Int) {
        val dialog = ShowNote(adapter.noteList[index], index)
        dialog.show(supportFragmentManager, "")
    }

    private fun saveNotes() {
        val notes = adapter.noteList
        val gson = GsonBuilder().create()
        val jsonNotes = gson.toJson(notes) // this and the above two lines contribute to the serialization of the notes.
        var writer: Writer? = null
        try{
          val out = this.openFileOutput(FILEPATH, Context.MODE_PRIVATE) // a variable for opening the file, FILEPATH, which is itself a string variable for storing JSON objects. out opens the file, FILEPATH.
            writer = OutputStreamWriter(out) // this gives writer the permission to write through out to FILEPATH
            writer.write(jsonNotes) // this and the two above lines are the actual saving of the notes to a file
        }catch(e:Exception){
            writer?.close()
        }finally{
            writer?.close()
        } }

    private fun retrieveNotes():MutableList<Note>{
        var noteList = mutableListOf<Note>()
        if (this.getFileStreamPath(FILEPATH).isFile){
            var reader: Reader? = null
            try{
                val fileInput = this.openFileInput(FILEPATH)
                reader = BufferedReader(InputStreamReader(fileInput))
                val stringBuilder = StringBuilder()
            for(line in reader.readLines()) stringBuilder.append(line)
            if(stringBuilder.isNotEmpty()){
                val listType = object: TypeToken<List<Note>>(){}.type
                noteList = Gson().fromJson(stringBuilder.toString(),listType)
            }
            }catch(e:Exception){
                reader?.close()
            }finally{
                reader?.close()
            }
        }
        return noteList
    }
}