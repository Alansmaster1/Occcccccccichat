package com.example.occcccccccichat.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.*
import com.example.occcccccccichat.data.model.ContactItem
import java.security.Key

@Dao
interface ContactItemDao {
    @Insert
    fun insertItem(item : ContactItem) : Long

    @Update
    fun updateItem(item : ContactItem)

    @Delete
    fun deleteItem(item : ContactItem)

    @Query("select * from ContactItem where id = :id LIMIT 1")
    fun queryById(id : Long): ContactItem

    @Query("select * from ContactItem")
    fun queryAllItem(): LiveData<List<ContactItem>>

    @Query("select * from ContactItem")
    fun queryAllItemForPaging():PagingSource<Int,ContactItem>

    @Query("delete from ContactItem")
    fun deleteAllItem()
}