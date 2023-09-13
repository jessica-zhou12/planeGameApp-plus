package com.example.planegameapp.threads;

import android.graphics.BitmapFactory;
import android.media.SoundPool;
import android.os.Message;

import com.example.planegameapp.MainActivity;
import com.example.planegameapp.R;
import com.example.planegameapp.bean.EnemyPlane;
import com.example.planegameapp.bean.MyPlane;
import com.example.planegameapp.bean.MyPlaneBullet;
import com.example.planegameapp.utils.MusicUtil;
import com.example.planegameapp.views.GameView;

import java.util.List;

/**
 * 游戏开始的线程
 */

public class GameRunThread implements Runnable {

    private GameView gameView;

    //技能CD时间
    private int skillTime=0;
    //声明一个变量来记录飞机产生的时间（计数器）
    private int createEnemyPlaneTime;
    //声明一个变量来记录小飞机产生的个数（计数器）
    private int flyNum;
    //声明一个变量来记录创建了子弹时间（计数器）
    private int bulletCreateTime;


    boolean isPause=false;
    private int playStram=0;
    private int x=0;
    public GameRunThread(GameView gameView) {
        this.gameView = gameView;

    }

    @Override
    public void run() {

        while (true){
            MusicUtil.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    if (playStram==0)playStram=soundPool.play(MusicUtil.sounds.get(1),(float)0.5,(float) 0.5,2,-1,1);
                }
            });
            if(!gameView.isPLay()&&!isPause){
                MusicUtil.soundPool.pause(MusicUtil.sounds.get(1));
                isPause=true;
            }
            while (gameView.isPLay()){
                if(isPause){
                    MusicUtil.soundPool.resume(MusicUtil.sounds.get(1));
                    isPause=false;
                }
                try {
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                //技能CD时间
                if(skillTime!=0){
                    skillTime--;
                    if (skillTime==0)MusicUtil.soundPool.play(MusicUtil.sounds.get(7),1,1,1,0,1);
                }
                //记录创建敌机的时间
                createEnemyPlaneTime++;
                //记录子弹创建的时间
                bulletCreateTime++;
                x++;
                //修改背景图片的y坐标来实现bg的移动
                moveBg();
                //创建敌机
                createEmenyPlanes();
                //敌方飞机移动
                emenyPlaneMove();
                //创建子弹1、2、3
                createBullets();
                createBullets1();
                createBullets2();
                //子弹移动
                bulletMove();
                //本机与敌机的碰撞
                myPlaneAndEnemyPlaneCollide();
                //本机子弹与敌机发生碰撞
                myPlaneBulletAndEnemyPlaneCollide();
                //使用技能
                if (gameView.isSkill()){
                    if(skillTime==0) {
                        skill();
                    }
                    gameView.setSkill(false);
                }
                changeMyplane();
                //重绘自定义视图
                gameView.postInvalidate();
            }
        }
    }
    private void changeMyplane(){
        if (x%2==0){
            gameView.getMyPlane().setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.hero2));
        }else {
            gameView.getMyPlane().setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.hero1));
        }
        if (x>=100){
            x=0;
        }

    }
    private void skill() {
        MusicUtil.soundPool.play(MusicUtil.sounds.get(8),1,1,1,0,1);
        //获得子弹的集合
        List<MyPlaneBullet> myPlaneBulletList = gameView.getMyPlaneBulletList();
        //获得敌机的集合
        List<EnemyPlane> enemyPlaneList =gameView.getEnemyPlaneList();
        for (int i = 0; i < myPlaneBulletList.size(); i++) {
            MyPlaneBullet myPlaneBullet = myPlaneBulletList.get(i);
            myPlaneBullet.setIslive(false);
        }
        for (int i = 0; i < enemyPlaneList.size(); i++) {
            EnemyPlane enemyPlane = enemyPlaneList.get(i);
            enemyPlane.setIslive(false);
            enemyPlane.setHp(0);
            switch (enemyPlane.getPlaneType()){
                case 1:
                    enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy0_down4));
                    gameView.setEnem1(gameView.getEnem1()+1);
                    break;
                case 2:
                    enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy1_down4));
                    gameView.setEnem2(gameView.getEnem2()+1);
                    break;
                case 3:
                    enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy2_down6));
                    gameView.setEnem3(gameView.getEnem3()+1);
                    break;
                case 4:
                    enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy2_down6));
                    gameView.setEnem2(gameView.getEnem2()+1);
                    break;
            }
            //修改游戏界面的分数
            gameView.setGamescore(gameView.getGamescore()+enemyPlane.getScore());
        }
        skillTime=60;//3秒
    }

    //本机子弹与敌机发生碰撞
    private void myPlaneBulletAndEnemyPlaneCollide() {

        //获得子弹的集合
        List<MyPlaneBullet> myPlaneBulletList = gameView.getMyPlaneBulletList();
        //获得敌机的集合
        List<EnemyPlane> enemyPlaneList =gameView.getEnemyPlaneList();
        //遍历每颗子弹与敌机的碰撞
        for (int i = 0; i < myPlaneBulletList.size(); i++) {
            MyPlaneBullet myPlaneBullet = myPlaneBulletList.get(i);
            //遍历每一个敌机与该子弹是否发生碰撞
            for (int j = 0; j < enemyPlaneList.size(); j++) {
                //取出每一个敌机
                EnemyPlane enemyPlane = enemyPlaneList.get(j);
                //判断子弹的存活状态，只有活的子弹才判断与敌机碰撞
                if (myPlaneBullet.isIslive()&&enemyPlane.isIslive()){
                    if ((myPlaneBullet.getBullet_x()+myPlaneBullet.getBulletImgWidth()>=enemyPlane.getPlane_x())&&
                            (enemyPlane.getPlane_x()+enemyPlane.getPlaneImgWidth()>=myPlaneBullet.getBullet_x())&&
                            (myPlaneBullet.getBullet_y()+myPlaneBullet.getBulletImgHeight()>=enemyPlane.getPlane_y())&&
                            (enemyPlane.getPlane_y()+enemyPlane.getPlaneImgHeight()>=myPlaneBullet.getBullet_y())
                    ){
                        //把子弹的状态修改为死亡状态
                        myPlaneBullet.setIslive(false);
                        //修改敌机的hp值
                        if (enemyPlane.getHp()>0){
                            enemyPlane.setHp(enemyPlane.getHp()-1);
                            if (enemyPlane.getHp()==0){
//                                System.out.println("敌机死亡");
                                enemyPlane.setIslive(false);
                                //根据飞机的类型来设置死亡的图片
                                switch (enemyPlane.getPlaneType()){
                                    case 1:
                                        enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy0_down4));
                                        gameView.setEnem1(gameView.getEnem1()+1);
                                        break;
                                    case 2:
                                        enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy1_down4));
                                        gameView.setEnem2(gameView.getEnem2()+1);
                                        break;
                                    case 3:
                                        enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy2_down6));
                                        gameView.setEnem3(gameView.getEnem3()+1);
                                        break;
                                    case 4:
                                        enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy2_down6));
                                        gameView.setEnem2(gameView.getEnem2()+1);
                                        break;

                                }
                                //修改游戏界面的分数
                                gameView.setGamescore(gameView.getGamescore()+enemyPlane.getScore());
                            }else {
                                //根据类型和血量来显示不同的图片
                                switch (enemyPlane.getPlaneType()){
                                    case 1:
                                        MusicUtil.soundPool.play(MusicUtil.sounds.get(3),1,1,1,0,1);

                                        if (enemyPlane.getHp()<=1){
                                            enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy0_down3));
                                        }else if (enemyPlane.getHp()<=2){
                                            enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy0_down2));
                                        }else if (enemyPlane.getHp()<=3){
                                            enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy0_down1));
                                        }
                                        break;
                                    case 2:
                                        MusicUtil.soundPool.play(MusicUtil.sounds.get(4),1,1,1,0,1);

                                        if (enemyPlane.getHp()<=2){
                                            enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy1_down3));
                                        }else if (enemyPlane.getHp()<=4){
                                            enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy1_down2));
                                        }else if (enemyPlane.getHp()<=6){
                                            enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy1_down1));
                                        }else {
                                            enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy1_hit));
                                        }
                                        break;
                                    case 3:
                                        MusicUtil.soundPool.play(MusicUtil.sounds.get(5),1,1,1,0,1);

                                        if (enemyPlane.getHp()<=1){
                                            enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy2_down5));
                                        }else if (enemyPlane.getHp()<=2){
                                            enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy2_down4));
                                        }else if (enemyPlane.getHp()<=4){
                                            enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy2_down3));
                                        }else if (enemyPlane.getHp()<=8){
                                            enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy2_down2));
                                        }else if (enemyPlane.getHp()<=12){
                                            enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy2_down1));
                                        }else {
                                            enemyPlane.setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.enemy2_hit));
                                        }
                                        break;
                                }
                            }
                        }
                    }else {
                        //                    System.out.println("没有碰撞");
                    }
                }
            }
        }
    }

    //本机与敌机的碰撞
    private void myPlaneAndEnemyPlaneCollide() {

        //获得本方飞机
        MyPlane myPlane =gameView.getMyPlane();
        //获得敌方飞机集合
        List<EnemyPlane> enemyPlaneList=gameView.getEnemyPlaneList();
        //遍历所有敌机与本方飞机碰撞
        for (int i = 0; i < enemyPlaneList.size(); i++) {
            EnemyPlane enemyPlane = enemyPlaneList.get(i);
            //判断地方是否死亡
            if (enemyPlane.isIslive()){
                if (
                        (myPlane.getPlane_x() + myPlane.getPlaneImgWidth() +45 >= enemyPlane.getPlane_x())&&
                                (enemyPlane.getPlane_x() + enemyPlane.getPlaneImgWidth() -45>= myPlane.getPlane_x())&&
                                (myPlane.getPlane_y() + myPlane.getPlaneImgHeight()>= enemyPlane.getPlane_y())&&
                                (enemyPlane.getPlane_y() + enemyPlane.getPlaneImgHeight() -45>= myPlane.getPlane_y())
                ){
                    //System.out.println("发生碰撞");
                    //设置游戏不可玩
                    gameView.setPLay(false);
                    //显示飞机爆炸的图片
                    gameView.getMyPlane().setPlaneImg(BitmapFactory.decodeResource(gameView.getResources(),R.drawable.hero_blowup_n3));
                    MusicUtil.soundPool.play(MusicUtil.sounds.get(6),1,1,1,0,1);
                    //非主线程不能进行ui的操作，注意使用handler来通知主线程来操作ui
                    Message message = new Message();
                    message.what = 1;//表示需要显示游戏结束的对话框
                    //发生消息给处理ui的handler
                    gameView.getUiHandler().sendMessage(message);

                }
            }
        }
    }

    //移动子弹
    private void bulletMove() {
        List<MyPlaneBullet> myPlaneBulletList=gameView.getMyPlaneBulletList();
        if (myPlaneBulletList.size()>0 && myPlaneBulletList!=null){
            //遍历子弹的集合
            for (int i = 0; i < myPlaneBulletList.size(); i++) {
                MyPlaneBullet myPlaneBullet = myPlaneBulletList.get(i);
                //判断子弹是否存活
                if (myPlaneBullet.isIslive()){
                    myPlaneBullet.bulletMove();
                }else {
                    myPlaneBulletList.remove(i);
                    i--;
                }
            }
        }
    }

    //创建子弹1
    private void createBullets() {

        //判断是否可以创建子弹
        if (gameView.isIscreateBullet()){
            //当创建子弹的计数器为8的倍数时创建子弹
            if(bulletCreateTime%4==0){
                MusicUtil.soundPool.play(MusicUtil.sounds.get(2),(float)0.5,(float)0.5,1,0,1);
                MyPlaneBullet myPlaneBullet = new MyPlaneBullet(
                        (int)(gameView.getMyPlane().getPlane_x()+(gameView.getMyPlane().getPlaneImgWidth()-9)/2)-12,
                        (int)gameView.getMyPlane().getPlane_y()-21,
                        BitmapFactory.decodeResource(gameView.getResources(),R.drawable.bullet),
                        9,21,50);
                //把子弹创建好后添加到子弹的集合中
                gameView.getMyPlaneBulletList().add(myPlaneBullet);
            }
            //判断是否到达了边界值
            if (bulletCreateTime>1000){
                bulletCreateTime=0;
            }
        }

    }

    //创建子弹2
    private void createBullets1() {

        //判断是否可以创建子弹
        if (gameView.isIscreateBullet()){
            //当创建子弹的计数器为8的倍数时创建子弹
            if(bulletCreateTime%4==0){
                MyPlaneBullet myPlaneBullet = new MyPlaneBullet(
                        (int)(gameView.getMyPlane().getPlane_x()+(gameView.getMyPlane().getPlaneImgWidth()-9)/2)-36,
                        (int)gameView.getMyPlane().getPlane_y()-15,
                        BitmapFactory.decodeResource(gameView.getResources(),R.drawable.bullet1),
                        9,21,50);
                //把子弹创建好后添加到子弹的集合中
                gameView.getMyPlaneBulletList().add(myPlaneBullet);
            }
            //判断是否到达了边界值
            if (bulletCreateTime>1000){
                bulletCreateTime=0;
            }
        }

    }

    //创建子弹3
    private void createBullets2() {

        //判断是否可以创建子弹
        if (gameView.isIscreateBullet()){
            //当创建子弹的计数器为8的倍数时创建子弹
            if(bulletCreateTime%4==0){
                MyPlaneBullet myPlaneBullet = new MyPlaneBullet(
                        (int)(gameView.getMyPlane().getPlane_x()+(gameView.getMyPlane().getPlaneImgWidth()-9)/2)+12,
                        (int)gameView.getMyPlane().getPlane_y()-12,
                        BitmapFactory.decodeResource(gameView.getResources(),R.drawable.bullet1),
                        9,21,50);
                //把子弹创建好后添加到子弹的集合中
                gameView.getMyPlaneBulletList().add(myPlaneBullet);
            }
            //判断是否到达了边界值
            if (bulletCreateTime>1000){
                bulletCreateTime=0;
            }
        }

    }

    //创建敌机
    private void createEmenyPlanes() {
        //当时间为一秒时  相当于计数器的值为20
        if(createEnemyPlaneTime==20){
            //创建小飞机
            EnemyPlane enemyPlane = new EnemyPlane((int)(Math.random()*(MainActivity.screenWidth-51)),
                    -39, BitmapFactory.decodeResource(gameView.getResources(), R.drawable.enemy0),
                    51,39,4,4,20,1,10,10);
            //把创建的飞机添加到集合中
            gameView.getEnemyPlaneList().add(enemyPlane);
            //记录敌人小飞机的个数
            flyNum++;
            //当产生8个小飞机的时候，产生一个中飞机
            if(flyNum%5==0){
                EnemyPlane enemyPlane1 = new EnemyPlane((int)(Math.random()*(MainActivity.screenWidth-69)),
                        -89, BitmapFactory.decodeResource(gameView.getResources(), R.drawable.enemy1),
                        69,89,8,8,20,2,6,20);
                //把创建的飞机添加到集合中
                EnemyPlane enemyPlane3 = new EnemyPlane((int)(Math.random()*(MainActivity.screenWidth-171)),
                        -89, BitmapFactory.decodeResource(gameView.getResources(), R.drawable.fly),
                        171,171,8,8,20,4,6,20);
                EnemyPlane enemyPlane4 = new EnemyPlane((int)(Math.random()*(MainActivity.screenWidth-196)),
                        -89, BitmapFactory.decodeResource(gameView.getResources(), R.drawable.enemy4),
                        196,133,8,8,20,4,6,20);
                gameView.getEnemyPlaneList().add(enemyPlane1);
                gameView.getEnemyPlaneList().add(enemyPlane3);
                gameView.getEnemyPlaneList().add(enemyPlane4);
            }
            //当产生12个小飞机时，产生一个大飞机
            if(flyNum%10==0){
                EnemyPlane enemyPlane2 = new EnemyPlane((int)(Math.random()*(MainActivity.screenWidth-165)),
                        -246, BitmapFactory.decodeResource(gameView.getResources(), R.drawable.enemy2),
                        165,246,16,16,20,3,3,50);
                //把创建的飞机添加到集合中
                gameView.getEnemyPlaneList().add(enemyPlane2);
                //重置小飞机的飞机个数
                flyNum=0;
            }
            if(flyNum%10==0){
                EnemyPlane enemyPlane2 = new EnemyPlane((int)(Math.random()*(MainActivity.screenWidth-165)),
                        -246, BitmapFactory.decodeResource(gameView.getResources(), R.drawable.enemy2),
                        165,246,16,16,20,3,3,50);
                //把创建的飞机添加到集合中
                gameView.getEnemyPlaneList().add(enemyPlane2);
                //重置小飞机的飞机个数
                flyNum=0;
            }

            //重置创建飞机的时间
            createEnemyPlaneTime=0;
        }
    }

    //移动敌机
    private void emenyPlaneMove() {

        //遍历集合中的敌机，并调用该敌机对象的移动方法
        List<EnemyPlane> list = gameView.getEnemyPlaneList();
        //当集合有元素的时候再遍历
        if(list.size()>0&&list!=null){
            for (int i=0;i<list.size();i++){
                //取出每个敌机
                EnemyPlane enemyPlane = list.get(i);
                //存活的飞机可以移动
                if (enemyPlane.isIslive()){
                    enemyPlane.planeMove();
                }else {
                    enemyPlane.setDeadTime(enemyPlane.getDeadTime()-2);
                    //如果死亡时间等于0 则让该飞机在集合中删除
                    if (enemyPlane.getDeadTime()==0){
                        //如果飞机为死亡状态的话，则应该将该飞机从集合中删除
                        list.remove(i);
                        i--;
                    }

                }
            }
        }

    }

    //移动背景
    private void moveBg() {

        gameView.setBgImg1_y(gameView.getBgImg1_y()+5);
        gameView.setBgImg2_y(gameView.getBgImg2_y()+5);
        //如果第一个背景图移动到了设备的高度的时候把它的y设置为负的设备的高度
        if (gameView.getBgImg1_y()>= MainActivity.screenHeight){
            gameView.setBgImg1_y(-MainActivity.screenHeight);
        }
        if (gameView.getBgImg2_y()>= MainActivity.screenHeight){
            gameView.setBgImg2_y(-MainActivity.screenHeight);
        }
    }



}
