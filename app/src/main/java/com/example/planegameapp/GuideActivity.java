package com.example.planegameapp;

import static android.os.SystemClock.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class GuideActivity extends AppCompatActivity {

    private int i = 0;
    ImageView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        final TextView text = findViewById(R.id.text_content);
        final String a = "现实世界的你学累了吗？休息一下吧，接下来让小a帮你学一会！";
        final Handler handler = new Handler(Looper.getMainLooper()){
           @Override
            public void handleMessage(Message msg) {
               if (msg.what == 1) {
                   text.setText(msg.obj + "");

               }
           }
        };
        final Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                if(i < a.length()){
                    Message msg = new Message();
                    msg.what=1;
                    msg.obj=a.substring(0, i+1);
                    handler.sendMessage(msg);
                    i++;
                }else {
                    timer.cancel();
                }
            }
        },1000,100);

        next = findViewById(R.id.enter_game);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}