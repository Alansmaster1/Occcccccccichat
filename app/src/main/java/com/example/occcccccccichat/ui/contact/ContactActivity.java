package com.example.occcccccccichat.ui.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.occcccccccichat.R;
import com.example.occcccccccichat.Tool.LogUtil;
import com.example.occcccccccichat.Tool.MyApplication;
import com.example.occcccccccichat.data.model.ContactItem;
import com.example.occcccccccichat.databinding.ActivityChatBinding;
import com.example.occcccccccichat.databinding.ActivityContactBinding;
import com.example.occcccccccichat.ui.chat.ChatActivity;

import java.util.List;
import java.util.Objects;

public class ContactActivity extends AppCompatActivity {
    private ContactViewModel viewModel;
    private ContactRVAdapter adapter;
    private ActivityContactBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        binding = ActivityContactBinding.inflate(getLayoutInflater());

        viewModel.getRepository().getContactItemList().observe(this,(List<ContactItem> it)->{
            adapter.notifyDataSetChanged();
        });

        binding.btnAddItemContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactActivity.this,ContactEditActivity.class));
            }
        });

        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAdapter();
        initRecyclerView();
    }

    private void initAdapter(){
        adapter = new ContactRVAdapter(viewModel.getRepository().getContactItemList());
        adapter.setOnClickListener((int position)->{
            Intent intent = new Intent(ContactActivity.this, ChatActivity.class);
            intent.putExtra("targetId", Objects.requireNonNull(viewModel.getRepository().getContactItemList().getValue()).get(position).getId());
            startActivity(intent);
        });
        adapter.setOnLongClickListener((int position)->{
            Intent intent = new Intent(ContactActivity.this,ContactEditActivity.class);
            intent.putExtra("targetId", Objects.requireNonNull(viewModel.getRepository().getContactItemList().getValue()).get(position).getId());
            LogUtil.INSTANCE.d("Debug"," " + Objects.requireNonNull(viewModel.getRepository().getContactItemList().getValue()).get(position).getId());
            startActivity(intent);
        });
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MyApplication.context,DividerItemDecoration.VERTICAL);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        binding.recyclerviewContact.addItemDecoration(dividerItemDecoration);
        binding.recyclerviewContact.setHasFixedSize(true);
        binding.recyclerviewContact.setAdapter(adapter);
        binding.recyclerviewContact.setLayoutManager(layoutManager);
    }
}