package com.example.occcccccccichat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.occcccccccichat.data.model.MessageBean

@Dao
interface MessageDao {
    @Insert
    fun setMessage(messageBean: MessageBean)

    @Query("select * from MessageBean where targetId=:targetId and fromId=:fromId and msg=:msg")
    fun isInMessageBean(targetId: String,fromId: String,msg:String): List<MessageBean>

    @Query("select * from MessageBean where (targetId=:targetId and fromId=:fromId) or (targetId=:fromId and fromId=:targetId) order by id asc")
    fun getMessageList(targetId : String,fromId : String): List<MessageBean>
}