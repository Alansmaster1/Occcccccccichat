package com.example.occcccccccichat.Tool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.occcccccccichat.ui.Msg.MsgActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 该类中的代码作者为starRTC作者, 本人只作修改和注释
 */
public class MLOC {
    public static Context appContext;
    public static String userId = "";
    public static String userState = "logout";

    //服务器IP或者域名
    public static String SERVER_HOST                = "42.192.202.168";
    //IM功能的服务器socket
    public static String IM_SERVER_URL              = SERVER_HOST+":19903";

    public static Boolean AEventCenterEnable = false;

    //新消息标记
    public static Boolean hasLogout = false;

    public static boolean hasNewC2CMsg = false;
    public static boolean hasNewGroupMsg = false;

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

        IM_SERVER_URL           = loadSharedData(context,"IM_SERVER_URL",IM_SERVER_URL);
        AEventCenterEnable = !loadSharedData(context, "AEC_ENABLE", "0").equals("0");
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
    public static List<HistoryBean> getHistoryListInOwner(String type, String id){
        if(historyDao!=null){
            return historyDao.getHistoryInOwner(type,id);
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

    public static List<MessageBean> getMessageList(String targetId, String fromId){
        if(messageDao!=null){
            return messageDao.getMessageList(targetId,fromId);
        }else{
            return null;
        }
    }

    public static void saveMessage(MessageBean messageBean){
        if(messageDao!=null){
            List<MessageBean> list = messageDao.isInMessageBean(messageBean.getTargetId(),messageBean.getFromId(),messageBean.getMsg());
            if(list.isEmpty()){
                messageDao.setMessage(messageBean);
            }
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


    //新信息弹窗
    static Dialog[] dialogs = new Dialog[1];
    static Timer dialogTimer ;
    static TimerTask timerTask;
    public static void showDialog(final Context context, final JSONObject data) throws JSONException {
        try {
            final int type = data.getInt("listType");// 0:c2c,1:group,2:voip
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

    //头像_使用本地头像
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
}

