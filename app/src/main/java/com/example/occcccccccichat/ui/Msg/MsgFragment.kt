package com.example.occcccccccichat.ui.Msg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.occcccccccichat.R
import com.example.occcccccccichat.databinding.FragmentMsgBinding

class MsgFragment : Fragment() {

    private lateinit var msgViewModel: MsgViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        msgViewModel =
            ViewModelProvider(this).get(MsgViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_msg, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        msgViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

//        val binding:FragmentMsgBinding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_msg,container,false)
//        binding.text = ""

        val model: MsgViewModel by viewModels()

        val binding = FragmentMsgBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

//        val btn_change_textView: View = binding.root.findViewById(R.id.btn_change_textView)
//        btn_change_textView.setOnClickListener{
//            msgViewModel.incrementCounter()
//            binding.text = msgViewModel.msg.value?.cnt.toString()
//            LogUtil.d("Debug","plus one")
//        }


        return binding.root
    }
}