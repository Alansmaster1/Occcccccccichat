package com.example.occcccccccichat.ui.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.IpSecManager;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.example.occcccccccichat.Tool.AEvent;
import com.example.occcccccccichat.Tool.IEventListener;
import com.example.occcccccccichat.Tool.MLOC;
import com.example.occcccccccichat.Tool.MyApplication;
import com.example.occcccccccichat.data.database.AppDatabase;
import com.example.occcccccccichat.data.model.HistoryBean;
import com.example.occcccccccichat.data.model.MessageBean;
import com.example.occcccccccichat.databinding.ActivityChatBinding;
import com.starrtc.starrtcsdk.api.XHClient;
import com.starrtc.starrtcsdk.apiInterface.IXHResultCallback;
import com.starrtc.starrtcsdk.core.im.message.XHIMMessage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements IEventListener {
    private ChatViewModel viewModel;
    private ChatRVAdapter adapter;
    private ActivityChatBinding binding;

    private String mTargetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        binding = ActivityChatBinding.inflate(getLayoutInflater());

        addListener();
        mTargetId = getIntent().getStringExtra("targetId");

        adapter = new ChatRVAdapter(viewModel.getMMessageList());
        initRecyclerView();

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.inputEditText.getText().toString();
                if(!text.equals("")){
                    sendMsg(text);
                    binding.inputEditText.setText("");
                }
            }
        });

        setContentView(binding.getRoot());
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MyApplication.context,DividerItemDecoration.VERTICAL);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        binding.chatMsgRecyclerView.addItemDecoration(dividerItemDecoration);
        binding.chatMsgRecyclerView.setHasFixedSize(true);
        binding.chatMsgRecyclerView.setAdapter(adapter);
        binding.chatMsgRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        addListener();
    }

    //恢复数据,要改一下,数据存储在viewModel中
    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getMMessageList().clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MessageBean> list = MLOC.getMessageList(mTargetId,MLOC.userId);
                if(list!=null && list.size()>0){
                    viewModel.getMMessageList().addAll(list);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    @Override
    protected void onStop() {
        AEvent.removeListener(AEvent.AEVENT_C2C_REV_MSG,this);
        AEvent.removeListener(AEvent.AEVENT_REV_SYSTEM_MSG,this);
        super.onStop();
    }

    private void addListener(){
        AEvent.addListener(AEvent.AEVENT_C2C_REV_MSG,this);
        AEvent.addListener(AEvent.AEVENT_REV_SYSTEM_MSG,this);
    }

    private void sendMsg(String msg){
        XHIMMessage message = XHClient.getInstance().getChatManager().sendMessage(msg, mTargetId, new IXHResultCallback() {
            @Override
            public void success(Object data) {
                MLOC.d("IM_C2C 成功","消息序号: "+data);
            }

            @Override
            public void failed(String errMsg) {
                MLOC.d("IM_C2C  失败","消息序号："+errMsg);
            }
        });

        String type = AppDatabase.Companion.getDatabase().getHISTORY_TYPE_C2C();
        String lastTime = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new java.util.Date());
        String lastMsg = message.contentData;

        HistoryBean historyBean = new HistoryBean(type,message.fromId,message.targetId,1,lastMsg,lastTime,"","");

        MessageBean messageBean = new MessageBean(message.targetId,message.fromId,lastMsg,lastTime);


        new Thread(new Runnable() {
            @Override
            public void run() {
                MLOC.addHistory(historyBean,true);
                MLOC.saveMessage(messageBean);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewModel.getMMessageList().add(messageBean);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();


    }

    @Override
    public void dispatchEvent(String aEventID, boolean success, Object eventObj) {
        MLOC.d("IM_C2C",aEventID+"||"+eventObj);
        switch (aEventID){
            case AEvent.AEVENT_C2C_REV_MSG:
            case AEvent.AEVENT_REV_SYSTEM_MSG:
                final XHIMMessage revMsg = (XHIMMessage) eventObj;
                if(revMsg.fromId.equals(mTargetId)){
                    String type = AppDatabase.Companion.getDatabase().getHISTORY_TYPE_C2C();
                    String lastTime = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new java.util.Date());
                    String lastMsg = revMsg.contentData;
                    String targetId = revMsg.targetId == null ? MLOC.userId : revMsg.targetId;

                    HistoryBean historyBean = new HistoryBean(type,revMsg.fromId,targetId,1,lastMsg,lastTime,"","");
                    MessageBean messageBean = new MessageBean(targetId,revMsg.fromId,lastMsg,lastTime);


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MLOC.addHistory(historyBean,true);
                            MLOC.saveMessage(messageBean);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    viewModel.getMMessageList().add(messageBean);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();


                }
                break;
        }
    }
}