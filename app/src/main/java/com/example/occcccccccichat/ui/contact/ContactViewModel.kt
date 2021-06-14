package com.example.occcccccccichat.ui.contact

import androidx.annotation.ArrayRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.occcccccccichat.Tool.LogUtil
import com.example.occcccccccichat.data.dao.ContactItemDao
import com.example.occcccccccichat.data.database.AppDatabase
import com.example.occcccccccichat.data.model.ContactItem
import com.example.occcccccccichat.data.repository.ContactItemListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class ContactViewModel : ViewModel() {
    val repository = ContactItemListRepository()

    fun insert(item: ContactItem) {
        thread() {
            viewModelScope.launch {
                repository.insert(item)
            }
        }
    }

    fun getItem(id:Long, item: MutableLiveData<ContactItem>) {
        viewModelScope.launch {
            item.postValue(repository.query(id))
        }
    }

    fun updateItem(item:ContactItem){
        viewModelScope.launch {
            repository.update(item)
        }
    }
}