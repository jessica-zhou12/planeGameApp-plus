package com.example.planegameapp.bean;

import android.graphics.Bitmap;

/**
 * 子弹类的父类
 */
public abstract class Bullet {
    protected int bullet_x,bullet_y;

    protected Bitmap bulletImg;

    protected int bulletImgWidth,bulletImgHeight;

    protected boolean islive;

    protected int speed;

    public Bullet() {
    }

    public Bullet(int bullet_x, int bullet_y, Bitmap bulletImg, int bulletImgWidth, int bulletImgHeight, int speed) {
        this.bullet_x = bullet_x;
        this.bullet_y = bullet_y;
        this.bulletImg = bulletImg;
        this.bulletImgWidth = bulletImgWidth;
        this.bulletImgHeight = bulletImgHeight;
        this.speed = speed;
        this.islive = true;
    }

    public int getBullet_x() {
        return bullet_x;
    }

    public void setBullet_x(int bullet_x) {
        this.bullet_x = bullet_x;
    }

    public int getBullet_y() {
        return bullet_y;
    }

    public void setBullet_y(int bullet_y) {
        this.bullet_y = bullet_y;
    }

    public Bitmap getBulletImg() {
        return bulletImg;
    }

    public void setBulletImg(Bitmap bulletImg) {
        this.bulletImg = bulletImg;
    }

    public int getBulletImgWidth() {
        return bulletImgWidth;
    }

    public void setBulletImgWidth(int bulletImgWidth) {
        this.bulletImgWidth = bulletImgWidth;
    }

    public int getBulletImgHeight() {
        return bulletImgHeight;
    }

    public void setBulletImgHeight(int bulletImgHeight) {
        this.bulletImgHeight = bulletImgHeight;
    }

    public boolean isIslive() {
        return islive;
    }

    public void setIslive(boolean islive) {
        this.islive = islive;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public abstract void bulletMove();

}
