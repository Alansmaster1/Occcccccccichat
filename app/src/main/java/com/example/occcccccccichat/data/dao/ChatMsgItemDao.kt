package com.example.occcccccccichat.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.occcccccccichat.data.model.ChatMsgItem

@Dao
interface ChatMsgItemDao {
    @Insert
    fun insertItem(item : ChatMsgItem) : Long

    @Update
    fun updateItem(item : ChatMsgItem)

    @Delete
    fun deleteItem(item : ChatMsgItem)

    @Query("select * from ChatMsgItem")
    fun queryAllItem() : LiveData<List<ChatMsgItem>>

    @Query("delete from ChatMsgItem")
    fun deleteAllItem()
}