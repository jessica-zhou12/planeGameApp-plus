package com.example.planegameapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.planegameapp.MainActivity;
import com.example.planegameapp.R;
import com.example.planegameapp.acticitys.GameActivity;
import com.example.planegameapp.views.GameView;

/**
 * 游戏暂停的对话框
 */
public class GamePauseDialog extends Dialog {
    /**
     * 对话框中的两个按钮
     */
    private Button shopBtn,replayBtn,pause_playBtn,backBtn;
    //自定义视图
    private GameView gameView;

    public GamePauseDialog(@NonNull Context context) {
        super(context);
    }

    public GamePauseDialog(@NonNull Context context,GameView gameView) {
        super(context);
        this.gameView = gameView;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示自定义布局
        setContentView(R.layout.dialog_game_pause);
        //获得按钮的对象
        pause_playBtn = findViewById(R.id.pause_playBtn);
        replayBtn = findViewById(R.id.pause_replayBtn);
        shopBtn = findViewById(R.id.pause_shopBtn);
        backBtn = findViewById(R.id.pause_backBtn);
        //给两个按钮绑定点击事件
        //继续游戏
        pause_playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("回到游戏");
                //把当前的对话框关闭
                GamePauseDialog.this.dismiss();
                //设置isPLay的属性来继续游戏
                gameView.setPLay(true);

            }
        });
        //重新开始游戏
        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("重新开始游戏");
                //把当前的对话框关闭
                GamePauseDialog.this.dismiss();
                //重新加载游戏界面
                Intent intent=new Intent(getContext(), GameActivity.class);
                //重新激活游戏界面
                getContext().startActivity(intent);
            }
        });
        //能量补充（商店）
        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //把当前的对话框关闭
                GamePauseDialog.this.dismiss();
                ShopDialog shopDialog = new ShopDialog(getContext(),gameView);
                //设置该对话框为一个模态框
                shopDialog.setCancelable(false);
                //显示对话框
                shopDialog.show();
            }
        });
        //退出游戏返回主菜单
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }
}
