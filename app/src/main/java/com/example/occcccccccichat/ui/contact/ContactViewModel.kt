package com.example.occcccccccichat.ui.contact

import androidx.annotation.ArrayRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.occcccccccichat.Tool.LogUtil
import com.example.occcccccccichat.data.dao.ContactItemDao
import com.example.occcccccccichat.data.database.AppDatabase
import com.example.occcccccccichat.data.model.ContactItem

class ContactViewModel : ViewModel() {

//    val contactItemArrayList: ArrayList<ContactItem> = ArrayList()
//    val bkContactItemArrayList: ArrayList<ContactItem> = ArrayList()

    val contactItemDao = AppDatabase.getDatabase().contactItemDao()
    val contactItemList: LiveData<List<ContactItem>> = contactItemDao.queryAllItem()

    fun insert(item : ContactItem){
        contactItemDao.insertItem(item)
    }

    fun update(item : ContactItem){
        contactItemDao.updateItem(item)
    }

    fun delete(item : ContactItem){
        contactItemDao.deleteItem(item)
    }

    fun deleteAll(){
         contactItemDao.deleteAllItem()
    }
}