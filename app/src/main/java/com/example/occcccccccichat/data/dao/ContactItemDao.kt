package com.example.occcccccccichat.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.occcccccccichat.data.model.ContactItem

@Dao
interface ContactItemDao {
    @Insert
    fun insertItem(item : ContactItem) : Long

    @Update
    fun updateItem(item : ContactItem)

    @Delete
    fun deleteItem(item : ContactItem)

    @Query("select * from ContactItem where id = :id")
    fun queryById(id : Long): List<ContactItem>

    @Query("select * from ContactItem")
    fun queryAllItem(): LiveData<List<ContactItem>>

    @Query("delete from ContactItem")
    fun deleteAllItem()
}