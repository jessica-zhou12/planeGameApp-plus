package com.example.planegameapp.threads;

import com.example.planegameapp.MainActivity;
import com.example.planegameapp.views.GameView;

public class MyPlaneMoveThread implements Runnable{

    private GameView gameView;

    public MyPlaneMoveThread(GameView gameView){
        this.gameView = gameView;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(50);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            //设置本方飞机的移动
            gameView.getMyPlane().planeMove();
            //当本机移动到距离底部400dp的时候，线程结束
            if(MainActivity.screenHeight-300>=gameView.getMyPlane().getPlane_y()){
                //可以创建子弹
                gameView.setIscreateBullet(true);

                break;
            }
        }
    }
}

