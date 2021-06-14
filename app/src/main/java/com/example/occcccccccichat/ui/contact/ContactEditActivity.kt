package com.example.occcccccccichat.ui.contact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.*
import com.example.occcccccccichat.Tool.LogUtil
import com.example.occcccccccichat.data.model.ContactItem
import com.example.occcccccccichat.databinding.ActivityContactEditBinding

class ContactEditActivity : AppCompatActivity() {

    private val viewModel: ContactViewModel by viewModels()
    private lateinit var binding: ActivityContactEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun accessInfo(id : Long): MutableLiveData<ContactItem>{
        val item: MutableLiveData<ContactItem> = MutableLiveData()
        viewModel.getItem(id,item)
        item.observe(this, Observer {
            binding.nameTextView.text = item.value?.name
            binding.nicknameEditText.setText(item.value?.nickname)
            binding.idTextView.text = item.value?.id.toString()
        })
        return item
    }

    fun operationTypeCheck(){
        val id: Long = intent.getLongExtra("ID",-1);
        if(id==-1L){
            binding.saveBtn.setOnClickListener {
                val item: ContactItem = ContactItem(binding.nameTextView.text as String)
                item.nickname = binding.nicknameEditText.text.toString()
                viewModel.insert(item)
            }
        } else {
            val item: MutableLiveData<ContactItem> = accessInfo(id)
            binding.saveBtn.setOnClickListener{
                item.value?.let { it1 -> viewModel.updateItem(it1) }
            }
        }
    }
}