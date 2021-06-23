package com.example.occcccccccichat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.example.occcccccccichat.Tool.AEvent;
import com.example.occcccccccichat.Tool.IEventListener;
import com.example.occcccccccichat.Tool.LogUtil;
import com.example.occcccccccichat.Tool.MLOC;
import com.example.occcccccccichat.listener.XHChatManagerListener;
import com.example.occcccccccichat.listener.XHLoginManagerListener;
import com.starrtc.starrtcsdk.api.XHClient;
import com.starrtc.starrtcsdk.api.XHCustomConfig;
import com.starrtc.starrtcsdk.apiInterface.IXHResultCallback;

/**
 * 该类中的代码作者为starRTC作者, 本人只作修改和注释
 */
public class KeepLiveService extends Service implements IEventListener {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initSDK();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initSDK(){
        //修改后MLOC.init在Launch中被调用
        //MLOC.init(this);
        initFree();
    }

    private boolean isLogin = false;
    private void initFree(){

        //处理登陆业务逻辑的代码,原版本中是随机一个ID登陆,修改后的登录和注册逻辑分别移交给LoginActivity/SignupActivity
        isLogin = XHClient.getInstance().getIsOnline();
        LogUtil.INSTANCE.d("Debug",XHClient.getInstance().getIsOnline() + " KeepLiveServer");
        //防止多次启动service
        if(!isLogin){
            //当userState == logout 进入 KeepLiveService时,说明是从登录进来的
            if(MLOC.userState.equals("logout")){
                MLOC.userState = "login";
                MLOC.saveUserId(MLOC.userId);
                MLOC.saveUserState(MLOC.userState);
            }

            addListener();

            XHCustomConfig customConfig = XHCustomConfig.getInstance(this);
            customConfig.setImServerUrl(MLOC.IM_SERVER_URL);
            customConfig.initSDKForFree(MLOC.userId, (errMsg, data) -> {
                MLOC.e("KeepLiveService","error:"+errMsg);
                MLOC.showMsg(KeepLiveService.this,errMsg);
            },new Handler());

            XHClient.getInstance().getChatManager().addListener(new XHChatManagerListener());
            XHClient.getInstance().getLoginManager().addListener(new XHLoginManagerListener());

            XHClient.getInstance().getLoginManager().loginFree(new IXHResultCallback() {
                @Override
                public void success(Object data) {
                    MLOC.d("KeepLiveService","loginSuccess");
                    isLogin = true;
                }

                @Override
                public void failed(String errMsg) {
                    MLOC.d("KeepLiveService","loginFailed "+errMsg);
                    MLOC.showMsg(KeepLiveService.this,errMsg);
                }
            });
        }
    }

    private void addListener(){
        AEvent.addListener(AEvent.AEVENT_LOGOUT,this);
        AEvent.addListener(AEvent.AEVENT_C2C_REV_MSG,this);
        AEvent.addListener(AEvent.AEVENT_REV_SYSTEM_MSG,this);
        AEvent.addListener(AEvent.AEVENT_USER_KICKED,this);
        AEvent.addListener(AEvent.AEVENT_CONN_DEATH,this);
    }

    private void removeListener(){
        AEvent.removeListener(AEvent.AEVENT_LOGOUT,this);
        AEvent.removeListener(AEvent.AEVENT_C2C_REV_MSG,this);
        AEvent.removeListener(AEvent.AEVENT_REV_SYSTEM_MSG,this);
        AEvent.removeListener(AEvent.AEVENT_USER_KICKED,this);
        AEvent.removeListener(AEvent.AEVENT_CONN_DEATH,this);
    }

    @Override
    public void dispatchEvent(String aEventID, boolean success, Object eventObj) {
        switch (aEventID){
            case AEvent.AEVENT_C2C_REV_MSG:
            case AEvent.AEVENT_REV_SYSTEM_MSG:
                MLOC.hasNewC2CMsg = true;
                break;
            case AEvent.AEVENT_GROUP_REV_MSG:
                MLOC.hasNewGroupMsg = true;
                break;
            case AEvent.AEVENT_LOGOUT:
                removeListener();
                this.stopSelf();
            case AEvent.AEVENT_USER_KICKED:
            case AEvent.AEVENT_CONN_DEATH:
                MLOC.d("KeepLiveService","AEVENT_USER_KICKED OR AEVENT_CONN_DEATH");
                XHClient.getInstance().getLoginManager().loginFree(new IXHResultCallback() {
                    @Override
                    public void success(Object data) {
                        MLOC.d("KeepLiveService","loginSuccess");
                        isLogin = true;
                    }

                    @Override
                    public void failed(String errMsg) {
                        MLOC.d("KeepLiveService","loginFailed "+errMsg);
                        MLOC.showMsg(KeepLiveService.this,errMsg);
                    }
                });
                break;
        }
    }
}