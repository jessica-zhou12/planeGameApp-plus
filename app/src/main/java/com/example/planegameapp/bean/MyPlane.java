package com.example.planegameapp.bean;

import android.graphics.Bitmap;

public class MyPlane extends Plane {

    public MyPlane(){

    }
    public MyPlane(int plane_x, int plane_y, Bitmap planeImg, int planeImgWidth, int planeImgHeight, int hp, int maxHp, int deadTime, int planeType) {
        super(plane_x, plane_y, planeImg, planeImgWidth, planeImgHeight, hp, maxHp, deadTime, planeType);
    }

    @Override
    public void planeMove() {
        //本方飞机向上移动
        this.plane_y-=10;
    }
}
