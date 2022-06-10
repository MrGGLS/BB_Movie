package com.ggls.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.ggls.myapp.MainActivity;
import com.ggls.myapp.R;
import com.ggls.myapp.api.APIConfig;
import com.ggls.myapp.entity.MovieEntity;
import com.ggls.myapp.utils.AppUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {
    private EditText account;
    private EditText password;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        account = findViewById(R.id.register_account);
        password = findViewById(R.id.register_pwd);
        registerButton = findViewById(R.id.register_page_btn);
        registerButton.setOnClickListener((v) -> {
            String accStr = account.getText().toString().trim();
            String pwdStr = password.getText().toString().trim();
            register(accStr, pwdStr);
        });
    }

    private void register(String account, String password) {
        if (AppUtils.isStrEmpty(account)) {
            showToast("account name can't be empty!");
            return;
        }
        if (AppUtils.isStrEmpty(password)) {
            showToast("password can't be empty");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder()
                            .add("username", account)
                            .add("password", password)
                            .build();
                    Request request = new Request.Builder().url(APIConfig.USER_OPER_URL + APIConfig.USER_REGISTER).post(formBody).build();
                    Response response = client.newCall(request).execute();
                    String resp = response.body().string();//获取到的json数据
                    Gson gson = new Gson();
                    Integer flag = gson.fromJson(resp, Integer.class);
                    if (flag == 0) {
                        Looper.prepare();
                        showToast("register failed...");
                        Looper.loop();
                    } else {
                        AppUtils.USERNAME = account;
                        Looper.prepare();
                        showToast("Now back to login page...");
                        //TODO deal with register logics
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        finish();
                        startActivity(intent);
                        Looper.loop();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}