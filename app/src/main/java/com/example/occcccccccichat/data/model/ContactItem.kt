package com.example.occcccccccichat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactItem (
        var Name:String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}