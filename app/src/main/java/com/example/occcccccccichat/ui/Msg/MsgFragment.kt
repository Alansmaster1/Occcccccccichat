package com.example.occcccccccichat.ui.Msg

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.occcccccccichat.R
import com.example.occcccccccichat.Tool.AEvent
import com.example.occcccccccichat.Tool.IEventListener
import com.example.occcccccccichat.Tool.MLOC
import com.example.occcccccccichat.data.database.AppDatabase
import com.example.occcccccccichat.databinding.FragmentMsgBinding
import com.example.occcccccccichat.ui.chat.ChatActivity_bak
import com.starrtc.starrtcsdk.api.XHClient
import com.starrtc.starrtcsdk.core.im.message.XHIMMessage
import org.json.JSONException
import org.json.JSONObject

class MsgFragment : Fragment(), IEventListener {

    private lateinit var msgViewModel: MsgViewModel
    private lateinit var RVAdapter: MsgRVAdapter
    private lateinit var binding: FragmentMsgBinding
    private lateinit var mTargetId: String

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


        binding = FragmentMsgBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

//        val btn_change_textView: View = binding.root.findViewById(R.id.btn_change_textView)
//        btn_change_textView.setOnClickListener{
//            msgViewModel.incrementCounter()
//            binding.text = msgViewModel.msg.value?.cnt.toString()
//            LogUtil.d("Debug","plus one")
//        }

        //点击记录,进入聊天界面
        RVAdapter.setOnClickListener(MsgRVAdapter.ViewHolder.ItemClickListener { position: Int ->
            MLOC.addHistory(msgViewModel.mHistoryList.get(position),true)
            mTargetId = msgViewModel.mHistoryList.get(position).conversationId
            val intent = Intent(activity,ChatActivity_bak::class.java)
            startActivity(intent)
        })
        //binding.recyclerviewMsg.adapter = RVAdapter

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        MLOC.hasNewC2CMsg = false
        msgViewModel.mHistoryList.clear()
        //在UI线程访问数据库
        val list = MLOC.getHistoryList(AppDatabase.getDatabase().HISTORY_TYPE_C2C)
        //TODO:原来是size>0
        if(list.size >= 0){
            msgViewModel.mHistoryList.addAll(list)
        }
        RVAdapter.notifyDataSetChanged()
    }

    override fun dispatchEvent(aEventID: String?, success: Boolean, eventObj: Any?) {
//        activity?.apply {
//            when (aEventID) {
//                AEvent.AEVENT_C2C_REV_MSG, AEvent.AEVENT_REV_SYSTEM_MSG -> {
//                    MLOC.hasNewC2CMsg = true
//                    if (findViewById<View>(R.id.c2c_new) != null) {
//                        findViewById<View>(R.id.c2c_new).setVisibility(if (MLOC.hasNewC2CMsg) View.VISIBLE else View.INVISIBLE)
//                    }
//                    if (findViewById<View>(R.id.im_new) != null) {
//                        findViewById<View>(R.id.im_new).setVisibility(if (MLOC.hasNewC2CMsg || MLOC.hasNewGroupMsg) View.VISIBLE else View.INVISIBLE)
//                    }
//                    try {
//                        val revMsg = eventObj as XHIMMessage
//                        val alertData = JSONObject()
//                        alertData.put("listType", 0)
//                        alertData.put("farId", revMsg.fromId)
//                        alertData.put("msg", "收到一条新信息")
//                        MLOC.showDialog(activity, alertData)
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                }
//                AEvent.AEVENT_GROUP_REV_MSG -> {
//                    MLOC.hasNewGroupMsg = true
//                    if (findViewById<View>(R.id.group_new) != null) {
//                        findViewById<View>(R.id.group_new).setVisibility(if (MLOC.hasNewGroupMsg) View.VISIBLE else View.INVISIBLE)
//                    }
//                    if (findViewById<View>(R.id.im_new) != null) {
//                        findViewById<View>(R.id.im_new).setVisibility(if (MLOC.hasNewC2CMsg || MLOC.hasNewGroupMsg) View.VISIBLE else View.INVISIBLE)
//                    }
//                    try {
//                        val revMsg = eventObj as XHIMMessage
//                        val alertData = JSONObject()
//                        alertData.put("listType", 1)
//                        alertData.put("farId", revMsg.targetId)
//                        alertData.put("msg", "收到一条群消息")
//                        MLOC.showDialog(this@BaseActivity, alertData)
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                }
//                AEvent.AEVENT_USER_OFFLINE -> {
//                    MLOC.showMsg(this@BaseActivity, "服务已断开")
//                    (findViewById<View>(R.id.loading) as TextView).text = "连接中..."
//                    if (findViewById<View>(R.id.loading) != null) {
//                        if (XHClient.getInstance().isOnline) {
//                            findViewById<View>(R.id.loading).setVisibility(View.INVISIBLE)
//                        } else {
//                            findViewById<View>(R.id.loading).setVisibility(View.VISIBLE)
//                        }
//                    }
//                }
//                AEvent.AEVENT_USER_ONLINE -> if (findViewById<View>(R.id.loading) != null) {
//                    if (XHClient.getInstance().isOnline) {
//                        findViewById<View>(R.id.loading).setVisibility(View.INVISIBLE)
//                    } else {
//                        findViewById<View>(R.id.loading).setVisibility(View.VISIBLE)
//                    }
//                    (findViewById<View>(R.id.userinfo_head) as ImageView).setImageResource(
//                        MLOC.getHeadImage(
//                            this,
//                            MLOC.userId
//                        )
//                    )
//                    (findViewById<View>(R.id.userinfo_id) as TextView).text = MLOC.userId
//                }
//                AEvent.AEVENT_CONN_DEATH -> {
//                    MLOC.showMsg(this@BaseActivity, "服务已断开")
//                    if (findViewById<View>(R.id.loading) != null) {
//                        (findViewById<View>(R.id.loading) as TextView).text = "连接异常，请重新登录"
//                        if (XHClient.getInstance().isOnline) {
//                            findViewById<View>(R.id.loading).setVisibility(View.INVISIBLE)
//                        } else {
//                            findViewById<View>(R.id.loading).setVisibility(View.VISIBLE)
//                        }
//                        (findViewById<View>(R.id.userinfo_head) as ImageView).setImageResource(
//                            MLOC.getHeadImage(
//                                this,
//                                MLOC.userId
//                            )
//                        )
//                        (findViewById<View>(R.id.userinfo_id) as TextView).text = MLOC.userId
//                    }
//                }
//            }
//        }
//        onResume()
    }

}