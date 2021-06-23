package com.example.occcccccccichat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.occcccccccichat.Tool.AEvent;
import com.example.occcccccccichat.Tool.LogUtil;
import com.example.occcccccccichat.Tool.MLOC;
import com.example.occcccccccichat.Tool.MyApplication;
import com.example.occcccccccichat.databinding.ActivityMainBinding;
import com.example.occcccccccichat.ui.Msg.MsgActivity;
import com.example.occcccccccichat.ui.contact.ContactActivity;
import com.example.occcccccccichat.ui.login.LoginActivity;
import com.starrtc.starrtcsdk.api.XHClient;
import com.starrtc.starrtcsdk.apiInterface.IXHResultCallback;

public class MainActivity extends BaseActivity{
    private boolean isOnline = false;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        NavController navController = ActivityKt.findNavController(this,R.id.nav_host_fragment);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_msg,R.id.navigation_contact,R.id.navigation_notifications
//        ).build();
//        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
//        NavigationUI.setupWithNavController(navView,navController);

        ((TextView)binding.userinfoId).setText(MLOC.userId);
        binding.btnChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MsgActivity.class));
            }
        });

        binding.btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContactActivity.class));
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登出
                MLOC.userState = "logout";
                MLOC.saveUserState(MLOC.userState);
                XHClient.getInstance().getLoginManager().logout();
                AEvent.notifyListener(AEvent.AEVENT_LOGOUT,true,null);
                MLOC.hasLogout = true;
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });

        XHClient.getInstance().getAliveUserNum(new IXHResultCallback() {
            @Override
            public void success(Object data) {
                MLOC.d("!!!!!!!!!!!!!",data.toString());
            }
            @Override
            public void failed(String errMsg) {
                MLOC.d("!!!!!!!!!!!!!",errMsg.toString());
            }
        });

        XHClient.getInstance().getAliveUserList(1, new IXHResultCallback() {
            @Override
            public void success(Object data) {
                MLOC.d("!!!!!!!!!!!!!",data.toString());
            }
            @Override
            public void failed(String errMsg) {
                MLOC.d("!!!!!!!!!!!!!",errMsg.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(MLOC.hasLogout){
            MLOC.hasLogout=false;
            return;
        }
        if(MLOC.userId == null){
            startActivity(new Intent(MainActivity.this, LaunchActivity.class));
            finish();
        }
        binding.btnChatList.setBackgroundColor(MLOC.hasNewC2CMsg||MLOC.hasNewGroupMsg? Color.RED:Color.BLUE);
        isOnline = XHClient.getInstance().getIsOnline();
        binding.loading.setVisibility(XHClient.getInstance().getIsOnline()?View.INVISIBLE:View.VISIBLE);

        //findViewById(R.id.c2c_new).setVisibility(MLOC.hasNewC2CMsg? View.VISIBLE:View.INVISIBLE);
        //findViewById(R.id.group_new).setVisibility(MLOC.hasNewGroupMsg? View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void dispatchEvent(String aEventID, boolean success, Object eventObj) {
        super.dispatchEvent(aEventID, success, eventObj);
        onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.INSTANCE.d("Debug",XHClient.getInstance().getIsOnline() + "");
    }
}