package com.example.occcccccccichat.ui.contact

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.occcccccccichat.R
import com.example.occcccccccichat.Tool.LogUtil
import com.example.occcccccccichat.Tool.MyApplication
import com.example.occcccccccichat.data.model.ContactItem
import com.example.occcccccccichat.databinding.FragmentContactBinding
import com.example.occcccccccichat.ui.chat.ChatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.concurrent.thread

class ContactFragment : Fragment() {

    private lateinit var viewModel: ContactViewModel
    private lateinit var RVAdapter: ContactRVAdapter
    private lateinit var binding: FragmentContactBinding

    var _cnt = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(ContactViewModel::class.java)

        binding = FragmentContactBinding.inflate(inflater,container,false)

        viewModel.repository.contactItemList.observe(viewLifecycleOwner,  {
            RVAdapter.notifyDataSetChanged()
        })

        binding.btnAddItemContact.setOnClickListener{
            viewModel.insert(ContactItem("Alan ${_cnt++}"))
            LogUtil.d("Debug","Add an item to Contact RV")
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initAdapter()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        val linearLayoutManager = LinearLayoutManager(MyApplication.context)
        val dividerItemDecoration = DividerItemDecoration(MyApplication.context,DividerItemDecoration.VERTICAL)
        linearLayoutManager.orientation = RecyclerView.VERTICAL

        binding.recyclerviewContact.apply {
            addItemDecoration(dividerItemDecoration)
            setHasFixedSize(true)
            adapter = RVAdapter
            layoutManager = linearLayoutManager
            LogUtil.d("Debug","Init RecyclerView in Contact Fragment")
        }
    }

    private fun initAdapter(){
        RVAdapter = ContactRVAdapter(viewModel.repository.contactItemList)
        RVAdapter.run {
            setOnClickListener(ContactRVAdapter.ViewHolder.ItemClickListener{ layoutPosition: Int ->
                LogUtil.d("Debug","Item is clicked")
                //TODO: Start the Chat Activity
                val intent = Intent(activity,ChatActivity::class.java)
                intent.putExtra("ID",viewModel.repository.contactItemList.value?.get(layoutPosition)?.id)
                startActivity(intent)
            })

            setOnLongClickListener(ContactRVAdapter.ViewHolder.ItemLongClickListener {  layoutPosition ->
                LogUtil.d("Debug","Item is long clicked")
                //Start the Modify Activity
                val intent = Intent(activity,ContactEditActivity::class.java)
                intent.putExtra("ID",viewModel.repository.contactItemList.value?.get(layoutPosition)?.id)
                startActivity(intent)
            })
        }
        LogUtil.d("Debug","Init Adapter for Contact RV List")
    }
}