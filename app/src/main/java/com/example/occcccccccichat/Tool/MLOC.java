package com.example.occcccccccichat.Tool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.occcccccccichat.R;
import com.example.occcccccccichat.data.dao.HistoryDao;
import com.example.occcccccccichat.data.dao.MessageDao;
import com.example.occcccccccichat.data.database.AppDatabase;
import com.example.occcccccccichat.data.model.HistoryBean;
import com.example.occcccccccichat.data.model.MessageBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MLOC {
    public static Context appContext;
    public static String userId = "";
    public static String userState = "logout";

    //服务器IP或者域名
    //public static String SERVER_HOST                = "demo.starrtc.com";
    public static String SERVER_HOST                = "42.192.202.168";
    //public static String VOIP_SERVER_URL            = SERVER_HOST+":10086";
    //IM功能的服务器socket
    public static String IM_SERVER_URL              = SERVER_HOST+":19903";
    //public static String CHATROOM_SERVER_URL        = SERVER_HOST+":19906";
    //public static String LIVE_VDN_SERVER_URL        = SERVER_HOST+":19928";
    //public static String LIVE_SRC_SERVER_URL        = SERVER_HOST+":19931";
    //public static String LIVE_PROXY_SERVER_URL      = SERVER_HOST+":19932";

    //不知道干啥的
    public static Boolean AEventCenterEnable = false;

    //不知道干啥的,应该是从服务器数据库获取内容的
    public static String IM_GROUP_LIST_URL  = "http://www.starrtc.com/aec/group/list.php";
    public static String IM_GROUP_INFO_URL  = "http://www.starrtc.com/aec/group/members.php";
    public static String LIST_SAVE_URL      = "http://www.starrtc.com/aec/list/save.php";
    public static String LIST_DELETE_URL    = "http://www.starrtc.com/aec/list/del.php";
    public static String LIST_QUERY_URL     = "http://www.starrtc.com/aec/list/query.php";

    public static final int LIST_TYPE_CHATROOM = 0;             //IM 聊天室
//    public static final int LIST_TYPE_LIVE = 1;                 //直播
//    public static final int LIST_TYPE_LIVE_PUSH = 2;            //直播转推第三方流
//    public static final int LIST_TYPE_MEETING = 3;              //会议
//    public static final int LIST_TYPE_MEETING_PUSH = 4;         //会议转推第三方流
//    public static final int LIST_TYPE_CLASS = 5;                //小班课
//    public static final int LIST_TYPE_CLASS_PUSH = 6;           //小班课转推第三方流
//    public static final int LIST_TYPE_AUDIO_LIVE = 7;           //音频直播
//    public static final int LIST_TYPE_AUDIO_LIVE_PUSH = 8;      //音频直播转推第三方流
//    public static final int LIST_TYPE_SUPER_ROOM = 9;           //超级对讲
//    public static final int LIST_TYPE_SUPER_ROOM_PUSH = 10;     //超级对讲转推第三方流

//    public static final String LIST_TYPE_LIVE_ALL = LIST_TYPE_LIVE +","+ LIST_TYPE_LIVE_PUSH;
//    public static final String LIST_TYPE_MEETING_ALL = LIST_TYPE_MEETING +","+ LIST_TYPE_MEETING_PUSH;
//    public static final String LIST_TYPE_CLASS_ALL = LIST_TYPE_CLASS +","+ LIST_TYPE_CLASS_PUSH;
//    public static final String LIST_TYPE_AUDIO_LIVE_ALL = LIST_TYPE_AUDIO_LIVE +","+ LIST_TYPE_AUDIO_LIVE_PUSH;
//    public static final String LIST_TYPE_SUPER_ROOM_ALL = LIST_TYPE_SUPER_ROOM +","+ LIST_TYPE_SUPER_ROOM_PUSH;
//    public static final String LIST_TYPE_PUSH_ALL = LIST_TYPE_LIVE_PUSH
//            +","+ LIST_TYPE_MEETING_PUSH
//            +","+ LIST_TYPE_CLASS_PUSH
//            +","+ LIST_TYPE_AUDIO_LIVE_PUSH
//            +","+ LIST_TYPE_SUPER_ROOM_PUSH;

    //新消息标记
    public static Boolean hasLogout = false;

    public static boolean hasNewC2CMsg = false;
    public static boolean hasNewGroupMsg = false;
    public static boolean hasNewVoipMsg = false;
    public static boolean canPickupVoip = true;

    public static boolean deleteGroup = false;

    //数据库
    private static HistoryDao historyDao = null;
    private static MessageDao messageDao = null;

    public static void init(Context context){
        //appContext = context.getApplicationContext();
        appContext = MyApplication.context;
        if(historyDao==null){
            historyDao =  AppDatabase.Companion.getDatabase().historyDao();
        }
        if(messageDao==null){
            messageDao = AppDatabase.Companion.getDatabase().messageDao();
        }
        userId = loadSharedData(context,"userId",userId);
        userState = loadSharedData(context,"userState",userState);

//        VOIP_SERVER_URL         = loadSharedData(context,"VOIP_SERVER_URL",VOIP_SERVER_URL);
        IM_SERVER_URL           = loadSharedData(context,"IM_SERVER_URL",IM_SERVER_URL);
//        LIVE_SRC_SERVER_URL     = loadSharedData(context,"LIVE_SRC_SERVER_URL",LIVE_SRC_SERVER_URL);
//        LIVE_PROXY_SERVER_URL   =  loadSharedData(context,"LIVE_PROXY_SERVER_URL", LIVE_PROXY_SERVER_URL);
//        LIVE_VDN_SERVER_URL     = loadSharedData(context,"LIVE_VDN_SERVER_URL",LIVE_VDN_SERVER_URL);
//        CHATROOM_SERVER_URL     = loadSharedData(context,"CHATROOM_SERVER_URL", CHATROOM_SERVER_URL);


        AEventCenterEnable = !loadSharedData(context, "AEC_ENABLE", "0").equals("0");

        IM_GROUP_LIST_URL   = loadSharedData(context,"IM_GROUP_LIST_URL",IM_GROUP_LIST_URL);
        IM_GROUP_INFO_URL   = loadSharedData(context,"IM_GROUP_INFO_URL",IM_GROUP_INFO_URL);
        LIST_SAVE_URL       = loadSharedData(context,"LIST_SAVE_URL",LIST_SAVE_URL);
        LIST_DELETE_URL     =  loadSharedData(context,"LIST_DELETE_URL", LIST_DELETE_URL);
        LIST_QUERY_URL      = loadSharedData(context,"LIST_QUERY_URL",LIST_QUERY_URL);
    }


    //调试用的方法
    private static Boolean debug = true;
    public static void setDebug(Boolean b){
        debug = b;
    }

    public static void d(String tag,String msg){
        if(debug){
            Log.d("starSDK_demo_"+tag,msg);
        }
    }

    public static void e(String tag,String msg){
        Log.e("starSDK_demo_"+tag,msg);
    }

    private static Toast mToast;
    public static void showMsg(String str){
        try {
            if (mToast != null) {
                mToast.setText(str);
                mToast.setDuration(Toast.LENGTH_SHORT);
            } else {
                mToast = Toast.makeText(appContext.getApplicationContext(), str, Toast.LENGTH_SHORT);
            }
            mToast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void showMsg(Context context ,String str){
        try {
            if (mToast != null) {
                mToast.setText(str);
                mToast.setDuration(Toast.LENGTH_SHORT);
            } else {
                mToast = Toast.makeText(context.getApplicationContext(), str, Toast.LENGTH_SHORT);
            }
            mToast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //数据库访问
    //将coreDB改为对应的Dao, 判断!=null不知道有什么bug
    public static List<HistoryBean> getHistoryList(String type){
        if(historyDao!=null){
            return historyDao.getHistory(type);
        }else{
            return null;
        }
    }

    public static void addHistory(HistoryBean history, Boolean hasRead){
        if(historyDao!=null){
            historyDao.addHistory(history,hasRead);
        }
    }

    public static void updateHistory(HistoryBean history){
        if(historyDao!=null){
            historyDao.updateHistory(history);
        }
    }

    public static void removeHistory(HistoryBean history){
        if(historyDao!=null){
            historyDao.removeHistory(history);
        }
    }

    public static List<MessageBean> getMessageList(String conversationId){
        if(messageDao!=null){
            return messageDao.getMessageList(conversationId);
        }else{
            return null;
        }
    }

    public static void saveMessage(MessageBean messageBean){
        if(messageDao!=null){
            messageDao.setMessage(messageBean);
        }
    }


    //shareData不知道干啥的_可能是一些公用的数据,不是用户私有的
    public static void saveSharedData(Context context,String key,String value){
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("stardemo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String loadSharedData(Context context,String key){
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("stardemo", Activity.MODE_PRIVATE);
        return sp.getString(key,"");
    }

    public static String loadSharedData(Context context,String key,String defValue){
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("stardemo", Activity.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }
    public static void saveUserId(String id){
        MLOC.userId = id;
        MLOC.saveSharedData(appContext,"userId",MLOC.userId);
    }
    public static void saveUserState(String state){
        MLOC.userState = state;
        MLOC.saveSharedData(appContext,"userState",MLOC.userState);
    }

//    public static void saveVoipServerUrl(String voipServerUrl){
//        MLOC.VOIP_SERVER_URL = voipServerUrl;
//        saveSharedData(appContext,"VOIP_SERVER_URL",VOIP_SERVER_URL);
//    }

//    public static void saveSrcServerUrl(String srcServerUrl){
//        MLOC.LIVE_SRC_SERVER_URL = srcServerUrl;
//        saveSharedData(appContext,"LIVE_SRC_SERVER_URL",LIVE_SRC_SERVER_URL);
//    }

//    public static void saveVdnServerUrl(String vdnServerUrl){
//        MLOC.LIVE_VDN_SERVER_URL = vdnServerUrl;
//        saveSharedData(appContext,"LIVE_VDN_SERVER_URL",LIVE_VDN_SERVER_URL);
//    }

//    public static void saveProxyServerUrl(String proxyServerUrl){
//        MLOC.LIVE_PROXY_SERVER_URL = proxyServerUrl;
//        saveSharedData(appContext,"LIVE_PROXY_SERVER_URL", LIVE_PROXY_SERVER_URL);
//    }

//    public static void saveChatroomServerUrl(String chatroomServerUrl){
//        MLOC.CHATROOM_SERVER_URL = chatroomServerUrl;
//        saveSharedData(appContext,"CHATROOM_SERVER_URL", CHATROOM_SERVER_URL);
//    }

    public static void saveImServerUrl(String imServerUrl){
        MLOC.IM_SERVER_URL = imServerUrl;
        saveSharedData(appContext,"IM_SERVER_URL",IM_SERVER_URL);
    }

    public static void saveC2CUserId(Context context,String uid){
        String history = MLOC.loadSharedData(context.getApplicationContext(),"c2cHistory");
        if(history.length()>0){
            String[] arr = history.split(",,");
            //一个String到StringBuilder的转变,提高字符串效率
            StringBuilder newHistory = new StringBuilder();
            for(int i = 0;i<arr.length;i++){
                if(i==0){
                    if(arr[i].equals(uid))return;
                    newHistory.append(arr[i]);
                }else{
                    if(arr[i].equals(uid))continue;
                    newHistory.append(",,").append(arr[i]);
                }
            }
            if(newHistory.length()==0){
                newHistory = new StringBuilder(uid);
            }else{
                newHistory.insert(0, uid + ",,");
            }
            MLOC.saveSharedData(context.getApplicationContext(),"c2cHistory", newHistory.toString());
        }else{
            MLOC.saveSharedData(context.getApplicationContext(),"c2cHistory",uid);
        }
    }
    public static void cleanC2CUserId(Context context){
        MLOC.saveSharedData(context.getApplicationContext(),"c2cHistory","");
    }

    public static void saveVoipUserId(Context context,String uid){
        String history = MLOC.loadSharedData(context.getApplicationContext(),"voipHistory");
        if(history.length()>0){
            String[] arr = history.split(",,");
            StringBuilder newHistory = new StringBuilder();
            for(int i = 0;i<arr.length;i++){
                if(i==0){
                    if(arr[i].equals(uid))return;
                    newHistory.append(arr[i]);
                }else{
                    if(arr[i].equals(uid))continue;
                    newHistory.append(",,").append(arr[i]);
                }
            }
            if(newHistory.length()==0){
                newHistory = new StringBuilder(uid);
            }else{
                newHistory.insert(0, uid + ",,");
            }
            MLOC.saveSharedData(context.getApplicationContext(),"voipHistory", newHistory.toString());
        }else{
            MLOC.saveSharedData(context.getApplicationContext(),"voipHistory",uid);
        }
    }
    public static void cleanVoipUserId(Context context){
        MLOC.saveSharedData(context.getApplicationContext(),"voipHistory","");
    }


    //可能是对话框
    static Dialog[] dialogs = new Dialog[1];
    static Timer dialogTimer ;
    static TimerTask timerTask;
    public static void showDialog(final Context context, final JSONObject data){
        try {
            final int type = data.getInt("listType");// 0:c2c,1:group,2:voip
            final String farId = data.getString("farId");// 对方ID
            String msg = data.getString("msg");// 提示消息

            if(dialogs[0]==null|| !dialogs[0].isShowing()){
                dialogs[0] = new Dialog(context, R.style.dialog_notify);
                dialogs[0].setContentView(R.layout.dialog_new_msg);
                Window win = dialogs[0].getWindow();
                win.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                win.setWindowAnimations(R.style.dialog_notify_animation);
                win.setGravity(Gravity.TOP);
                dialogs[0].setCanceledOnTouchOutside(true);
            }
            ((TextView) dialogs[0].findViewById(R.id.msg_info)).setText(msg);
            dialogs[0].findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dialogTimer!=null){
                        dialogTimer.cancel();
                        timerTask.cancel();
                        dialogTimer = null;
                        timerTask = null;
                    }
                    dialogs[0].dismiss();
                    dialogs[0] = null;
//                    if(listType==0){
//                        //C2C
//                        Intent intent = new Intent(context,C2CListActivity.class);
//                        context.startActivity(intent);
//                    }else if(listType==1){
//                        //Group
//                        Intent intent = new Intent(context, MessageGroupListActivity.class);
//                        context.startActivity(intent);
//                    }else if(listType==2){
//                        //VOIP
//                        Intent intent = new Intent(context, VoipListActivity.class);
//                        context.startActivity(intent);
//                    }
                }
            });
            dialogs[0].show();

            if(dialogTimer!=null){
                dialogTimer.cancel();
                timerTask.cancel();
                dialogTimer = null;
                timerTask = null;
            }
            dialogTimer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(dialogs[0]!=null&&dialogs[0].isShowing()){
                        dialogs[0].dismiss();
                        dialogs[0] = null;
                    }
                }
            };
            dialogTimer.schedule(timerTask,5000);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //头像
    private static int[] mHeadIconIds ;
    public static int getHeadImage(Context context,String userID){
        if(mHeadIconIds==null){
            TypedArray ar = context.getApplicationContext().getResources().obtainTypedArray(R.array.head_images);
            int len = ar.length();
            mHeadIconIds = new int[len];
            for (int i = 0; i < len; i++) {
                mHeadIconIds[i] = ar.getResourceId(i, 0);
            }
            ar.recycle();
        }

        if(userID.isEmpty()){
            return mHeadIconIds[70];
        }else{
            int intId = 0;
            char[] chars = userID.toCharArray();
            for (char aChar : chars) {
                intId += (int) aChar;
            }
            return mHeadIconIds[intId%70];
        }
    }

    public static void saveImGroupListUrl(String imGroupListUrl) {
        MLOC.IM_GROUP_LIST_URL = imGroupListUrl;
        saveSharedData(appContext,"IM_GROUP_LIST_URL", IM_GROUP_LIST_URL);
    }

    public static void saveImGroupInfoUrl(String imGroupInfoUrl) {
        MLOC.IM_GROUP_INFO_URL = imGroupInfoUrl;
        saveSharedData(appContext,"IM_GROUP_INFO_URL", IM_GROUP_INFO_URL);
    }

    public static void saveListSaveUrl(String listSaveUrl) {
        MLOC.LIST_SAVE_URL = listSaveUrl;
        saveSharedData(appContext,"LIST_SAVE_URL", LIST_SAVE_URL);
    }

    public static void saveListDeleteUrl(String listDeleteUrl) {
        MLOC.LIST_DELETE_URL = listDeleteUrl;
        saveSharedData(appContext,"LIST_DELETE_URL", LIST_DELETE_URL);
    }

    public static void saveListQueryUrl(String listQueryUrl) {
        MLOC.LIST_QUERY_URL = listQueryUrl;
        saveSharedData(appContext,"LIST_QUERY_URL", LIST_QUERY_URL);
    }
}

