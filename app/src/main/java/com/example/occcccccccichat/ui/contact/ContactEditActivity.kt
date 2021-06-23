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
import com.example.occcccccccichat.Tool.MLOC
import com.example.occcccccccichat.data.model.ContactItem
import com.example.occcccccccichat.databinding.ActivityContactEditBinding

class ContactEditActivity : AppCompatActivity() {

    private val viewModel: ContactViewModel by viewModels()
    private lateinit var binding: ActivityContactEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactEditBinding.inflate(layoutInflater)
        operationTypeCheck()
        setContentView(binding.root)
    }

    private fun accessInfo(targetId : String): MutableLiveData<ContactItem>{
        val item: MutableLiveData<ContactItem> = MutableLiveData()
        viewModel.getItem(MLOC.userId,targetId,item)
        item.observe(this, Observer {
            binding.nameTextView.setText(item.value?.name)
            binding.nicknameEditText.setText(item.value?.nickname)
            binding.idTextView.text = item.value?.targetId.toString()
        })
        return item
    }

    private fun operationTypeCheck(){
        val id: String? = intent.getStringExtra("targetId")
        if(id==null){
            binding.nameTextView.isEnabled = true
            binding.idTextView.visibility = View.INVISIBLE
            binding.idLabel.visibility = View.INVISIBLE
            binding.saveBtn.setOnClickListener {
                val item = ContactItem(binding.nameTextView.text.toString())
                item.nickname = binding.nicknameEditText.text.toString()
                viewModel.insert(item)
                finish()
            }
        } else {
            val item: MutableLiveData<ContactItem> = accessInfo(id)
            binding.nameTextView.isEnabled = false
            binding.idTextView.visibility = View.VISIBLE
            binding.idLabel.visibility = View.VISIBLE
            binding.saveBtn.setOnClickListener{
                item.value?.let { it1 ->
                    it1.nickname = binding.nicknameEditText.text.toString()
                    viewModel.updateItem(it1)
                }
                finish()
            }
        }
    }
}