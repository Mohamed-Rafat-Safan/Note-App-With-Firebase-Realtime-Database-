package com.example.notesapp

import java.sql.Timestamp
import java.util.*

class Note{
    lateinit var id:String
    lateinit var title:String
    lateinit var note:String
    lateinit var timestamp: String

    constructor(){
    }

    constructor(id: String, title: String, note: String, timestamp: String) {
        this.id = id
        this.title = title
        this.note = note
        this.timestamp = timestamp
    }


}
