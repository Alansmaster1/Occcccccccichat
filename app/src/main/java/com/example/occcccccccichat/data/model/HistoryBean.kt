package com.example.occcccccccichat.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryBean(
    var type:String = "",
    var fromId:String = "",
    var targetId:String = "",
    var newMsgCount:Int = 0,
    var lastMsg:String = "",
    var lastTime:String = "",
    var groupName:String = "",
    var groupCreatorId:String = ""
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
