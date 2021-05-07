package com.example.occcccccccichat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MsgItem (
        var message: String
) {
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0
}