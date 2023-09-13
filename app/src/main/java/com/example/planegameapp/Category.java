package com.example.planegameapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.planegameapp.views.GameView;


public class Category extends AppCompatActivity {

    Button btback;
    private TextView coinsShow, replayShow, attackShow, freeShow;

    private int coin_count = GameView.getGamescore();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        btback = (Button)findViewById(R.id.pack_back);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Category.this, MainActivity.class);
                startActivity(i);
            }
        });

        coinsShow = findViewById(R.id.replay_card);
        Bundle bundle = getIntent().getExtras();
        String coins = bundle.getString("coins");
        coinsShow.setText(coins);

        attackShow = findViewById(R.id.attack_card);
        String attacks = bundle.getString("attack");
        attackShow.setText(attacks);

        replayShow = findViewById(R.id.replay_card);
        String replay = bundle.getString("life");
        replayShow.setText(replay);

        freeShow = findViewById(R.id.free_card);
        String free = bundle.getString("score");
        freeShow.setText(free);


    }




}
