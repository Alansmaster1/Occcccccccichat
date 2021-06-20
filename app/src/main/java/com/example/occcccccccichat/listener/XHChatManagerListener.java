package com.example.occcccccccichat.listener;

import com.example.occcccccccichat.Tool.AEvent;
import com.example.occcccccccichat.Tool.MLOC;
import com.example.occcccccccichat.data.database.AppDatabase;
import com.example.occcccccccichat.data.model.HistoryBean;
import com.example.occcccccccichat.data.model.MessageBean;
import com.starrtc.starrtcsdk.apiInterface.IXHChatManagerListener;
import com.starrtc.starrtcsdk.core.im.message.XHIMMessage;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class XHChatManagerListener implements IXHChatManagerListener {
    @Override
    public void onReceivedMessage(XHIMMessage message) {
        String type = AppDatabase.Companion.getDatabase().getHISTORY_TYPE_C2C();
        String lastTime = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new java.util.Date());
        String lastMsg = message.contentData;
        String conversationId = message.fromId;
        int newMsgCount = 1;

        HistoryBean historyBean = new HistoryBean(type,conversationId,newMsgCount,lastMsg,lastTime,"","");
        MessageBean messageBean = new MessageBean(conversationId,conversationId,lastMsg,lastTime);

        new Thread(new Runnable() {
            @Override
            public void run() {
                MLOC.addHistory(historyBean,false);
                MLOC.saveMessage(messageBean);
                AEvent.notifyListener(AEvent.AEVENT_C2C_REV_MSG,true,message);
            }
        }).start();

    }

    @Override
    public void onReceivedSystemMessage(XHIMMessage message) {
        String type = AppDatabase.Companion.getDatabase().getHISTORY_TYPE_C2C();
        String lastTime = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new java.util.Date());
        String lastMsg = message.contentData;
        String conversationId = message.fromId;
        int newMsgCount = 1;
        HistoryBean historyBean = new HistoryBean(type,conversationId,newMsgCount,lastMsg,lastTime,"","");
        MessageBean messageBean = new MessageBean(conversationId,conversationId,lastMsg,lastTime);

        new Thread(new Runnable() {
            @Override
            public void run() {
                MLOC.addHistory(historyBean,false);
                MLOC.saveMessage(messageBean);
                AEvent.notifyListener(AEvent.AEVENT_REV_SYSTEM_MSG,true,message);
            }
        }).start();
    }
}
