package com.example.occcccccccichat.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.occcccccccichat.data.model.ChatMsgItem
import com.example.occcccccccichat.data.model.MessageBean
import com.example.occcccccccichat.data.repository.ChatMsgItemListRepository
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class ChatViewModel: ViewModel() {
    val repository =  ChatMsgItemListRepository()

    fun insert(item : ChatMsgItem){
        thread {
            viewModelScope.launch {
                repository.insert(item)
            }
        }
    }

    var mMessageList:MutableList<MessageBean> = ArrayList<MessageBean>()
}