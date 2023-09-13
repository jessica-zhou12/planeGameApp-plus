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
 * 重修卡使用的对话框
 */
public class Card1UseDialog extends Dialog {
    /**
     * 对话框中的两个按钮
     */
    private Button Card1UseCheckBtn,Card1UsePlayBtn;
    //自定义视图
    private GameView gameView;

    public Card1UseDialog(@NonNull Context context) {
        super(context);
    }

    public Card1UseDialog(@NonNull Context context,GameView gameView) {
        super(context);
        this.gameView = gameView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示自定义布局
        setContentView(R.layout.dialog_card1_use);
        //获得按钮的对象
        Card1UseCheckBtn = findViewById(R.id.card1_use_checkBtn);
        Card1UsePlayBtn = findViewById(R.id.card1_use_playBtn);
        //给两个按钮绑定点击事件
        //继续游戏
        Card1UsePlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("回到游戏");
                //把当前的对话框关闭
                Card1UseDialog.this.dismiss();
                //设置isPLay的属性来继续游戏
                gameView.setPLay(true);

            }
        });
        //确认使用重修卡
        Card1UseCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("重新开始游戏");
                //把当前的对话框关闭
                Card1UseDialog.this.dismiss();
                //重新加载游戏界面
                Intent intent=new Intent(getContext(), GameActivity.class);
                //重新激活游戏界面
                getContext().startActivity(intent);
            }
        });
    }
}
