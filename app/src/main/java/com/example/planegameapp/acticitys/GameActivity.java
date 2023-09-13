package com.example.planegameapp.acticitys;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planegameapp.utils.StatusBarUtils;
import com.example.planegameapp.views.GameView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameView gameView=new GameView(this);
        setStatusBar();
        setContentView(gameView);


    }


    private void setStatusBar()
    {
        StatusBarUtils.setTranslucent(this,0);
    }
}