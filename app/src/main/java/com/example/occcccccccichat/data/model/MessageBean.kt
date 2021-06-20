package com.example.occcccccccichat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageBean(
    var conversationId:String = "",
    var fromId:String = "",
    var msg:String = "",
    var time:String = ""
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
