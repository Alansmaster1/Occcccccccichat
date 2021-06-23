package com.example.occcccccccichat.listener;

import com.example.occcccccccichat.Tool.AEvent;
import com.starrtc.starrtcsdk.api.XHConstants;
import com.starrtc.starrtcsdk.apiInterface.IXHLoginManagerListener;

/**
 * 该类中的代码作者为starRTC作者, 本人只作修改和注释
 */
public class XHLoginManagerListener implements IXHLoginManagerListener {
    @Override
    public void onConnectionStateChanged(XHConstants.XHSDKConnectionState state) {
        if(state == XHConstants.XHSDKConnectionState.SDKConnectionStateDisconnect){
            AEvent.notifyListener(AEvent.AEVENT_USER_OFFLINE,true,"");
        } else if(state == XHConstants.XHSDKConnectionState.SDKConnectionStateReconnect){
            AEvent.notifyListener(AEvent.AEVENT_USER_ONLINE,true,"");
        }else if(state == XHConstants.XHSDKConnectionState.SDKConnectionDeath){
            AEvent.notifyListener(AEvent.AEVENT_CONN_DEATH,true,"");
        }
    }

    @Override
    public void onKickedByOtherDeviceLogin() {
        AEvent.notifyListener(AEvent.AEVENT_USER_KICKED,true,"");
    }

    @Override
    public void onLogout() {

    }
}
