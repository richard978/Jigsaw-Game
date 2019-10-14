package com.example.inplus.jigsaw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{
    RelativeLayout layout1;
    List<Integer> imageSet;
    Bitmap image;
    int difficulty;
    int reCode;     //用于图库读取的活动ID
    int isRot;
    List<ImagePiece> ip;
    ImageView mFirst,mSecond;
    boolean isAniming;
    ImageView[] images;
    int[] rArray,iArray;
    int mPadding;
    int ItemWidth;
    int id1,id2;
    boolean[]rotated;
    float x1,x2,y1,y2;
    long recordTime = 0;//记录运行用的时间，用于chronometer的正确显示，单位：毫秒
    //使用Handler更新时间显示的TextView，每隔40+random()%20毫秒更新一次上面显示的时间，经过的时间使用Chronometer记录
    Handler handler = new Handler();
    private SoundPool soundPool,soundPool2;
    private int soundID,soundID2;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ((TextView)findViewById(R.id.time)).setText(typeChange(SystemClock.elapsedRealtime() - ((Chronometer)findViewById(R.id.timer)).getBase()));
            if((int)(findViewById(R.id.stop).getTag()) == 0)
                handler.postDelayed(this, (new Random()).nextInt() % 20 + 40);
        }
    };

    //格式转换，将经过的毫秒转化成 分钟'秒"毫秒 的格式
    static public String typeChange(long milliseconds){
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        if(minutes > 59){
            minutes = 59;
            seconds = 59;
            milliseconds = 999;
        }else{
            seconds %= 60;
            milliseconds %= 1000;
        }
        return String.format("%02d'%02d\"%03d", minutes, seconds, milliseconds);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent mIntent = new Intent(GameActivity.this, MusicPlayer.class);
        super.onCreate(savedInstanceState);
        mIntent.putExtra("id",1);
        startService(mIntent);

        setContentView(R.layout.activity_game);
        ImageView backbtn=(ImageView)findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent mIntent = new Intent(GameActivity.this, MusicPlayer.class);
                stopService(mIntent);
                mIntent = new Intent(GameActivity.this, MusicPlayer.class);
                mIntent.putExtra("id",0);
                startService(mIntent);
                finish();
            }
        });
        ((TextView)findViewById(R.id.time)).setText("00'00\"000");
        ((Chronometer)findViewById(R.id.timer)).start();
        handler.postDelayed(runnable, (new Random()).nextInt() % 20 + 40);
        ImageView stop = findViewById(R.id.stop);
        stop.setTag(0);
        stop.setOnClickListener(new View.OnClickListener() {
            boolean noAnyAniming = false;//记录是不是正确的暂停（暂停的时候不能执行动画，否则暂停失败）
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ImageView stop = (ImageView)v;
                Chronometer timer = findViewById(R.id.timer);
                if((int)(stop.getTag()) == 0){
                    if(!isAniming){
                        isAniming = true;
                        noAnyAniming = true;
                        timer.stop();
                        handler.removeCallbacks(runnable);
                        recordTime = SystemClock.elapsedRealtime() - timer.getBase();
                        stop.setImageResource(R.drawable.start);
                        stop.setTag(1);
                    }
                }else{
                    if(noAnyAniming){
                        stop.setTag(0);
                        stop.setImageResource(R.drawable.stop);
                        timer.setBase(SystemClock.elapsedRealtime() - recordTime);
                        handler.postDelayed(runnable, (new Random()).nextInt() % 20 + 40);
                        timer.start();
                        isAniming = false;
                        noAnyAniming = false;
                    }
                }
            }

        });

        ImageView saveView = (ImageView)findViewById(R.id.save);
        saveView.setOnClickListener(new View.OnClickListener() {
            boolean noAnyAniming = false;//记录是不是正确的暂停（暂停的时候不能执行动画，否则暂停失败）
            @Override
            public void onClick(View v) {
                SaveData.difficulty_save = difficulty;
                SaveData.images_save = images;
                SaveData.isRot_save = isRot;
                SaveData.rarray_save = rArray;
                Chronometer timer = findViewById(R.id.timer);
                SaveData.recordTime_save = SystemClock.elapsedRealtime() - timer.getBase();
                SaveData.rotated_save = rotated;
                SaveData.saved = true;
                SaveData.reCode_save = reCode;
                SaveData.image_save = image;
                Intent mIntent = new Intent(GameActivity.this, MusicPlayer.class);
                stopService(mIntent);
                Intent intent =new Intent(GameActivity.this,MainFrameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        difficulty = 2;
        reCode = 1;
        isRot = 0;
        mPadding = 2;
        Intent intent = getIntent();
        int imgID = intent.getIntExtra("imgID",0);
        difficulty = intent.getIntExtra("difficulty",0);
        isRot = intent.getIntExtra("isRot",0);
        initImage(imgID);
        initSound();
    }

    //初始化图片显示
    private void initImage(int id){
        imageSet = new ArrayList<>();
        imageSet.add(R.drawable.exp1);imageSet.add(R.drawable.exp2);imageSet.add(R.drawable.exp3);imageSet.add(R.drawable.exp4);imageSet.add(R.drawable.exp5);
        layout1 = (RelativeLayout) findViewById(R.id.jigsaw);
        image = BitmapFactory.decodeResource(getResources(), imageSet.get(0));
        if(id < 5) {
            image = BitmapFactory.decodeResource(getResources(), imageSet.get(id));
            setImage(difficulty);
        }
        else
            chooseFromGallery();
    }

    //进行图片切割，分成size*size份，每份单独一个动态imageView
    private void setImage(int size){
        ip = splitImage.split(image, size, size);//分割成size*size份
        images = new ImageView[size*size];
        rotated = new boolean[size*size];
        ItemWidth = 720/size;
        rArray = randomArray(0,size*size-1,size*size);//产生大小在size*size-1和0之间的size*size个随机数来分配
        iArray = new int[size * size];
        for(int i = 0; i < size * size; i++){
            int k = rArray[i];
            iArray[i] = k;
        }

        for(int i = 0; i < size*size; i++) {
            ImageView iv = new ImageView(this);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ItemWidth,ItemWidth);
            //设置图块位置
            //iv.setImageBitmap(ip.get(i).bitmap);       //按顺序获得后加入images
            iv.setImageBitmap(ip.get(rArray[i]).bitmap);     //随机设置加入图中
            iv.setOnClickListener(this);                //监听函数
            images[i] = iv;
            iv.setId(i+1);
            iv.setBackgroundColor(Color.YELLOW);
            iv.setPadding(2,2,2,2);
            Random rand = new Random();
            int t = rand.nextInt(2);
            rotated[i] = false;
            if(isRot==1 && t == 1){
                iv.setRotation(180);       //随机旋转图块
                rotated[i] = true;
            }
//            // 设置横向边距,不是最后一列
//            if ((i + 1) % difficulty != 0) {
//                //p.rightMargin = 2;
//            }
            // 如果不是第一列
            if (i % difficulty != 0) {
                p.addRule(RelativeLayout.RIGHT_OF, images[i - 1].getId());
            }
            // 如果不是第一行，//设置纵向边距，非最后一行
            if ((i + 1) > difficulty) {
                //p.topMargin = 2;
                p.addRule(RelativeLayout.BELOW,images[i - difficulty].getId());
            }
            layout1.addView(iv,p);
        }
    }

    //生成随机数列（用于设置图块位置）
    private static int[] randomArray(int min,int max,int n){
        int len = max-min+1;
        if(max < min || n > len){
            return null;
        }
        int[] source = new int[len];
        for (int i = min; i < min+len; i++){
            source[i-min] = i;
        }
        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            index = Math.abs(rd.nextInt() % len--);
            result[i] = source[index];
            source[index] = source[len];
        }
        return result;
    }

    //从图库选择图片
    private void chooseFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2){
            if (data == null){
                return;
            }else{
                Uri uri;
                uri = data.getData();
                convertUri(uri);
            }
        }
    }
    private void convertUri(Uri uri){
        InputStream is;
        try {
            is = getContentResolver().openInputStream(uri);
            image = BitmapFactory.decodeStream(is);
            if(image!=null)
                setImage(difficulty);
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //点击图片块事件
    public void onClick(View view){
        Log.d("TAG", "onClick: "+view.getTag());
        //Toast.makeText(GameActivity.this,"Click!",Toast.LENGTH_SHORT).show();
        // 如果正在执行动画，则屏蔽
        if(isAniming)
            return;
        //如果两次点击的是同一个View
        if(mFirst == view){
            if(isRot == 1){
                int x = view.getId();
                if(rotated[x - 1]){
                    rotated[x - 1] = false;
                    rotateView(view,180,360);
                    //Toast.makeText(GameActivity.this, view.getX()+" "+view.getY(), Toast.LENGTH_SHORT).show();
                    view.setRotation(0);
                    checkSuccess();
                }
                else if(!rotated[x - 1]){
                    rotated[x - 1] = true;
                    rotateView(view,180,360);
                    //Toast.makeText(GameActivity.this, view.getX()+" "+view.getY(), Toast.LENGTH_SHORT).show();
                    view.setRotation(180);
                    checkSuccess();
                }
            }
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }
        //点击第一个View
        if(mFirst==null){
            mFirst= (ImageView) view;
            id1 = view.getId();
            mFirst.setColorFilter(Color.parseColor("#55FF0000"));
            x1 = mFirst.getX();
            y1 = mFirst.getY();
            playSound();
        }
        //点击第二个View
        else{
            mSecond= (ImageView) view;
            id2 = view.getId();
            x2 = mSecond.getX();
            y2 = mSecond.getY();
            //Toast.makeText(GameActivity.this, x1+" "+y1+"\n"+x2+" "+y2+"\n"+id1+" "+id2, Toast.LENGTH_SHORT).show();
//            String te ="";
//            for(int j = 0; j <  difficulty * difficulty; j ++){
//                te += rotated[j] + " ";
//            }
//            Toast.makeText(GameActivity.this, te, Toast.LENGTH_SHORT).show();
            exchangeView();
        }
    }

    @SuppressLint("ResourceType")
    private  void rotateView(View view, float start, float end){
        int a = 0,b = 0;
        for(int i = 0; i < difficulty * difficulty; i++){
            if(iArray[i] == rArray[(view.getId() - 1)]){//
                a = i;
            }
            if(i == (view.getId()-1)){//获取view起始位置下标和view当前位置下标
               //a = iArray[i];
                b = i;
            }
        }
        playSound2();
        int ax,ay,bx,by;
        ax = a%difficulty;
        ay = a/difficulty;
        bx = b%difficulty;
        by = b/difficulty;
        //Toast.makeText(GameActivity.this, a+" "+b, Toast.LENGTH_SHORT).show();
        RotateAnimation anim;
        //anim = new RotateAnimation(start,end);
        //if(b>=a){//休息一下
            //int m = (b-a)/difficulty;
            //int n = (b-a)%difficulty;
            //anim = new RotateAnimation(start,end,RotateAnimation.ABSOLUTE,((b-a)%difficulty)*720/difficulty+360/difficulty,RotateAnimation.ABSOLUTE,(b-a)*720/(difficulty*difficulty)+360/difficulty);
        anim = new RotateAnimation(start,end,(bx-ax)*720/difficulty+360/difficulty,(by-ay)*720/difficulty+360/difficulty);
       // }
        //else{
            //anim = new RotateAnimation(start,end,RotateAnimation.ABSOLUTE,-((a-b)%difficulty)*720/difficulty+360/difficulty,RotateAnimation.ABSOLUTE,-((a-b)/difficulty)*720/difficulty+360/difficulty);
            //anim = new RotateAnimation(start,end,-((a-b)%difficulty)*720/difficulty+360/difficulty,-((a-b)*720/(difficulty*difficulty))+360/difficulty);
       // }

        anim.setDuration(500);
        view.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isAniming = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAniming = false;
            }
        });
    }
    //交换图片位置/动画
    private void exchangeView(){
        mFirst.setColorFilter(null);

        // 设置动画
        TranslateAnimation anim = new TranslateAnimation(0, x2 - x1, 0, y2-y1);
        anim.setDuration(300);
        anim.setFillAfter(true);
        mFirst.startAnimation(anim);

        TranslateAnimation animSecond = new TranslateAnimation(0, x1 - x2, 0, y1 - y2);
        animSecond.setDuration(300);
        animSecond.setFillAfter(true);
        mSecond.startAnimation(animSecond);

        //交换rArray中对应位置和图片id
        int temp = rArray[id1 - 1];
        rArray[id1 - 1] = rArray[id2 - 1];
        rArray[id2 - 1] = temp;
        boolean temp2 = rotated[id1 - 1];
        rotated[id1 - 1] = rotated[id2 - 1];
        rotated[id2 - 1] = temp2;
        mFirst.setId(id2);
        mSecond.setId(id1);
         //添加动画监听
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isAniming = true;
                mFirst.setVisibility(INVISIBLE);
                mSecond.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFirst.setX(x2);
                mFirst.setY(y2);
                mSecond.setX(x1);
                mSecond.setY(y1);
                mSecond.clearAnimation();
                mFirst.clearAnimation();
                mFirst.setVisibility(VISIBLE);
                mSecond.setVisibility(VISIBLE);
                mFirst = mSecond = null;
                //mAnimLayout.removeAllViews();
                isAniming = false;
                //进行游戏胜利判断
                checkSuccess();
            }
        });
    }

     //* 用来判断游戏是否成功
    private void checkSuccess() {
        boolean isSuccess = true;
        for (int i = 0; i < difficulty * difficulty; i++) {
            if(isRot == 1){
                if(rArray[i] != i || rotated[i])
                    isSuccess = false;
            }
            if(rArray[i] != i){
                isSuccess = false;
            }
        }
        if (isSuccess) {
//            Toast.makeText(this, "Success!",
//                    Toast.LENGTH_LONG).show();
            Chronometer timer = findViewById(R.id.timer);
            timer.stop();
            handler.removeCallbacks(runnable);
            findViewById(R.id.stop).setTag(1);
            Intent intent =new Intent(this,ScoreActivity.class);
            //将用时和游戏结束的时刻发送过去
            intent.putExtra("timeCost",typeChange(SystemClock.elapsedRealtime() - timer.getBase()));
            intent.putExtra("date", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
            intent.putExtra("difficulty", difficulty);
            intent.putExtra("isRot", isRot);
            Intent mIntent = new Intent(GameActivity.this, MusicPlayer.class);
            stopService(mIntent);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onStop(){
        Intent mIntent = new Intent(GameActivity.this, MusicPlayer.class);
        //stopService(mIntent);
        super.onStop();
    }
    public void initSound() {
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundID = soundPool.load(this, R.raw.click3, 1);
        soundPool2 = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundID2 = soundPool.load(this, R.raw.rotate1, 1);
    }
    private void playSound() {
        soundPool.play(soundID, 1f, 1f, 0,0,1);
    }
    private void playSound2() {
        soundPool2.play(soundID2, 1f, 1f, 0,0,1);
    }
}
