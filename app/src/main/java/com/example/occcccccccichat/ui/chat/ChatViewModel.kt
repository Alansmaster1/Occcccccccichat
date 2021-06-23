package com.example.occcccccccichat.ui.chat

import androidx.lifecycle.ViewModel
import com.example.occcccccccichat.data.model.MessageBean

class ChatViewModel: ViewModel() {
    var mMessageList:MutableList<MessageBean> = ArrayList<MessageBean>()
}