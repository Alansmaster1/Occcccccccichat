package com.example.occcccccccichat.ui.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.occcccccccichat.R
import com.example.occcccccccichat.data.model.ContactItem

class ContactSearchRVAdapter(private var _dataSet: List<ContactItem>)
    :RecyclerView.Adapter<ContactSearchRVAdapter.ViewHolder>() {
    private var itemClickListener: ViewHolder.ItemClickListener? = null
    private var itemLongClickListener: ViewHolder.ItemLongClickListener? = null

    class ViewHolder(view: View, clickListener: ItemClickListener?, longClickListener: ItemLongClickListener?):
        RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {

        //ViewHolder中的控件需要改,其他的大概率不变
        val textView: TextView = view.findViewById(R.id.name_item_contact)
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
            .inflate(R.layout.item_contact,parent,false)

        return ViewHolder(view,itemClickListener,itemLongClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = _dataSet.get(position).name
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

    fun setData(newDataSet: List<ContactItem>){
        _dataSet = newDataSet
    }
}