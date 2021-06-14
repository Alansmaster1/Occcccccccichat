package com.example.occcccccccichat.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.occcccccccichat.Tool.MyApplication
import com.example.occcccccccichat.data.dao.ChatMsgItemDao
import com.example.occcccccccichat.data.dao.ContactItemDao
import com.example.occcccccccichat.data.dao.MsgItemDao
import com.example.occcccccccichat.data.model.ChatMsgItem
import com.example.occcccccccichat.data.model.ContactItem
import com.example.occcccccccichat.data.model.MsgItem

@Database(version =5, entities = [MsgItem::class, ContactItem::class, ChatMsgItem::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun msgItemDao(): MsgItemDao
    abstract fun contactItemDao(): ContactItemDao
    abstract fun chatMsgItemDao(): ChatMsgItemDao

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
}