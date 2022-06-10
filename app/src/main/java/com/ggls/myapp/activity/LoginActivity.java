package com.ggls.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;

import com.ggls.myapp.R;
import com.ggls.myapp.api.APIConfig;
import com.ggls.myapp.utils.AppUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    private EditText account;
    private EditText password;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        account = findViewById(R.id.login_account);
        password = findViewById(R.id.login_pwd);
        loginButton = findViewById(R.id.login_page_btn);
        loginButton.setOnClickListener((v) -> {
            String accStr = account.getText().toString().trim();
            String pwdStr = password.getText().toString().trim();
            login(accStr, pwdStr);
        });
    }

    private void login(String account, String password) {
        if (AppUtils.isStrEmpty(account)) {
            showToast("account name can't be empty!");
            return;
        }
        if (AppUtils.isStrEmpty(password)) {
            showToast("password can't be empty");
            return;
        }
        Integer flag;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody.Builder()
                            .add("username", account)
                            .add("password", password)
                            .build();
                    Request request = new Request.Builder().url(APIConfig.USER_OPER_URL + APIConfig.USER_LOGIN).post(formBody).build();
                    Response response = client.newCall(request).execute();
                    String resp = response.body().string();//获取到的json数据
                    Gson gson = new Gson();
                    Integer flag = gson.fromJson(resp, Integer.class);
                    if (flag == 0) {
                        Looper.prepare();
                        showToast("wrong account or password!");
                        Looper.loop();
                        return;
                    } else {
                        //TODO deal with login logics
                        AppUtils.USERNAME = account;
                        Looper.prepare();
                        showToast("Successfully login!");
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
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