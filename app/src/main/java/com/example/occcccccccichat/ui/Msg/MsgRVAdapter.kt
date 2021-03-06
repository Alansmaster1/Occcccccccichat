package com.example.occcccccccichat.ui.Msg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.occcccccccichat.R
import com.example.occcccccccichat.data.model.HistoryBean

class MsgRVAdapter(private val _dataSet: MutableList<HistoryBean>)
    : RecyclerView.Adapter<MsgRVAdapter.ViewHolder>() {

    private var itemClickListener: ViewHolder.ItemClickListener? = null
    private var itemLongClickListener: ViewHolder.ItemLongClickListener? = null

    class ViewHolder(view: View, clickListener: ItemClickListener?, longClickListener: ItemLongClickListener?):
        RecyclerView.ViewHolder(view),View.OnClickListener,View.OnLongClickListener {

        //ViewHolder中的控件需要改,其他的大概率不变
        val idTextView:TextView = view.findViewById(R.id.item_msg_id)
        val lastMsgTextView: TextView = view.findViewById(R.id.item_msg_message)
        val lastTimeTextView:TextView = view.findViewById(R.id.item_msg_time)
        var mItemClickListener: ItemClickListener? = clickListener
        var mItemLongClickListener: ItemLongClickListener? = longClickListener

        init{
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        fun interface ItemClickListener{
            fun onItemClick(layoutPosition: Int)
        }

        fun interface ItemLongClickListener{
            fun onItemLongClick(layoutPosition:Int)
        }

        override fun onClick(view: View) {
            mItemClickListener?.onItemClick(layoutPosition)
        }

        override fun onLongClick(view: View): Boolean {
            mItemLongClickListener?.onItemLongClick(layoutPosition)
            //长按不触发点击事件
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_msg,parent,false)

        return ViewHolder(view,itemClickListener,itemLongClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.idTextView.text = _dataSet.get(position).fromId
        holder.lastMsgTextView.text = _dataSet.get(position).lastMsg
        holder.lastTimeTextView.text = _dataSet.get(position).lastTime
    }

    override fun getItemCount(): Int {
        return _dataSet.size
    }

    fun setOnClickListener(listener: ViewHolder.ItemClickListener){
        itemClickListener = listener
    }

    fun setOnLongClickListener(listener: ViewHolder.ItemLongClickListener) {
        itemLongClickListener = listener
    }
}