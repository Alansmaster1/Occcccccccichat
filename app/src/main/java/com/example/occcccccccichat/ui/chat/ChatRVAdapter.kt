package com.example.occcccccccichat.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.occcccccccichat.R
import com.example.occcccccccichat.data.model.ChatMsgItem
import com.example.occcccccccichat.data.model.MessageBean

class ChatRVAdapter(private val _dataSet: List<MessageBean>)
    :RecyclerView.Adapter<ChatRVAdapter.ViewHolder>() {

    private var itemLongClickListener: ViewHolder.ItemLongClickListener? = null

    class ViewHolder(view: View, longClickListener: ItemLongClickListener?):
        RecyclerView.ViewHolder(view), View.OnLongClickListener {

        val contentTextView: TextView = view.findViewById(R.id.content_TextView)
        val op_avatar = view.findViewById<ImageView>(R.id.op_avatar)
        val self_avatar = view.findViewById<ImageView>(R.id.self_avatar)
        var mItemLongClickListener: ItemLongClickListener? = longClickListener

        init{
            view.setOnLongClickListener(this)
        }

        fun interface ItemLongClickListener{
            fun onItemLongClick(layoutPosition:Int)
        }

        override fun onLongClick(view: View): Boolean {
            mItemLongClickListener?.onItemLongClick(layoutPosition)
            //长按不触发点击事件
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_msg,parent,false)

        return ViewHolder(view,itemLongClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.contentTextView.text = _dataSet.get(position).msg
    }

    override fun getItemCount(): Int {
        return _dataSet.size
    }

    fun setOnLongClickListener(listener: ViewHolder.ItemLongClickListener) {
        itemLongClickListener = listener
    }
}