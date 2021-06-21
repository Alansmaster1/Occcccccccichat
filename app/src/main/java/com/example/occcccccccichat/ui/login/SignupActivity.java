package com.example.occcccccccichat.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.occcccccccichat.R;
import com.example.occcccccccichat.Tool.MyApplication;
import com.example.occcccccccichat.databinding.ActivitySignupBinding;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());

        binding.btnSignupSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.editTextPasswdSignup.getText().toString().equals(binding.editTextPasswdSignupRepeat.getText().toString())){
                    signup();
                } else {
                    Toast.makeText(MyApplication.context,"密码不一致",Toast.LENGTH_SHORT).show();
                }
            }
        });

        setContentView(binding.getRoot());
    }

    private void signup(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request
                        .Builder()
                        .url("http://42.192.202.168/ochichat/checkSignup.php?"
                                +"name="+binding.editTextNameSignup.getText().toString()
                                +"&passwd="+binding.editTextPasswdSignup.getText().toString())
                        .get()
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        readResult(result);
                        response.body().close();
                    }
                });
            }
        }).start();
    }

    private void readResult(String result){
        if(result!=null){
            Looper.prepare();
            if(!result.equals("-1")){
                AlertDialog.Builder dialog = new AlertDialog.Builder(SignupActivity.this);
                dialog.setTitle("注意!");
                dialog.setMessage("注册ID: "+result);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.setNegativeButton("前往登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                        finish();
                    }
                });
                dialog.show();
            } else {
                Toast.makeText(SignupActivity.this, "ID或密码不正确", Toast.LENGTH_SHORT).show();
            }
            Looper.loop();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}