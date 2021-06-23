package com.example.occcccccccichat

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.*
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.occcccccccichat.Tool.AEvent
import com.example.occcccccccichat.Tool.MLOC
import com.example.occcccccccichat.Tool.MyApplication
import com.example.occcccccccichat.databinding.ActivityLaunchBinding
import com.example.occcccccccichat.service.KeepLiveService
import com.example.occcccccccichat.ui.login.LoginActivity
import com.starrtc.starrtcsdk.api.XHClient

class LaunchActivity : AppCompatActivity() {
    private var isLogin = false;
    private var times:Int = 0
    private val REQUEST_PHONE_PERMISSION = 0
    private lateinit var binding: ActivityLaunchBinding

    private val animationListener_logo:Animation.AnimationListener = object: Animation.AnimationListener{
        override fun onAnimationStart(animation: Animation?) {
            rotate_scale_positive(binding.logo1)
            startService()
        }

        override fun onAnimationEnd(animation: Animation?) {
            Fade_out(binding.logo2)
            rotate_scale_reverse(binding.logo1)
        }

        override fun onAnimationRepeat(animation: Animation?) {

        }

    }

    private val animationListener_finish:Animation.AnimationListener = object: Animation.AnimationListener{
        override fun onAnimationStart(animation: Animation?) {
            //TODO("Not yet implemented")
        }

        override fun onAnimationEnd(animation: Animation?) {
            //TODO("Not yet implemented")
            startActivity(Intent(this@LaunchActivity,MainActivity::class.java))
            finish()
        }

        override fun onAnimationRepeat(animation: Animation?) {
            //TODO("Not yet implemented")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        MLOC.init(MyApplication.context)
        checkLogin()
    }

    private fun startService(){
        val intent = Intent(this, KeepLiveService::class.java)
        startService(intent)
    }

    private fun checkLogin(){
        if(MLOC.userState.equals("logout")){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        } else if (MLOC.userState.equals("login")){
            Fade_in(binding.logo2)
//            startService()
//            startActivity(Intent(this@LaunchActivity,MainActivity::class.java))
        }
    }

    private fun Fade_in(view:View){
        val alphaAnimation = AlphaAnimation(0.0f,1.0f)
        alphaAnimation.apply {
            fillAfter = true
            duration = 2000
            interpolator = AccelerateInterpolator()
            setAnimationListener(animationListener_logo)
        }
        view.startAnimation(alphaAnimation)
    }

    private fun Fade_out(view:View){
        val alphaAnimation = AlphaAnimation(1.0f,0.0f)
        alphaAnimation.apply {
            fillAfter = true
            duration = 2000
            interpolator = DecelerateInterpolator()
            setAnimationListener((animationListener_finish))
        }
        view.startAnimation(alphaAnimation)
    }

    private fun rotate_scale_positive(view:View){
        val animation = AnimationUtils.loadAnimation(this,R.anim.rotate_scale_positive)
        animation.fillAfter = true
        view.startAnimation(animation)
    }

    private fun rotate_scale_reverse(view:View){
        val animation = AnimationUtils.loadAnimation(this,R.anim.rotate_scale_reverse)
        animation.fillAfter = true
        view.startAnimation(animation)
    }

}