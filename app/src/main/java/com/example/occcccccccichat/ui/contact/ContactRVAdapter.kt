package com.example.occcccccccichat.ui.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.occcccccccichat.R
import com.example.occcccccccichat.data.model.ContactItem

class ContactRVAdapter(private val _dataSet: LiveData<List<ContactItem>>)
    : RecyclerView.Adapter<ContactRVAdapter.ViewHolder>() {

    private var itemClickListener: ViewHolder.ItemClickListener? = null
    private var itemLongClickListener: ViewHolder.ItemLongClickListener? = null

    class ViewHolder(view: View, clickListener: ItemClickListener?, longClickListener: ItemLongClickListener?):
            RecyclerView.ViewHolder(view),View.OnClickListener,View.OnLongClickListener {

        val nameTextView: TextView = view.findViewById(R.id.name_item_contact)
        val idTextView:TextView = view.findViewById(R.id.id_item_contact)
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
        holder.idTextView.text = _dataSet.value?.get(position)?.targetId
        holder.nameTextView.text = _dataSet.value?.get(position)?.nickname
    }

    override fun getItemCount(): Int {
        _dataSet.value?.apply {
            return size
        }
        return 0
    }

    fun setOnClickListener(listener: ViewHolder.ItemClickListener){
        itemClickListener = listener
    }

    fun setOnLongClickListener(listener: ViewHolder.ItemLongClickListener) {
        itemLongClickListener = listener
    }
}