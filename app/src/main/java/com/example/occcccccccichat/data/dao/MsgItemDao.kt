package com.example.occcccccccichat.data.dao

import androidx.room.*
import com.example.occcccccccichat.data.model.MsgItem

@Dao
interface MsgItemDao {
    @Insert
    fun insertItem(item : MsgItem): Long

    @Update
    fun updateItem(item : MsgItem)

    @Delete
    fun deleteItem(item : MsgItem)

    @Query("select * from MsgItem where id = :id")
    fun queryById(id : Long) : List<MsgItem>
}