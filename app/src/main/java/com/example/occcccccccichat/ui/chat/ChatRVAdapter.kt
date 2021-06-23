package com.example.occcccccccichat.ui.chat

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.occcccccccichat.R
import com.example.occcccccccichat.Tool.MLOC
import com.example.occcccccccichat.data.model.MessageBean

class ChatRVAdapter(private val _dataSet: List<MessageBean>)
    :RecyclerView.Adapter<ChatRVAdapter.ViewHolder>() {

    private var itemLongClickListener: ViewHolder.ItemLongClickListener? = null

    class ViewHolder(view: View, longClickListener: ItemLongClickListener?):
        RecyclerView.ViewHolder(view), View.OnLongClickListener {

        val messageTextView: TextView = view.findViewById(R.id.item_chat_message)
        val opIdTextView:TextView = view.findViewById<TextView>(R.id.item_chat_id_op)
        val selfIdTextView:TextView = view.findViewById<TextView>(R.id.item_chat_id_self)
        val timeTextView:TextView = view.findViewById<TextView>(R.id.item_chat_time)
        val layoutOp:View = view.findViewById(R.id.layout_chat_op)
        val layoutSelf:View = view.findViewById(R.id.layout_chat_self)
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
        if(_dataSet.get(position).fromId == MLOC.userId){
            holder.opIdTextView.text = _dataSet.get(position).targetId
            holder.layoutOp.visibility = View.GONE
            holder.layoutSelf.visibility = View.VISIBLE
            holder.messageTextView.gravity = Gravity.END
        } else {
            holder.opIdTextView.text = _dataSet.get(position).fromId
            holder.layoutOp.visibility = View.VISIBLE
            holder.layoutSelf.visibility = View.GONE
            holder.messageTextView.gravity = Gravity.START
        }
        holder.selfIdTextView.text = MLOC.userId
        holder.timeTextView.text = _dataSet.get(position).time
        holder.messageTextView.text = _dataSet.get(position).msg
    }

    override fun getItemCount(): Int {
        return _dataSet.size
    }

    fun setOnLongClickListener(listener: ViewHolder.ItemLongClickListener) {
        itemLongClickListener = listener
    }
}