package com.example.planegameapp.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.planegameapp.R;

import java.util.HashMap;
import java.util.Map;

public final class MusicUtil {
    public MusicUtil() {
    }

    public static SoundPool soundPool=new SoundPool(13, AudioManager.STREAM_MUSIC,0);
    public static Map<Integer,Integer> sounds = new HashMap<Integer, Integer>();
    public static void initMusic(Context context){
        sounds.put(1,soundPool.load(context, R.raw.game_music,1));
        sounds.put(2,soundPool.load(context,R.raw.bullet,1));
        sounds.put(3,soundPool.load(context,R.raw.enemy0_down,1));
        sounds.put(4,soundPool.load(context,R.raw.enemy1_down,1));
        sounds.put(5,soundPool.load(context,R.raw.enemy2_down,1));
        sounds.put(6,soundPool.load(context,R.raw.game_over,1));
        sounds.put(7,soundPool.load(context,R.raw.get_bomb,1));
        sounds.put(8,soundPool.load(context,R.raw.use_bomb,1));

    }
}
