package com.example.planegameapp.acticitys;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planegameapp.MainActivity;
import com.example.planegameapp.R;
import com.example.planegameapp.dialogs.GamePauseDialog;

import java.util.HashMap;
import java.util.Map;

public class MusicActivity extends AppCompatActivity {

    private Button btn1,btn2,btn3,btn4,btnback;
    //音乐播放器
    private SoundPool soundPool;
    //使用一个集合存储音乐
    private Map<Integer,Integer> sounds;
    int playStram=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        //创建一个播放器
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,0);
        //把需要播放的音乐加载到播放器中
        sounds = new HashMap<Integer, Integer>();
        sounds.put(1,soundPool.load(this,R.raw.game_music,1));
        sounds.put(2,soundPool.load(this,R.raw.enemy0_down,1));
        sounds.put(3,soundPool.load(this,R.raw.bullet,1));
        sounds.put(4,soundPool.load(this,R.raw.get_bomb,1));
        sounds.put(5,soundPool.load(this,R.raw.use_bomb,1));


        btn1 =findViewById(R.id.btn_1);
        btn2 =findViewById(R.id.btn_2);
        btn3 =findViewById(R.id.btn_3);
        btn4 =findViewById(R.id.btn_4);
        btnback =findViewById(R.id.btn_back);


        //播放背景音乐
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//                    @Override
//                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                        //播放音乐
//                        if(playStram==0)playStram=soundPool.play(sounds.get(1),1,1,1,-1,1);
//                    }
//                });
//                    int playStram = soundPool.play(sounds.get(1),1,1,1,-1,1);
//                        sounds.put(1,playStram);
                if(playStram==0)playStram=soundPool.play(sounds.get(1),1,1,2,-1,1);
            }
        });
        //停止播放
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.stop(playStram);
                playStram=0;
            }
        });
        //播放飞机爆炸的音乐
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //播放飞机爆炸的声音
                soundPool.play(sounds.get(4),1,1,1,0,1);
            }
        });
        //播放子弹声音
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundPool.play(sounds.get(5),1,1,1,0,1);
            }
        });


        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //监听按钮，如果点击，就跳转
                Intent intent = new Intent();
                //前一个（MainActivity.this）是目前页面，后面一个是要跳转的下一个页面
                intent.setClass(MusicActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}