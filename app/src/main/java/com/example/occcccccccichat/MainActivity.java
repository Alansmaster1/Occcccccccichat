package com.example.occcccccccichat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.occcccccccichat.Tool.LogUtil;
import com.example.occcccccccichat.Tool.MLOC;
import com.example.occcccccccichat.ui.Msg.MsgActivity;
import com.example.occcccccccichat.ui.contact.ContactActivity;
import com.starrtc.starrtcsdk.api.XHClient;
import com.starrtc.starrtcsdk.apiInterface.IXHResultCallback;

public class MainActivity extends BaseActivity {
    private boolean isOnline = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        NavController navController = ActivityKt.findNavController(this,R.id.nav_host_fragment);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_msg,R.id.navigation_contact,R.id.navigation_notifications
//        ).build();
//        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
//        NavigationUI.setupWithNavController(navView,navController);

        ((TextView)findViewById(R.id.userinfo_id)).setText(MLOC.userId);
        findViewById(R.id.btn_chatList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MsgActivity.class));
            }
        });

        findViewById(R.id.btn_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContactActivity.class));
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
            startActivity(new Intent(MainActivity.this,LauchActivity.class));
            finish();
        }
        if(findViewById(R.id.btn_chatList)!=null){
            findViewById(R.id.btn_chatList).setBackgroundColor(MLOC.hasNewC2CMsg||MLOC.hasNewGroupMsg? Color.RED:Color.BLUE);
        }
        isOnline = XHClient.getInstance().getIsOnline();
        if(findViewById(R.id.loading)!=null){
            findViewById(R.id.loading).setVisibility(XHClient.getInstance().getIsOnline()?View.INVISIBLE:View.VISIBLE);
        }

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