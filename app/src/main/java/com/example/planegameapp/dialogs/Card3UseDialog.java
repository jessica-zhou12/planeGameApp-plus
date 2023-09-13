package com.example.planegameapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.planegameapp.R;
import com.example.planegameapp.acticitys.GameActivity;
import com.example.planegameapp.views.GameView;

/**
 * 免试卡使用的对话框
 */
public class Card3UseDialog extends Dialog {
    /**
     * 对话框中的两个按钮
     */
    private Button Card3UseCheckBtn,Card3UsePlayBtn;
    //自定义视图
    private GameView gameView;

    public Card3UseDialog(@NonNull Context context) {
        super(context);
    }

    public Card3UseDialog(@NonNull Context context,GameView gameView) {
        super(context);
        this.gameView = gameView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示自定义布局
        setContentView(R.layout.dialog_card3_use);
        //获得按钮的对象
        Card3UseCheckBtn = findViewById(R.id.card3_use_checkBtn);
        Card3UsePlayBtn = findViewById(R.id.card3_use_playBtn);
        //给两个按钮绑定点击事件
        //继续游戏
        Card3UsePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("回到游戏");
                //把当前的对话框关闭
                Card3UseDialog.this.dismiss();
                //设置isPLay的属性来继续游戏
                gameView.setPLay(true);

            }
        });
        //确认使用重修卡
        Card3UseCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("重新开始游戏");
                //把当前的对话框关闭
                Card3UseDialog.this.dismiss();
                //重新加载游戏界面
                Intent intent=new Intent(getContext(), GameActivity.class);
                //重新激活游戏界面
                getContext().startActivity(intent);
            }
        });
    }
}
