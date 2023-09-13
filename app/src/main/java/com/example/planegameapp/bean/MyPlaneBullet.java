package com.example.planegameapp.bean;

import android.graphics.Bitmap;

/**
 * 本方飞机的子弹类
 */
public class MyPlaneBullet extends Bullet {

    public MyPlaneBullet() {
    }

    public MyPlaneBullet(int bullet_x, int bullet_y, Bitmap bulletImg, int bulletImgWidth, int bulletImgHeight, int speed) {
        super(bullet_x, bullet_y, bulletImg, bulletImgWidth, bulletImgHeight, speed);
    }

    @Override
    public void bulletMove() {
        this.bullet_y-=this.speed;
        //判断子弹是否越过边界
        if(this.bullet_y<=this.bulletImgHeight){
            //把子弹的状态设置为死亡状态
            this.islive=false;
        }
    }
}
