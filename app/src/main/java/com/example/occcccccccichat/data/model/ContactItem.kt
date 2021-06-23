package com.example.occcccccccichat.data.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["ownId"],unique = false)],primaryKeys = ["ownId", "targetId"])
data class ContactItem (
        var name:String
) {
    var ownId: String = ""
    var targetId: String = ""
    var nickname: String = name
}