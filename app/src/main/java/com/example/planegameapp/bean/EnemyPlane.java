package com.example.planegameapp.bean;

import android.graphics.Bitmap;

import com.example.planegameapp.MainActivity;

public class EnemyPlane extends Plane {

    private int speed;

    /**
     * 敌人飞机的分数
     */
    private int score;

    public EnemyPlane() {

    }

    public EnemyPlane(int plane_x, int plane_y, Bitmap planeImg, int planeImgWidth, int planeImgHeight, int hp, int maxHp, int deadTime, int planeType, int speed, int score) {
        super(plane_x, plane_y, planeImg, planeImgWidth, planeImgHeight, hp, maxHp, deadTime, planeType);
        this.speed = speed;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public void planeMove() {
        //修改敌人飞机的y坐标
        this.plane_y+=this.speed;

        //当敌人飞机的y坐标大于了设备的高度则标记该飞机死亡
        if(this.plane_y> MainActivity.screenHeight){
            this.islive=false;
        }
    }
}
