package com.example.planegameapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.planegameapp.MainActivity;
import com.example.planegameapp.R;
import com.example.planegameapp.bean.Bullet;
import com.example.planegameapp.bean.EnemyPlane;
import com.example.planegameapp.bean.MyPlane;
import com.example.planegameapp.bean.MyPlaneBullet;
import com.example.planegameapp.dialogs.GameOverDialog;
import com.example.planegameapp.dialogs.GamePauseDialog;
import com.example.planegameapp.dialogs.Card1UseDialog;//重修卡使用确认界面
import com.example.planegameapp.dialogs.Card2UseDialog;//超常发挥卡使用确认界面
import com.example.planegameapp.dialogs.Card3UseDialog;//免试卡使用确认界面
import com.example.planegameapp.threads.GameRunThread;
import com.example.planegameapp.threads.MyPlaneMoveThread;
import com.example.planegameapp.utils.MusicUtil;

import java.util.ArrayList;
import java.util.List;


public class GameView extends View {

    private Paint paint;

    private Bitmap bgBitImg_1,bgBitImg_2;

    private int bgImgWidth,bgImgHeight;
    //设置背景的坐标
    private  int bgImg1_x,bgImg1_y;
    private  int bgImg2_x,bgImg2_y;
    //本方飞机
    private MyPlane myPlane;
    //创建一个集合来管理所有敌机(为了避免空指针异常，所以先把集合new出来)
    private List<EnemyPlane> enemyPlaneList = new ArrayList<EnemyPlane>();
    //创建一个集合来管理本方飞机的子弹
    private List<MyPlaneBullet> myPlaneBulletList =new ArrayList<>();
    //声明一个变量来记录是否可以创建子弹
    private boolean iscreateBullet;
    //声明一个变量来记录总分数
    private static int gamescore;
    //声明一个游戏是否可能的状态
    private  boolean isPLay=true;
    //处理非主线程操作ui的handler
    private Handler uiHandler;
    //技能
    private boolean isSkill;
    //敌机死亡计数
    private int enem1,enem2,enem3;



