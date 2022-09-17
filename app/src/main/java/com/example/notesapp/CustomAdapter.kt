package com.example.notesapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.custom_list.view.*
import kotlinx.android.synthetic.main.custom_update_note.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CustomAdapter(private val context: Context , private val myList: ArrayList<Note>) : RecyclerView.Adapter<CustomAdapter.NoteViewHolder>() {
    val database = Firebase.database
    val myRef = database.getReference().child("Notes")


     class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val noteTitle = itemView.tv_titleNote
        val noteTime = itemView.tv_timeNote
        fun bind(note:Note){
            noteTitle.text = note.title
            noteTime.text = note.timestamp
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_list , parent,false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = myList[position]
        holder.bind(note)
        holder.itemView.setOnClickListener{
            val intent = Intent(context , ShowNoteActivity::class.java)
            intent.putExtra("title",note.title)
            intent.putExtra("note",note.note)
            context.startActivity(intent)
        }


        holder.itemView.setOnLongClickListener  { v ->
//            val selectNote = myList[position-1]
            showDialogDeleteAndUpdateNotes(note)
            false
        }
    }

    override fun getItemCount(): Int {
       return myList.size
    }


    fun showDialogDeleteAndUpdateNotes(note:Note){
        val view = LayoutInflater.from(context).inflate(R.layout.custom_update_note,null,false)

        val dialog = AlertDialog.Builder(context)
        dialog.setView(view)
        dialog.setTitle(R.string.delOrUpdate)
        val alert = dialog.create()
        alert.show()

        view.btn_delete.setOnClickListener {
            myRef.child(note.id).removeValue()
            alert.dismiss()
        }

        view.btn_update.setOnClickListener {
            view.et_update_title.visibility = View.VISIBLE
            view.et_update_note.visibility = View.VISIBLE
            val data_title = view.et_update_title.text.toString()
            val data_note = view.et_update_note.text.toString()

            if(data_title.isEmpty() && data_note.isEmpty()){
                view.et_update_title.setText(note.title)
                view.et_update_note.setText(note.note)
            }

            if (data_title.isNotEmpty() && data_note.isNotEmpty()) {
                val childRef = myRef.child(note.id)
                val noteAfterUpdate = Note(note.id , data_title , data_note , getCurrentDate())
                childRef.setValue(noteAfterUpdate)
                alert.dismiss()
            }else
                Toast.makeText(context, "Please Enter title & note",Toast.LENGTH_LONG).show()

        }

    }

    fun getCurrentDate():String{
        val calender = Calendar.getInstance()
        val mdFormat = SimpleDateFormat("EEEE hh:mm a ")
        val strDate = mdFormat.format(calender.time)
        return strDate
    }

}