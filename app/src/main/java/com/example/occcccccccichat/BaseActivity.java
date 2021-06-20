package com.example.occcccccccichat;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.occcccccccichat.Tool.AEvent;
import com.example.occcccccccichat.Tool.IEventListener;
import com.example.occcccccccichat.Tool.MLOC;
import com.starrtc.starrtcsdk.api.XHClient;
import com.starrtc.starrtcsdk.core.im.message.XHIMMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseActivity extends AppCompatActivity implements IEventListener {
    @Override
    public void dispatchEvent(String aEventID, boolean success, Object eventObj) {
        switch (aEventID){
            case AEvent.AEVENT_C2C_REV_MSG:
            case AEvent.AEVENT_REV_SYSTEM_MSG:
                MLOC.hasNewC2CMsg = true;
                if(findViewById(R.id.btn_chatList)!=null){
                    findViewById(R.id.btn_chatList).setBackgroundColor(MLOC.hasNewC2CMsg? Color.RED:Color.BLUE);
                }
//                if(findViewById(R.id.im_new)!=null) {
//                    findViewById(R.id.im_new).setVisibility((MLOC.hasNewC2CMsg || MLOC.hasNewGroupMsg) ? View.VISIBLE : View.INVISIBLE);
//                }
                try {
                    XHIMMessage revMsg = (XHIMMessage) eventObj;
                    JSONObject alertData = new JSONObject();
                    alertData.put("listType",0);
                    alertData.put("farId",revMsg.fromId);
                    alertData.put("msg","收到一条新信息");
                    MLOC.showDialog(BaseActivity.this,alertData);
                } catch (JSONException e){
                    e.printStackTrace();
                }
                break;
//            case AEvent.AEVENT_GROUP_REV_MSG:
//                MLOC.hasNewGroupMsg = true;
//                if(findViewById(R.id.group_new)!=null){
//                    findViewById(R.id.group_new).setVisibility(MLOC.hasNewGroupMsg?View.VISIBLE:View.INVISIBLE);
//                }
//                if(findViewById(R.id.im_new)!=null) {
//                    findViewById(R.id.im_new).setVisibility((MLOC.hasNewC2CMsg || MLOC.hasNewGroupMsg) ? View.VISIBLE : View.INVISIBLE);
//                }
//                try{
//                    XHIMMessage revMsg = (XHIMMessage) eventObj;
//                    JSONObject alertData = new JSONObject();
//                    alertData.put("listType",1);
//                    alertData.put("farId",revMsg.targetId);
//                    alertData.put("msg","收到一条群消息");
//                    MLOC.showDialog(BaseActivity.this,alertData);
//                } catch (JSONException e){
//                    e.printStackTrace();
//                }
//                break;
            case AEvent.AEVENT_USER_OFFLINE:
                MLOC.showMsg(BaseActivity.this,"服务已断开");
                ((TextView)findViewById(R.id.loading)).setText("连接中...");
                if(findViewById(R.id.loading)!=null) {
                    if (XHClient.getInstance().getIsOnline()) {
                        findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                    } else {
                        findViewById(R.id.loading).setVisibility(View.VISIBLE);
                    }
                }
                break;
            case AEvent.AEVENT_USER_ONLINE:
                if(findViewById(R.id.loading)!=null) {
                    if (XHClient.getInstance().getIsOnline()) {
                        findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                    } else {
                        findViewById(R.id.loading).setVisibility(View.VISIBLE);
                    }
                    ((ImageView)findViewById(R.id.userinfo_head)).setImageResource(MLOC.getHeadImage(this,MLOC.userId));
                    ((TextView)findViewById(R.id.userinfo_id)).setText(MLOC.userId);
                }
                break;
            case AEvent.AEVENT_CONN_DEATH:
                MLOC.showMsg(BaseActivity.this,"服务已断开");
                if(findViewById(R.id.loading)!=null) {
                    ((TextView)findViewById(R.id.loading)).setText("连接异常，请重新登录");
                    if (XHClient.getInstance().getIsOnline()) {
                        findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                    } else {
                        findViewById(R.id.loading).setVisibility(View.VISIBLE);
                    }
                    ((ImageView)findViewById(R.id.userinfo_head)).setImageResource(MLOC.getHeadImage(this,MLOC.userId));
                    ((TextView)findViewById(R.id.userinfo_id)).setText(MLOC.userId);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(findViewById(R.id.group_new)!=null){
//            findViewById(R.id.group_new).setVisibility(MLOC.hasNewGroupMsg?View.VISIBLE:View.INVISIBLE);
//        }
//        if(findViewById(R.id.im_new)!=null) {
//            findViewById(R.id.im_new).setVisibility((MLOC.hasNewC2CMsg || MLOC.hasNewGroupMsg) ? View.VISIBLE : View.INVISIBLE);
//        }
        addListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeListener();
    }

    private void addListener(){
        AEvent.addListener(AEvent.AEVENT_C2C_REV_MSG,this);
        AEvent.addListener(AEvent.AEVENT_REV_SYSTEM_MSG,this);
        AEvent.addListener(AEvent.AEVENT_GROUP_REV_MSG,this);
        AEvent.addListener(AEvent.AEVENT_USER_ONLINE,this);
        AEvent.addListener(AEvent.AEVENT_USER_OFFLINE,this);
        AEvent.addListener(AEvent.AEVENT_CONN_DEATH,this);
    }

    private void removeListener(){
        AEvent.removeListener(AEvent.AEVENT_C2C_REV_MSG,this);
        AEvent.removeListener(AEvent.AEVENT_REV_SYSTEM_MSG,this);
        AEvent.removeListener(AEvent.AEVENT_GROUP_REV_MSG,this);
        AEvent.removeListener(AEvent.AEVENT_USER_ONLINE,this);
        AEvent.removeListener(AEvent.AEVENT_USER_OFFLINE,this);
        AEvent.removeListener(AEvent.AEVENT_CONN_DEATH,this);
    }
}
