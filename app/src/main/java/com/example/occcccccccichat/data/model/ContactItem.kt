package com.example.occcccccccichat.data.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["id"],unique = true)])
data class ContactItem (
        var name:String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var nickname: String = name
}