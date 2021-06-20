package com.example.occcccccccichat

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.example.occcccccccichat.Tool.AEvent
import com.example.occcccccccichat.Tool.MLOC
import com.example.occcccccccichat.service.KeepLiveService
import com.example.occcccccccichat.ui.login.LoginActivity

class LauchActivity : AppCompatActivity() {
    private var isLogin:Boolean = false
    private val checkNetState: Boolean = false
    private var times:Int = 0
    private val REQUEST_PHONE_PERMISSION = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_lauch)
        AEvent.setHandler(Handler())
        checkPermission()
    }

    private fun checkPermission(){
        times++
        val permissionList: ArrayList<String> = ArrayList<String>()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)!=PackageManager.PERMISSION_GRANTED)
                permissionList.add(android.Manifest.permission.ACCESS_NETWORK_STATE)
            if(checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)
                permissionList.add(android.Manifest.permission.READ_PHONE_STATE)
            if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
                permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
                permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            if(checkSelfPermission(android.Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)
                permissionList.add(android.Manifest.permission.CAMERA)
            if(checkSelfPermission(android.Manifest.permission.BLUETOOTH)!=PackageManager.PERMISSION_GRANTED)
                permissionList.add(android.Manifest.permission.BLUETOOTH)
            if(checkSelfPermission(android.Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)
                permissionList.add(android.Manifest.permission.RECORD_AUDIO)

            if(permissionList.isNotEmpty()){
                if(times == 1){
                    requestPermissions(permissionList.toArray(arrayOf()),REQUEST_PHONE_PERMISSION)
                } else {
                    AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setMessage("请给APP授权")
                        .setPositiveButton("确定",DialogInterface.OnClickListener{ dialogInterface: DialogInterface, i: Int ->
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                requestPermissions(permissionList.toArray(arrayOf()),REQUEST_PHONE_PERMISSION)
                            }
                        }).setNegativeButton("取消",DialogInterface.OnClickListener{ dialogInterface: DialogInterface, i: Int ->
                            finish();
                        }).show()
                }
            } else {
                initSDK()
            }
        } else {
            initSDK()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkPermission()
    }

    private fun initSDK(){
        startService()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startService(){
        //TODO:在创建服务之前先进入登陆注册界面
        val intent = Intent(this, KeepLiveService::class.java)
        startService(intent)
    }

    private fun checkLogin(){
        if(MLOC.userState.equals("logout")){
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

}