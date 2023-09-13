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
public class ShopDialog extends Dialog {
    /**
     * 对话框中的两个按钮
     */
    private Button backBtn;
    //自定义视图
    private GameView gameView;

    public ShopDialog(@NonNull Context context) {
        super(context);
    }

    public ShopDialog(@NonNull Context context,GameView gameView) {
        super(context);
        this.gameView = gameView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示自定义布局
        setContentView(R.layout.dialog_shop);
        //获得按钮的对象
        backBtn = findViewById(R.id.shop_backBtn);
        //给两个按钮绑定点击事件
        //继续游戏
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("回到游戏");
                //把当前的对话框关闭
                ShopDialog.this.dismiss();
                //设置isPLay的属性来继续游戏
                gameView.setPLay(true);

            }
        });

    }
}
