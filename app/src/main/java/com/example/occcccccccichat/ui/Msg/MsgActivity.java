package com.example.occcccccccichat.ui.Msg;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.occcccccccichat.BaseActivity;
import com.example.occcccccccichat.Tool.MLOC;
import com.example.occcccccccichat.Tool.MyApplication;
import com.example.occcccccccichat.data.database.AppDatabase;
import com.example.occcccccccichat.data.model.HistoryBean;
import com.example.occcccccccichat.databinding.ActivityMsgBinding;
import com.example.occcccccccichat.ui.chat.ChatActivity;

import java.util.List;

public class MsgActivity extends BaseActivity {
    private MsgViewModel viewModel;
    private MsgRVAdapter adapter;
    private ActivityMsgBinding binding;
    private String mTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MsgViewModel.class);
        binding = ActivityMsgBinding.inflate(getLayoutInflater());

        adapter = new MsgRVAdapter(viewModel.getMHistoryList());
        adapter.setOnClickListener((int position) -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MLOC.addHistory(viewModel.getMHistoryList().get(position),true );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTarget = viewModel.getMHistoryList().get(position).getFromId().equals(MLOC.userId) ?
                                    viewModel.getMHistoryList().get(position).getTargetId() :
                                    viewModel.getMHistoryList().get(position).getFromId();
                            Intent intent = new Intent(MsgActivity.this, ChatActivity.class);
                            intent.putExtra("targetId",mTarget);
                            startActivity(intent);
                        }
                    });
                }
            }).start();

        });

        initRecyclerView();
        setContentView(binding.getRoot());
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MyApplication.context,DividerItemDecoration.VERTICAL);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        binding.recyclerviewMsg.addItemDecoration(dividerItemDecoration);
        binding.recyclerviewMsg.setHasFixedSize(true);
        binding.recyclerviewMsg.setAdapter(adapter);
        binding.recyclerviewMsg.setLayoutManager(layoutManager);
    }

    private void updateRVList(){
        MLOC.hasNewC2CMsg = false;
        viewModel.getMHistoryList().clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HistoryBean> list = MLOC.getHistoryListInOwner(AppDatabase.Companion.getDatabase().getHISTORY_TYPE_C2C(),MLOC.userId);
                if(list != null && list.size()>0){
                    viewModel.getMHistoryList().addAll(list);
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
    protected void onResume() {
        super.onResume();
        updateRVList();
//        List<HistoryBean> list = MLOC.getHistoryList(AppDatabase.Companion.getDatabase().getHISTORY_TYPE_C2C());
//        if(list != null && list.size()>0){
//            viewModel.getMHistoryList().addAll(list);
//        }
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void dispatchEvent(String aEventID, boolean success, Object eventObj) {
        super.dispatchEvent(aEventID, success, eventObj);
        onResume();
    }
}