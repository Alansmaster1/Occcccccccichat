package com.example.occcccccccichat.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.occcccccccichat.Tool.MyApplication;
import com.example.occcccccccichat.databinding.ActivityLoginBinding;

import java.time.Duration;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLogin()) {
                    login();
                }
                else {
                    Toast.makeText(MyApplication.context,"ID或密码输入错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        setContentView(binding.getRoot());
    }

    private Boolean checkLogin(){
        return true;
    }

    private void login(){

    }
}