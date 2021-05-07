package com.example.occcccccccichat.ui.contact

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
import kotlinx.android.synthetic.main.fragment_contact.view.*
import kotlin.concurrent.thread

class ContactFragment : Fragment() {

    private lateinit var _contactViewModel: ContactViewModel
    private lateinit var _contactRVAdapter: ContactRVAdapter

    var _cnt = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _contactViewModel =
            ViewModelProvider(this).get(ContactViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_contact, container, false)

        _contactViewModel.contactItemList.observe(viewLifecycleOwner, Observer {
            _contactRVAdapter.notifyDataSetChanged()
        })

        root.btn_add_item_contact.setOnClickListener {
            thread {
                _contactViewModel.insert(ContactItem("Alan${_cnt++}"))
                LogUtil.d("Debug","Add Item to Contact RV List")
            }
        }
        return root
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

        val recyclerView: RecyclerView? = view?.findViewById(R.id.recyclerview_contact)
        recyclerView?.apply {
            addItemDecoration(dividerItemDecoration)
            setHasFixedSize(true)
            adapter = _contactRVAdapter
            layoutManager = linearLayoutManager
            LogUtil.d("Debug","Init RecyclerView in Contact Fragment")
        }
    }

    private fun initAdapter(){
        _contactRVAdapter = ContactRVAdapter(_contactViewModel.contactItemList)

        LogUtil.d("Debug","Init Adapter for Contact RV List")
    }
}