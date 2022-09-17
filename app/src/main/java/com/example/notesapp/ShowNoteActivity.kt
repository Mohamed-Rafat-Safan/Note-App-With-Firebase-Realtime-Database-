package com.example.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_show_note.*

class ShowNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_note)

        val animation2 = AnimationUtils.loadAnimation(this,R.anim.zoomin)
        tv_show_title.startAnimation(animation2)

        val title = intent.getStringExtra("title")
        val note = intent.getStringExtra("note")

        tv_show_title.text = title
        tv_show_note.text = note
    }
}