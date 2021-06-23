package com.example.occcccccccichat.Tool;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * 该类中的代码作者为starRTC作者, 本人只作修改和注释
 */
public class AEvent {
    private static List<EventObj> callBackList = new ArrayList<EventObj>();
    private static Handler mHandler;

    public static void setHandler(Handler handler){
        mHandler = handler;
    }

    public static void addListener(String eventID, IEventListener eventListener){
        for(EventObj eventObj:callBackList){
            if(eventObj.eventID.equals(eventID)&&eventObj.eventListener.getClass()==eventListener.getClass()) {
                return;
            }
        }
        EventObj event = new EventObj();
        event.eventListener = eventListener;
        event.eventID = eventID;
        callBackList.add(event);
    }

    public static void removeListener(String eventID, IEventListener eventListener){
        int i;
        EventObj event;
        for(i=0;i<callBackList.size();i++){
            event = callBackList.get(i);
            if(event.eventID.equals(eventID) && event.eventListener == eventListener){
                callBackList.remove(i);
                return;
            }
        }
    }

    public static void notifyListener(final String eventID, final boolean success, final Object object){

        if (Looper.myLooper() == Looper.getMainLooper()) {
            // UI线程
            int i;
            EventObj event;
            for(i=0;i<callBackList.size();i++){
                event = callBackList.get(i);
                if(event.eventID.equals(eventID)){
                    event.eventListener.dispatchEvent(eventID,success,object);
                }
            }
        } else {
            // 非UI线程
            if(mHandler!=null){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        int i;
                        EventObj event;
                        for(i=0;i<callBackList.size();i++){
                            event = callBackList.get(i);
                            if(event.eventID.equals(eventID)){
                                event.eventListener.dispatchEvent(eventID,success,object);
                            }
                        }
                    }
                });
            }else{
                int i;
                EventObj event;
                for(i=0;i<callBackList.size();i++){
                    event = callBackList.get(i);
                    if(event.eventID.equals(eventID)){
                        event.eventListener.dispatchEvent(eventID,success,object);
                    }
                }
            }
        }

    }

    private static class EventObj {
        IEventListener eventListener;
        String eventID;
    }

    //事件类型
    public static final String AEVENT_LOGOUT                    = "AEVENT_LOGOUT";
    public static final String AEVENT_RESET                     = "AEVENT_RESET";

    public static final String AEVENT_VOIP_REV_MISS             = "AEVENT_VOIP_REV_MISS";

    public static final String AEVENT_C2C_REV_MSG               ="AEVENT_C2C_REV_MSG";
    public static final String AEVENT_GROUP_REV_MSG             ="AEVENT_GROUP_REV_MSG";
    public static final String AEVENT_REV_SYSTEM_MSG             ="AEVENT_REV_SYSTEM_MSG";

    public static final String AEVENT_CONN_DEATH               ="AEVENT_CONN_DEATH";
    public static final String AEVENT_USER_KICKED               ="AEVENT_USER_KICKED";
    public static final String AEVENT_USER_ONLINE               ="AEVENT_USER_ONLINE";
    public static final String AEVENT_USER_OFFLINE              ="AEVENT_USER_OFFLINE";
}