    public GameView(Context context) {
        super(context);

        //初始化音频
        MusicUtil.initMusic(context);
        paint = new Paint();
        initBgImg();
        //创建本方飞机
        myPlane=new MyPlane((MainActivity.screenWidth-100)/2,MainActivity.screenHeight+30,BitmapFactory.decodeResource(getResources(),R.drawable.hero1),100,124,0,0,20,0);
        //创建处理ui的handler
        uiHandler = new Handler(Looper.myLooper()){
            //重写处理消息的方法
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        //创建游戏结束的对话框
                        GameOverDialog gameOverDialog = new GameOverDialog(getContext());
                        gameOverDialog.setCancelable(false);
                        gameOverDialog.show();
                        //获得显示分数的文本控件
                        TextView scoreView = gameOverDialog.getGameoverScore();
                        //设置游戏分数
                        scoreView.setText("学分："+gamescore);
                        break;

                }
            }
        };

        //本方飞机的出场方式
        new Thread(new MyPlaneMoveThread(this)).start();

        //开启线程开始游戏
        new Thread(new GameRunThread(this)).start();
    }



    private void initBgImg() {
        bgBitImg_1 = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        bgBitImg_2 = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        bgImgWidth=bgBitImg_1.getWidth();
        bgImgHeight=bgBitImg_1.getHeight();

        bgImg1_x=bgImg1_y=0;
        bgImg2_y =  -MainActivity.screenHeight;

    }

    //重写draw方法
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //画背景
        drawBg(canvas,paint);
        //画本方飞机
        canvas.drawBitmap(myPlane.getPlaneImg(),myPlane.getPlane_x(),myPlane.getPlane_y(),paint);
        //画敌人飞机
        drawEnemyPlanes(canvas,paint);
        //画本方飞机的子弹
        drawMyPlaneBullets(canvas,paint);
        //画分数（学分）
        drawScore(canvas,paint);
        //画暂停
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.game_pause_nor),MainActivity.screenWidth-100,60,paint);
        //画技能
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bomb),20,MainActivity.screenHeight-60,paint);
        //画死亡敌机数1 → 画重修卡card1
        //drawEnemDead1(canvas,paint);
        drawCard1(canvas,paint);
        //画死亡敌机数2 → 超常发挥卡card2
        //drawEnemDead2(canvas,paint);
        drawCard2(canvas,paint);
        //画死亡敌机数3 → 画免试卡card3
        //drawEnemDead3(canvas,paint);
        drawCard3(canvas,paint);
        //画学历
        drawLevel(canvas,paint);
        //画金币
        drawMoney(canvas,paint);
    }


    //画金币
    private void drawMoney(Canvas canvas, Paint paint) {
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.money),340,50,paint);

        paint.setColor(Color.RED);
        paint.setTextSize(30);

        canvas.drawText(this.enem3 +"S",400,88,paint);
    }
    //三号敌机死亡数量 → 免试卡数量
    private void drawCard3(Canvas canvas, Paint paint) {
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.card3),800,50,paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(30);

        canvas.drawText(this.enem3 +"张",890,90,paint);
    }
    //二号敌机死亡数量 → 超常发挥卡
    private void drawCard2(Canvas canvas, Paint paint) {
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.card2),650,50,paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(30);

        canvas.drawText(this.enem2 + "张",740,90,paint);
    }
    //一号敌机死亡数量 → 重修卡
    private void drawCard1(Canvas canvas, Paint paint) {
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.card1),500,50,paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(30);

        canvas.drawText(this.enem1 + "张",590,90,paint);
    }
    //画学分
    private void drawScore(Canvas canvas, Paint paint) {
        //画分数的图标
        //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.plane),60,60,paint);
        //修改画笔的颜色和大小
        paint.setColor(Color.RED);
        paint.setTextSize(30);
        //画分数
        canvas.drawText("学分: "+this.gamescore,200,90,paint);
    }

    private void drawLevel(Canvas canvas, Paint paint) {
        //修改画笔的颜色和大小
        paint.setColor(Color.RED);
        paint.setTextSize(30);
        //画分数
        canvas.drawText("学历: "+this.gamescore,20,90,paint);
    }

    //画本方飞机的子弹
    private void drawMyPlaneBullets(Canvas canvas, Paint paint) {
        //判断子弹集合中是否有子弹
        if(myPlaneBulletList.size()>0&&myPlaneBulletList!=null){
            //遍历画子弹
            for(int i=0; i< myPlaneBulletList.size();i++){
                Bullet bullet = myPlaneBulletList.get(i);
                //判断该子弹是否死亡
                if(bullet.isIslive()){
                    canvas.drawBitmap(bullet.getBulletImg(),bullet.getBullet_x(),bullet.getBullet_y(),paint);
                }
            }
        }
    }

    //画敌人飞机
    private void drawEnemyPlanes(Canvas canvas, Paint paint) {
//        //每一个敌机都有它自己的血条，所以画血条的时候要放到画飞机里

        //判断集合中有飞机的时候再画
        if(enemyPlaneList.size()>0&&enemyPlaneList!=null){
            //遍历敌人飞机的集合来画敌机
            for (int i = 0;i<enemyPlaneList.size();i++){
                //取出每一个敌机
                EnemyPlane enemyPlane = enemyPlaneList.get(i);
                //当前的飞机血量 >0 画血条
                if(enemyPlane.getHp()>0){
                    //设置画笔的颜色
                    paint.setColor(Color.BLACK);
                    //设置画笔的大小
                    paint.setTextSize(1);
                    //设置画笔的样式(描边：STROKE    Fill：填充)
                    paint.setStyle(Paint.Style.STROKE);
                    //画一个描边的矩形
                    canvas.drawRect(enemyPlane.getPlane_x(),enemyPlane.getPlane_y()-15,
                            enemyPlane.getPlane_x()+enemyPlane.getPlaneImgWidth(),enemyPlane.getPlane_y()-8,paint);
                    //设置画笔的颜色为红色
                    paint.setColor(Color.RED);
                    //设置画笔的样式
                    paint.setStyle(Paint.Style.FILL);
                    //画实体的矩形
                    //根据敌机的当前hp值与最大hp值的百分百来确定画多少hp
                    int hpwidth = (int) (((double)enemyPlane.getHp()/enemyPlane.getMaxHp())*enemyPlane.getPlaneImgWidth());

                    canvas.drawRect(enemyPlane.getPlane_x(),enemyPlane.getPlane_y()-14,
                            hpwidth+enemyPlane.getPlane_x(),enemyPlane.getPlane_y()-7,paint);

                }
                //画敌机
                canvas.drawBitmap(enemyPlane.getPlaneImg(),enemyPlane.getPlane_x(),enemyPlane.getPlane_y(),paint);
            }
        }
    }

    //画背景
    private void drawBg(Canvas canvas,Paint paint) {
        /**
         * decodeResoarce() 有两个参数 1资源 2图片的id号
         */
        //得到资源图片的矩形大小
        Rect srcRect_1 = new Rect(0,0,bgImgWidth,bgImgHeight);
        Rect srcRect_2 = new Rect(0,0,bgImgWidth,bgImgHeight);
        //得到目标矩形的大小
        Rect desRect_1 = new Rect(0,bgImg1_y, MainActivity.screenWidth,MainActivity.screenHeight+200);
        Rect desRect_2 = new Rect(0,bgImg2_y, MainActivity.screenWidth,MainActivity.screenHeight+200);


        //绘制背景
        canvas.drawBitmap(bgBitImg_1,srcRect_1,desRect_1,paint);
        canvas.drawBitmap(bgBitImg_2,srcRect_2,desRect_2,paint);
    }


    //重写onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //得到发生事件的类型
        int eventType = event.getAction();

        switch (eventType){
            //表示点击事件
            case MotionEvent.ACTION_DOWN:
                //判断当前鼠标的x坐标和y坐标是否在暂停图标的范围内
                if (
                        (event.getX()>=MainActivity.screenWidth-100 && event.getX()<=MainActivity.screenWidth-100+42)&&
                                (event.getY()>=60 && event.getY()<=60+70)
                ){
                    //System.out.println("点击了暂停");
                    isPLay=false;
                    //创建暂停游戏对话框
                    GamePauseDialog gamePauseDialog = new GamePauseDialog(getContext(),this);
                    //设置该对话框为一个模态框
                    gamePauseDialog.setCancelable(false);
                    //显示对话框
                    gamePauseDialog.show();
                    //注意：对话框只有在显示出来之后，才会被初始化对话框的属性

                }
                //判断当前鼠标的x坐标和y坐标是否在技能图标的范围内
//                if (
//                        (event.getX()>=0 && event.getX()<=100)&&
//                                (event.getY()>=MainActivity.screenHeight-100 && event.getY()<=MainActivity.screenHeight)
//                )isSkill=true;
//                break;
//                //判断当前鼠标x坐标和y坐标是否在重修卡图标范围内
                else if (
                        (event.getX()>=500 && event.getX()<=590)&&
                                (event.getY()>=60 && event.getY()<=60+70)
                ){
                    //System.out.println("点击了重修卡");
                    isPLay=false;
                    //创建重修游戏对话框
                    Card1UseDialog card1UseDialogDialog = new Card1UseDialog(getContext(),this);
                    //设置该对话框为一个模态框
                    card1UseDialogDialog.setCancelable(false);
                    //显示对话框
                    card1UseDialogDialog.show();
                    //注意：对话框只有在显示出来之后，才会被初始化对话框的属性

                }
                else if (
                        (event.getX()>=650 && event.getX()<=740)&&
                                (event.getY()>=60 && event.getY()<=60+70)
                ){
                    //System.out.println("点击了超常发挥卡");
                    isPLay=false;
                    //创建超常发挥对话框
                    Card2UseDialog card2UseDialogDialog = new Card2UseDialog(getContext(),this);
                    //设置该对话框为一个模态框
                    card2UseDialogDialog.setCancelable(false);
                    //显示对话框
                    card2UseDialogDialog.show();
                    //注意：对话框只有在显示出来之后，才会被初始化对话框的属性

                }
                else if (
                        (event.getX()>=800 && event.getX()<=890)&&
                                (event.getY()>=60 && event.getY()<=60+70)
                ){
                    //System.out.println("点击了免试卡");
                    isPLay=false;
                    //创建免试卡使用确认对话框
                    Card3UseDialog card3UseDialogDialog = new Card3UseDialog(getContext(),this);
                    //设置该对话框为一个模态框
                    card3UseDialogDialog.setCancelable(false);
                    //显示对话框
                    card3UseDialogDialog.show();
                    //注意：对话框只有在显示出来之后，才会被初始化对话框的属性

                }

            //移动事件
            case MotionEvent.ACTION_MOVE:
                //修改飞机的坐标来移动本方飞机
                //获得鼠标的坐标,同时避免点击卡片时造成飞机移动
                if( event.getY()>60+70
                ){
                    myPlane.setPlane_x((int) event.getX() - myPlane.getPlaneImgWidth()/2);
                    myPlane.setPlane_y((int) event.getY() - myPlane.getPlaneImgHeight()/2);
                    break;
                }


        }
        //返回true 表示我已经处理了该事件，阻止事件的传播
        return true;
    }



    public int getBgImg1_x() {
        return bgImg1_x;
    }

    public void setBgImg1_x(int bgImg1_x) {
        this.bgImg1_x = bgImg1_x;
    }

    public int getBgImg1_y() {
        return bgImg1_y;
    }

    public void setBgImg1_y(int bgImg1_y) {
        this.bgImg1_y = bgImg1_y;
    }

    public int getBgImg2_x() {
        return bgImg2_x;
    }

    public void setBgImg2_x(int bgImg2_x) {
        this.bgImg2_x = bgImg2_x;
    }

    public int getBgImg2_y() {
        return bgImg2_y;
    }

    public void setBgImg2_y(int bgImg2_y) {
        this.bgImg2_y = bgImg2_y;
    }

    public MyPlane getMyPlane() {
        return myPlane;
    }

    public void setMyPlane(MyPlane myPlane) {
        this.myPlane = myPlane;
    }

    public List<EnemyPlane> getEnemyPlaneList() {
        return enemyPlaneList;
    }

    public void setEnemyPlaneList(List<EnemyPlane> enemyPlaneList) {
        this.enemyPlaneList = enemyPlaneList;
    }

    public List<MyPlaneBullet> getMyPlaneBulletList() {
        return myPlaneBulletList;
    }

    public void setMyPlaneBulletList(List<MyPlaneBullet> myPlaneBulletList) {
        this.myPlaneBulletList = myPlaneBulletList;
    }

    public boolean isIscreateBullet() {
        return iscreateBullet;
    }

    public void setIscreateBullet(boolean iscreateBullet) {
        this.iscreateBullet = iscreateBullet;
    }

    public static int getGamescore() {
        return gamescore;
    }

    public void setGamescore(int gamescore) {
        this.gamescore = gamescore;
    }

    public boolean isPLay() {
        return isPLay;
    }

    public void setPLay(boolean PLay) {
        isPLay = PLay;
    }

    public Handler getUiHandler() {
        return uiHandler;
    }

    public void setUiHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    public boolean isSkill() {
        return isSkill;
    }

    public void setSkill(boolean skill) {
        isSkill = skill;
    }

    public int getEnem1() {
        return enem1;
    }

    public void setEnem1(int enem1) {
        this.enem1 = enem1;
    }

    public int getEnem2() {
        return enem2;
    }

    public void setEnem2(int enem2) {
        this.enem2 = enem2;
    }

    public int getEnem3() {
        return enem3;
    }

    public void setEnem3(int enem3) {
        this.enem3 = enem3;
    }
}

