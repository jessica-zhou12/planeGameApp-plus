package com.example.planegameapp.bean;

import android.graphics.Bitmap;

/**
 * 飞机类
 */

public abstract class Plane {
    /**
     * 飞机的x坐标和y坐标
     */
    protected int plane_x,plane_y;
    /**
     * 飞机的图片
     */
    protected Bitmap planeImg;
    /**
     * 飞机图片的宽和高
     */
    protected int planeImgWidth,planeImgHeight;
    /**
     * 飞机的血量
     */
    protected int hp;
    /**
     * 飞机的最大血量
     */
    protected int maxHp;
    /**
     * 飞机的状态
     */
    protected boolean islive;
    /**
     * 飞机的死亡时间
     */
    protected int deadTime;
    /**
     * 飞机类型
     */
    protected int planeType;

    public Plane(){

    }

    /**
     *
     * @param plane_x x坐标
     * @param plane_y y坐标
     * @param planeImg  飞机的图片对象
     * @param planeImgWidth 飞机图片的宽
     * @param planeImgHeight    飞机图片的高
     * @param hp    生命值
     * @param maxHp 最大生命值
     * @param deadTime  死亡时间
     * @param planeType 飞机类型
     */
    public Plane(int plane_x, int plane_y, Bitmap planeImg, int planeImgWidth, int planeImgHeight, int hp, int maxHp, int deadTime, int planeType) {
        this.plane_x = plane_x;
        this.plane_y = plane_y;
        this.planeImg = planeImg;
        this.planeImgWidth = planeImgWidth;
        this.planeImgHeight = planeImgHeight;
        this.hp = hp;
        this.maxHp = maxHp;
        this.deadTime = deadTime;
        this.planeType = planeType;
        this.islive=true;
    }



    /**
     * 飞机的移动方式
     */
    public abstract void planeMove();

    public float getPlane_x() {
        return plane_x;
    }

    public void setPlane_x(int plane_x) {
        this.plane_x = plane_x;
    }

    public float getPlane_y() {
        return plane_y;
    }

    public void setPlane_y(int plane_y) {
        this.plane_y = plane_y;
    }

    public Bitmap getPlaneImg() {
        return planeImg;
    }

    public void setPlaneImg(Bitmap planeImg) {
        this.planeImg = planeImg;
    }

    public int getPlaneImgWidth() {
        return planeImgWidth;
    }

    public void setPlaneImgWidth(int planeImgWidth) {
        this.planeImgWidth = planeImgWidth;
    }

    public int getPlaneImgHeight() {
        return planeImgHeight;
    }

    public void setPlaneImgHeight(int planeImgHeight) {
        this.planeImgHeight = planeImgHeight;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public boolean isIslive() {
        return islive;
    }

    public void setIslive(boolean islive) {
        this.islive = islive;
    }

    public int getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(int deadTime) {
        this.deadTime = deadTime;
    }

    public int getPlaneType() {
        return planeType;
    }

    public void setPlaneType(int planeType) {
        this.planeType = planeType;
    }
}
