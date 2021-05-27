package com.example.occcccccccichat.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(entity = ContactItem::class,parentColumns = ["id"],childColumns = ["send_id"],onDelete = CASCADE),
                  ForeignKey(entity = ContactItem::class,parentColumns = ["id"],childColumns = ["receive_id"],onDelete = CASCADE)],
    indices = [Index(value = ["send_id"],unique = false),
                Index(value = ["receive_id"],unique = false)]
)
data class ChatMsgItem(var send_id:Long, var receive_id:Long, var content: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}