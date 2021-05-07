package com.example.occcccccccichat.ui.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.occcccccccichat.R
import com.example.occcccccccichat.data.model.ContactItem

class ContactRVAdapter(private val _dataSet: LiveData<List<ContactItem>>)
    : RecyclerView.Adapter<ContactRVAdapter.ViewHolder>() {

    private var _ItemClickListener: ViewHolder.ItemClickListener? = null

    class ViewHolder(view: View):
            RecyclerView.ViewHolder(view),View.OnClickListener,View.OnLongClickListener {
        val textView: TextView = view.findViewById(R.id.name_item_contact)
        var mItemClickListener: ItemClickListener? = null
        var mItemLongClickListener: ItemLongClickListener? = null

        interface ItemClickListener{
            fun onItemClick(view:View,id:Int,position:Int)
        }

        interface ItemLongClickListener{
            fun onItemLongClick(view:View,id:Int,position:Int)
        }

        override fun onClick(view: View) {
            mItemClickListener?.apply {
                onItemClick(view,view.id,layoutPosition)
            }
        }

        override fun onLongClick(view: View): Boolean {
            mItemLongClickListener?.apply {
                onItemLongClick(view,view.id,layoutPosition)
            }
            //长按不触发点击事件
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contact,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = _dataSet.value!!.get(position).Name
    }

    override fun getItemCount(): Int {
        _dataSet.value?.apply {
            return size
        }
        return 0
    }

    fun setOnClickListener(listener: ContactRVAdapter.ViewHolder.ItemClickListener){
        _ItemClickListener = listener
    }
}