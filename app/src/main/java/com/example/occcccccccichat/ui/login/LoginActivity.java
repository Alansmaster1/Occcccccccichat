package com.example.occcccccccichat.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.example.occcccccccichat.MainActivity;
import com.example.occcccccccichat.Tool.MLOC;
import com.example.occcccccccichat.Tool.MyApplication;
import com.example.occcccccccichat.databinding.ActivityLoginBinding;
import com.example.occcccccccichat.service.KeepLiveService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.LoggingPermission;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });

        setContentView(binding.getRoot());
    }

    private void login(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request
                        .Builder()
                        .url("http://42.192.202.168/ochichat/checkLogin.php?"
                                +"id="+binding.editTextId.getText().toString()
                                +"&passwd="+binding.editTextPasswd.getText().toString())
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
            if (result.equals("验证成功")) {
                Looper.prepare();
                MLOC.userId = binding.editTextId.getText().toString();
                startService(new Intent(LoginActivity.this, KeepLiveService.class));
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                Looper.loop();
            } else {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, "ID或密码不正确", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    }
}