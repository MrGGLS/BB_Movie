package com.ggls.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.ggls.myapp.activity.BaseActivity;
import com.ggls.myapp.activity.LoginActivity;
import com.ggls.myapp.activity.RegisterActivity;

public class MainActivity extends BaseActivity {
    private Button loginButton;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.login_btn);
        loginButton.setOnClickListener((v)->{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        registerButton = findViewById(R.id.register_btn);
        registerButton.setOnClickListener((v)->{
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}