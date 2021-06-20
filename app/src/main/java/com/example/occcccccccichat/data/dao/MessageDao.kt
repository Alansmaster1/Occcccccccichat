package com.example.occcccccccichat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.occcccccccichat.data.model.MessageBean

@Dao
interface MessageDao {
    @Insert
    fun setMessage(messageBean: MessageBean)

    @Query("select * from MessageBean where conversationId=:conversationId order by id desc limit 5")
    fun getMessageList(conversationId : String): List<MessageBean>
}