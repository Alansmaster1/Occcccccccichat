package com.example.occcccccccichat.data.repository

import androidx.lifecycle.LiveData
import com.example.occcccccccichat.data.dao.ChatMsgItemDao
import com.example.occcccccccichat.data.database.AppDatabase
import com.example.occcccccccichat.data.model.ChatMsgItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatMsgItemListRepository {
    private val chatMsgItemDao: ChatMsgItemDao = AppDatabase.getDatabase().chatMsgItemDao()
    val chatMsgItemList: LiveData<List<ChatMsgItem>> = chatMsgItemDao.queryAllItem()

    suspend fun insert(item: ChatMsgItem): Long {
        return withContext(Dispatchers.IO){
            chatMsgItemDao.insertItem(item)
        }
    }
}