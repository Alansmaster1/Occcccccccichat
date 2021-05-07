package com.example.occcccccccichat.data

import com.example.occcccccccichat.data.model.ContactItem

class ContactItemList {
    private val _contactItemList = ArrayList<ContactItem>()

    fun addItem(item : ContactItem){
        _contactItemList.add(item)
    }

    fun removeItem(item : ContactItem){
        _contactItemList.remove(item)
    }

    fun getItem(index : Int) : ContactItem{
        return _contactItemList[index]
    }
}