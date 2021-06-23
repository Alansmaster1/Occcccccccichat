package com.example.occcccccccichat.ui.Msg

import androidx.lifecycle.ViewModel
import com.example.occcccccccichat.data.dao.MessageDao
import com.example.occcccccccichat.data.database.AppDatabase
import com.example.occcccccccichat.data.model.HistoryBean
import java.lang.Appendable
import kotlin.concurrent.thread

class MsgViewModel : ViewModel() {
    var mHistoryList: MutableList<HistoryBean> = ArrayList<HistoryBean>()
    //val messageDao: MessageDao = AppDatabase.getDatabase().messageDao()
}