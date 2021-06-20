package com.example.occcccccccichat.ui.Msg

import androidx.lifecycle.ViewModel
import com.example.occcccccccichat.data.dao.MessageDao
import com.example.occcccccccichat.data.database.AppDatabase
import com.example.occcccccccichat.data.model.HistoryBean
import java.lang.Appendable
import kotlin.concurrent.thread

class MsgViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text
//
//    fun changeText(){
//        _text.value = Random(12).toString()
//    }

    var mHistoryList: MutableList<HistoryBean> = ArrayList<HistoryBean>()
    val messageDao: MessageDao = AppDatabase.getDatabase().messageDao()
}