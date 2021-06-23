package com.example.occcccccccichat.ui.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;

import com.example.occcccccccichat.R;
import com.example.occcccccccichat.Tool.MLOC;
import com.example.occcccccccichat.Tool.MyApplication;
import com.example.occcccccccichat.data.dao.ContactItemDao;
import com.example.occcccccccichat.data.database.AppDatabase;
import com.example.occcccccccichat.data.model.ContactItem;
import com.example.occcccccccichat.databinding.ActivityContactAddBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ContactAddActivity extends AppCompatActivity {
    private ActivityContactAddBinding binding;
    private List<ContactItem> mData;
    private ContactSearchRVAdapter adapter;
    private final ContactItemDao contactItemDao = AppDatabase.Companion.getDatabase().contactItemDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mData = new ArrayList<ContactItem>();
        adapter = new ContactSearchRVAdapter(mData);
        //TODO:添加到ContactItem表中
        adapter.setOnClickListener(new ContactSearchRVAdapter.ViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        contactItemDao.insertItem(mData.get(position));
                        String ownId = mData.get(position).getOwnId();
                        String targetId = mData.get(position).getTargetId();
                        mData.remove(position);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request
                                .Builder()
                                .url("http://42.192.202.168/ochichat/insertContact.php?"
                                +"ownId="+ownId
                                +"&targetId="+targetId)
                                .get()
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                response.close();
                            }
                        });
                    }
                }).start();
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO:在服务器user表中搜索并更新本Activity列表
                getData();
            }
        });

        initRV();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initRV(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MyApplication.context,DividerItemDecoration.VERTICAL);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        binding.recyclerviewContactSearch.addItemDecoration(dividerItemDecoration);
        binding.recyclerviewContactSearch.setHasFixedSize(true);
        binding.recyclerviewContactSearch.setAdapter(adapter);
        binding.recyclerviewContactSearch.setLayoutManager(layoutManager);
    }

    private void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request
                        .Builder()
                        .url("http://42.192.202.168/ochichat/searchUser.php?"
                        +"ownId="+MLOC.userId
                        +"&key="+binding.editTextSearch.getText().toString())
                        .get()
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            displayDate(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        response.close();
                    }
                });
            }
        }).start();
    }

    private void displayDate(JSONArray jsonArray) throws JSONException {
        int len = jsonArray.length();
        mData.clear();
        for(int i=0;i<len;++i){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            ContactItem tmp = new ContactItem(jsonObject.getString("name"));
            tmp.setOwnId(MLOC.userId);
            tmp.setTargetId(jsonObject.getString("id"));
            mData.add(tmp);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

    }

}