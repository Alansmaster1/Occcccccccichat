package com.example.occcccccccichat.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.occcccccccichat.R
import com.example.occcccccccichat.Tool.LogUtil
import com.example.occcccccccichat.Tool.MyApplication
import com.example.occcccccccichat.data.model.ChatMsgItem
import com.example.occcccccccichat.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var RVAdatper: ChatRVAdapter
    private lateinit var binding: ActivityChatBinding

    private val self_id = 1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendBtn.setOnClickListener {
            //TODO: send message
            val id = intent.getLongExtra("ID",0)
            if(id != 0L){
                viewModel.insert(ChatMsgItem(self_id,id,binding.inputEditText.text.toString()))
            }
        }

        viewModel.repository.chatMsgItemList.observe(this, Observer{
            RVAdatper.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        super.onResume()
        initAdapter()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        val linearLayoutManager = LinearLayoutManager(MyApplication.context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL

        binding.chatMsgRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = RVAdatper
            LogUtil.d("Debug","Init RecyclerView in ChatActivity")
        }
    }

    private fun initAdapter(){
        RVAdatper = ChatRVAdapter(viewModel.repository.chatMsgItemList)
        RVAdatper.run {
            setOnLongClickListener(ChatRVAdapter.ViewHolder.ItemLongClickListener{
                LogUtil.d("Debug","ChatMsg is long clicked.")
            })
        }
        LogUtil.d("Debug","Init RVAdapter in ChatActivity")
    }
}