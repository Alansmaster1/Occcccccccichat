package com.example.occcccccccichat.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.occcccccccichat.Tool.AEvent
import com.example.occcccccccichat.Tool.AEvent.AEVENT_RESET
import com.example.occcccccccichat.Tool.IEventListener
import com.example.occcccccccichat.Tool.MyApplication
import com.example.occcccccccichat.data.dao.*
import com.example.occcccccccichat.data.model.*
import java.util.*

@Database(version =13, entities = [ContactItem::class,MessageBean::class,HistoryBean::class])
abstract class AppDatabase : RoomDatabase(){
    abstract fun contactItemDao(): ContactItemDao
    abstract fun messageDao(): MessageDao
    abstract fun historyDao(): HistoryDao

    val HISTORY_TYPE_C2C:String = "c2c"
    val HISTORY_TYPE_GROUP:String = "group"

    companion object{
        private var instance: AppDatabase? = null

        @Synchronized
        fun getDatabase() : AppDatabase{
            instance?.let{
                return it
            }
            return Room.databaseBuilder(
                    MyApplication.context.applicationContext,
                    AppDatabase::class.java,"ochiChat_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build().apply {
                        instance = this
                    }
        }
    }
    //原本要实现IEventListener接口中的方法_用于在触发AEVENT_RESET事件时关闭数据库
}