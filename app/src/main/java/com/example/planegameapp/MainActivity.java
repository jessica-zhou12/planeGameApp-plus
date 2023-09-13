package com.example.planegameapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.planegameapp.acticitys.GameActivity;
import com.example.planegameapp.acticitys.MusicActivity;
import com.example.planegameapp.firsttime.Screen_one;
import com.example.planegameapp.utils.StatusBarUtils;
import com.example.planegameapp.views.GameView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 游戏的开始界面
 */

public class MainActivity extends AppCompatActivity {

    volatile boolean StopFlag=false;
    Button startBtn,endBtn, buDis;
    public static int screenWidth,screenHeight;
    ImageView btmnav;
    CardView pop1, pop2, pop3, pop4;
    private String[] names = new String[]{"录取通知书", "学姐遗失的香水", "后悔药", "学长遗失的电脑"};

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;
    public static int flag;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBar();
        getWidthAndHeight();

//        init();
        //Chceking it's first time or not
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();
        if (isItFirestTime()) {

            Intent i = new Intent(MainActivity.this, Screen_one.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Welcome Back", Toast.LENGTH_SHORT).show();
        }

        //bottom dialogue
        btmnav = (ImageView)findViewById(R.id.bottom_nav);
        btmnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Category.class);
                Bundle bundle = new Bundle();
                bundle.putString("life",String.valueOf(GameView.getGamescore()));
                bundle.putString("score",String.valueOf(GameView.getGamescore()));
                bundle.putString("attack",String.valueOf(GameView.getGamescore()));
                bundle.putString("coins",String.valueOf(GameView.getGamescore()));
                i.putExtras(bundle);
                startActivity(i);
            }
        });



        pop1 = (CardView)findViewById(R.id.popular1);
        pop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Gameintro.class);
                startActivity(intent);

            }
        });

        pop2 = (CardView)findViewById(R.id.popular2);
        pop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                startActivity(intent);

            }
        });

        pop3 = (CardView)findViewById(R.id.popular3);
        pop3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
                flag = 0;

            }
        });
        pop4 = (CardView)findViewById(R.id.popular4);
        pop4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
                flag = 1;

            }
        });

        startBtn = findViewById(R.id.start_lucky);
//        String msg = "xxx";
//        Log.i(msg, String.valueOf(luckynum));
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_lucky(startBtn);
            }
        });


    }
    public void noluck(){
        //创建一个警告对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //, R.style.AlertDialog

        builder.setTitle("!注意！");
        builder.setMessage(" 您的两次抽奖次数已用完" );

        AlertDialog.Builder builder1 = builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"选择了确定",Toast.LENGTH_SHORT).show();
            }

        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this,"选择了取消",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        builder.setNeutralButton("再想想", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this,"选择了再想想",Toast.LENGTH_SHORT).show();
//
//            }
//        });

        AlertDialog alertDialog =builder.create();//这个方法可以返回一个alertDialog对象
        alertDialog.show();

    }

    int luckynum =4;
    public void start_lucky(Button btn) {
        //将view转换为button
        String title=btn.getText().toString();
        if(luckynum!=0) {
            if (title.equals("开始抽奖")) {
                //设置为暂停
                btn.setText("暂停");
                //创建定时器
                timer = new Timer();
                //每隔一段时间去执行一个任务
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                    }
                }, 0, 100);
            } else {
                btn.setText("开始抽奖");
                //关闭定时器
                timer.cancel();
            }
            produceOnePeople();
            luckynum--;
        }else {
            noluck();
        }
    }
    public void produceOnePeople(){
        Random random=new Random();
        int index= Math.abs(random.nextInt())%(names.length);
        String name=names[index];
        //把名字显示到文本框
        TextView tv=findViewById(R.id.show_lucky);
        tv.setText(name);
    }


    public boolean isItFirestTime() {
        if (sharedPreferences.getBoolean("firstTime", true)) {
            sharedEditor.putBoolean("firstTime", false);
            sharedEditor.commit();
            sharedEditor.apply();
            return true;
        } else {
            return false;
        }

    }
    public static int getflag() {
        return flag;
    }


    private void getWidthAndHeight() {
        WindowManager windowManager=this.getWindowManager();
        Display defaultDisplay=windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics=new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);
        screenWidth= displayMetrics.widthPixels;
        screenHeight= displayMetrics.heightPixels;
    }

    private void setStatusBar()
    {
//        StatusBarUtils.setColorNoTranslucent(this, getColor(R.color.blue_2196F3));
        StatusBarUtils.setTranslucent(this,0);
//        StatusBarUtils.setDarkMode(this);//设置状态栏字体颜色为白色
    }


}