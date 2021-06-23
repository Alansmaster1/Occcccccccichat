package com.example.occcccccccichat.ui.contact

import androidx.recyclerview.widget.DiffUtil
import com.example.occcccccccichat.data.model.ContactItem

// 不使用这个类
// 在 ViewModel + RV 中使用了LiveData
// 这样一来, 就没有新/旧数组用于进行比较

class ContactRVDiffCallBack(
        private val _oldList:ArrayList<ContactItem>,
        private val _newList:ArrayList<ContactItem>
) :DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return _oldList.size
    }

    override fun getNewListSize(): Int {
        return _newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return _oldList[oldItemPosition].targetId == _newList[newItemPosition].targetId
                && _oldList[oldItemPosition].ownId == _newList[newItemPosition].ownId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return _oldList[oldItemPosition].name == _newList[newItemPosition].name
    }
}