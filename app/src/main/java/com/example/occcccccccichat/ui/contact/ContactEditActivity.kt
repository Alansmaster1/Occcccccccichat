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

        val id = intent.getLongExtra("ID",0)
        LogUtil.d("Debug","ID is $id.")

        val item: MutableLiveData<ContactItem> = MutableLiveData()

        viewModel.getItem(id,item)
        item.observe(this, Observer {
            binding.nameTextView.text = item.value?.name
            binding.nicknameEditText.setText(item.value?.nickname)
            binding.idTextView.text = item.value?.id.toString()
        })

        binding.saveBtn.setOnClickListener{
            item.value?.run {
                nickname = binding.nicknameEditText.text.toString()
                viewModel.updateItem(this)
            }
        }
    }
}