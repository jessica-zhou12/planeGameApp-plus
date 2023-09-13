package com.example.planegameapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText name;  //创建账号
    EditText passwd;  //创建密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.name);   //获取输入的账号
        passwd = findViewById(R.id.passwd);     //获取输入的密码
    }

    //添加按钮事件
    public void Login(View v) {


        //这是能够登录的账号密码
        String Usename = "admin";
        String Upwd = "12345";

        //创建两个String类，储存从输入文本框获取到的内容ad
        String user = name.getText().toString().trim();
        String pwd = passwd.getText().toString().trim();

        //进行判断，如果两个内容都相等，就显现第一条语句，反之，第二条。
        if (user.equals(Usename) & pwd.equals(Upwd)) {
            Toast.makeText(this, "欢迎你，新世界学习者", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, GuideActivity.class);
            startActivity(intent);
       } else {
            Toast.makeText(this, "身份验证错误，禁止访问", Toast.LENGTH_LONG).show();
        }

    }

}