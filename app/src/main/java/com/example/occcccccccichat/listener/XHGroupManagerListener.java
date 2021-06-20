package com.example.occcccccccichat.listener;

import com.example.occcccccccichat.Tool.AEvent;
import com.example.occcccccccichat.Tool.MLOC;
import com.example.occcccccccichat.data.database.AppDatabase;
import com.example.occcccccccichat.data.model.HistoryBean;
import com.example.occcccccccichat.data.model.MessageBean;
import com.starrtc.starrtcsdk.apiInterface.IXHGroupManagerListener;
import com.starrtc.starrtcsdk.core.im.message.XHIMMessage;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class XHGroupManagerListener implements IXHGroupManagerListener {
    @Override
    public void onMembersUpdeted(String s, int i) {

    }

    @Override
    public void onSelfKicked(String s) {

    }

    @Override
    public void onGroupDeleted(String s) {

    }

    @Override
    public void onReceivedMessage(XHIMMessage message) {
        //msg.fromId 消息来源
        //msg.targetId 目标ID
        //msg.contentData 消息体
        //msg.time 消息发送的时间
        //msg.atList 群内@某个用户 多人用逗号“,”分隔

        String type = AppDatabase.Companion.getDatabase().getHISTORY_TYPE_GROUP();
        String lastTime = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new java.util.Date());
        String lastMsg = message.contentData;
        String conversationId = message.targetId;
        int newMsgCount = 1;
        HistoryBean historyBean = new HistoryBean(type,conversationId,newMsgCount,lastMsg,lastTime,"","");
        MLOC.addHistory(historyBean,false);

        MessageBean messageBean = new MessageBean(conversationId,message.fromId,lastMsg,lastTime);
        MLOC.saveMessage(messageBean);

        AEvent.notifyListener(AEvent.AEVENT_GROUP_REV_MSG,true,message);
    }
}
