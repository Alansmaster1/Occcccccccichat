package com.example.occcccccccichat.data.dao

import androidx.room.*
import com.example.occcccccccichat.data.model.HistoryBean

@Dao
interface HistoryDao {
    @Query("select * from HistoryBean where type = :type order by id desc")
    fun getHistory(type: String): List<HistoryBean>

    @Update
    fun updateHistory(historyBean: HistoryBean)

    fun addHistory(historyBean: HistoryBean, hasRead: Boolean){
        if(historyBean.conversationId == "" || historyBean.type == "" )
            return
        val qlist: List<HistoryBean> = addHistoryFun1(historyBean.type,historyBean.conversationId)
        if(qlist.isNotEmpty()){
            historyBean.id = qlist[0].id
            if(!hasRead){
                historyBean.newMsgCount = qlist[0].newMsgCount + 1
            } else {
                historyBean.newMsgCount = 0
            }
            addHistoryFun2(historyBean)
        } else {
            if(hasRead) {
                historyBean.newMsgCount = 0
            }
            addHistoryFun3(historyBean)
        }
    }

    @Query("select * from HistoryBean where type=:type and conversationId=:conversationId")
    fun addHistoryFun1(type: String, conversationId:String): List<HistoryBean>

    @Update
    fun addHistoryFun2(historyBean: HistoryBean)

    @Insert
    fun addHistoryFun3(historyBean: HistoryBean)

    @Delete
    fun removeHistory(historyBean: HistoryBean)
}