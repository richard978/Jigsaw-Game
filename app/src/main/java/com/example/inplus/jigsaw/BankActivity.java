package com.example.inplus.jigsaw;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class BankActivity extends AppCompatActivity {

    ImageView exp1;
    ImageView exp2;
    ImageView exp3;
    ImageView exp4;
    ImageView exp5;
    ImageView album;
    Spinner diffSpinner;
    Switch rotSwitch;
    int imgID;
    int difficulty;
    int isRot;
    private SoundPool soundPool;
    private int soundID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent mIntent = new Intent(BankActivity.this, MusicPlayer.class);
        super.onCreate(savedInstanceState);
        mIntent.putExtra("id",0);
        startService(mIntent);
        setContentView(R.layout.activity_bank);

        ImageView backbtn=(ImageView)findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                playSound();
                // TODO Auto-generated method stub
                Intent mIntent = new Intent(BankActivity.this, MusicPlayer.class);
                stopService(mIntent);
                finish();
            }
        });

        //“旧游戏”的图片获取及添加跳转响应事件
        ImageView Game=(ImageView)findViewById(R.id.beginGame);
        Game.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                playSound();
                Intent intent =new Intent(BankActivity.this,GameActivity.class);
                intent.putExtra("imgID",imgID);
                intent.putExtra("difficulty",difficulty);
                intent.putExtra("isRot",isRot);
                Intent mIntent = new Intent(BankActivity.this, MusicPlayer.class);
                stopService(mIntent);
                startActivity(intent);
            }
        });
        initSound();
        initBtn();
    }

    //图片点击相应事件
    private void initBtn() {
        imgID = 0;
        difficulty = 2;
        isRot = 0;
        exp1 = (ImageView) findViewById(R.id.exp1);
        exp2 = (ImageView) findViewById(R.id.exp2);
        exp3 = (ImageView) findViewById(R.id.exp3);
        exp4 = (ImageView) findViewById(R.id.exp4);
        exp5 = (ImageView) findViewById(R.id.exp5);
        album = (ImageView) findViewById(R.id.Album);
        diffSpinner = (Spinner)findViewById(R.id.spinner1);
        rotSwitch = (Switch)findViewById(R.id.switch1);

        exp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                imgID = 0;
                revBtnStyle();
                exp1.setPadding(8,8,8,8);
                exp1.setBackgroundColor(Color.RED);
            }
        });
        exp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                imgID = 1;
                revBtnStyle();
                exp2.setPadding(8,8,8,8);
                exp2.setBackgroundColor(Color.RED);
            }
        });
        exp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                imgID = 2;
                revBtnStyle();
                exp3.setPadding(8,8,8,8);
                exp3.setBackgroundColor(Color.RED);
            }
        });
        exp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                imgID = 3;
                revBtnStyle();
                exp4.setPadding(8,8,8,8);
                exp4.setBackgroundColor(Color.RED);
            }
        });
        exp5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                imgID = 4;
                revBtnStyle();
                exp5.setPadding(8,8,8,8);
                exp5.setBackgroundColor(Color.RED);
            }
        });
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                imgID = 5;
                revBtnStyle();
                Intent intent =new Intent(BankActivity.this,GameActivity.class);
                intent.putExtra("imgID",imgID);
                intent.putExtra("difficulty",difficulty);
                intent.putExtra("isRot",isRot);
                Intent mIntent = new Intent(BankActivity.this, MusicPlayer.class);
                stopService(mIntent);
                startActivity(intent);
                imgID = 0;
            }
        });
        diffSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                difficulty = i+2;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        rotSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                playSound();
                if(rotSwitch.isChecked())
                    isRot = 1;
                else
                    isRot = 0;
            }
        });
    }

    //恢复按钮初始样式
    private void revBtnStyle(){
        exp1.setPadding(0,0,0,0);
        exp2.setPadding(0,0,0,0);
        exp3.setPadding(0,0,0,0);
        exp4.setPadding(0,0,0,0);
        exp5.setPadding(0,0,0,0);
    }

    @Override
    protected void onStop(){
        Intent mIntent = new Intent(BankActivity.this, MusicPlayer.class);
        //stopService(mIntent);
        super.onStop();
    }
    private void initSound() {
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundID = soundPool.load(this, R.raw.click2, 1);
    }
    private void playSound() {
        soundPool.play(soundID, 1f, 1f, 0,0,1);
    }
}
