package com.example.occcccccccichat.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.occcccccccichat.Tool.MyApplication;
import com.example.occcccccccichat.databinding.ActivitySignupBinding;

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

    private void signup(){}
}