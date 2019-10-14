package com.example.inplus.jigsaw;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by wangzhenan on 2018/7/4.
 */

public class MainFrameActivity extends AppCompatActivity {
    private SoundPool soundPool;
    private int soundID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSound();
        //设置当前用户名为guest
        UserMessage userMessage = (UserMessage) getApplication();
        userMessage.User = "guest";

        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //“新游戏”的图片获取及添加跳转响应事件
        ImageView newGame=(ImageView)findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                playSound();
                Intent intent =new Intent(MainFrameActivity.this,BankActivity.class);
                startActivity(intent);
            }
        });

        //“旧游戏”的图片获取及添加跳转响应事件
        ImageView oldGame=(ImageView)findViewById(R.id.oldGame);
        oldGame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                playSound();
                Intent intent =new Intent(MainFrameActivity.this,OldGameActivity.class);
                startActivity(intent);
            }
        });

        //“排名”的图片获取及添加跳转响应事件
        ImageView Rank=(ImageView)findViewById(R.id.rank);
        Rank.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                playSound();
                Intent intent =new Intent(MainFrameActivity.this,RankActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initSound() {
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundID = soundPool.load(this, R.raw.click3, 1);
    }
    private void playSound() {
        soundPool.play(soundID, 1f, 1f, 0,0,1);
    }
}
