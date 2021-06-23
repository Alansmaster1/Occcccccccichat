package com.example.occcccccccichat.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.occcccccccichat.Tool.MLOC
import com.example.occcccccccichat.data.dao.ContactItemDao
import com.example.occcccccccichat.data.database.AppDatabase
import com.example.occcccccccichat.data.model.ContactItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactItemListRepository {
    private val contactItemDao: ContactItemDao = AppDatabase.getDatabase().contactItemDao()
    val contactItemList: LiveData<List<ContactItem>> = contactItemDao.queryAllItemInOwner(MLOC.userId)

    suspend fun insert(item : ContactItem): Long{
        return withContext(Dispatchers.IO){
            contactItemDao.insertItem(item)
        }
    }

    suspend fun query(ownId : String, targetId:String) : ContactItem{
        return withContext(Dispatchers.IO){
            contactItemDao.queryById(ownId,targetId)
        }
    }

    suspend fun update(item: ContactItem) {
        return withContext(Dispatchers.IO) {
            contactItemDao.updateItem(item)
        }
    }
}