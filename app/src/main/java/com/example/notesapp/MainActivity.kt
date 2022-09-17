package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_add_note.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var myRef:DatabaseReference
    private var listNotes = ArrayList<Note>()
    private val context = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val database = Firebase.database
        myRef = database.getReference().child("Notes")


        addDataInList()

        fab_add_note.setOnClickListener {
            showDialodAddNotes()
        }
    }



    fun showDialodAddNotes(){
        val view = layoutInflater.inflate(R.layout.custom_add_note , null , false)

        val dialog = AlertDialog.Builder(this)
        dialog.setView(view)
        dialog.setTitle(R.string.addNewNote)
        val alert = dialog.create()
        alert.show()

        view.btn_add.setOnClickListener {
            val title = view.et_title.text.toString()
            val note = view.et_note.text.toString()
            if (title.isNotEmpty() && note.isNotEmpty()) {
                val id = myRef.push().key
                val myNote = Note(id!!, title, note, getCurrentDate())
                myRef.child(id).setValue(myNote)
                    .addOnSuccessListener {
                        // Write was successful!
                        Toast.makeText(this, "Write was successful!",Toast.LENGTH_LONG).show()
                        alert.dismiss()
                    }
                    .addOnFailureListener {
                        // Write failed
                    }

            }else
                Toast.makeText(this, "Please Enter title & note",Toast.LENGTH_LONG).show()
        }

    }


    fun getCurrentDate():String{
        val calender = Calendar.getInstance()
        val mdFormat = SimpleDateFormat("EEEE hh:mm a ")
        val strDate = mdFormat.format(calender.time)
        return strDate
    }


    fun addDataInList(){
        myRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listNotes.clear()
                for (i in snapshot.children){
                    val note = i.getValue(Note::class.java)
                    listNotes.add(0, Note(note!!.id , note!!.title , note.note , note.timestamp))
                }
                val adapter = CustomAdapter(context , listNotes)
                rv_notes.layoutManager = LinearLayoutManager(context)
                rv_notes.adapter = adapter
                rv_notes.setHasFixedSize(true)
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}




