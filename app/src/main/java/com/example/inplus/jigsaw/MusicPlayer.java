package com.example.inplus.jigsaw;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.Gravity;
import android.widget.Toast;

public class MusicPlayer extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        int flag = intent.getIntExtra("id",0);
        mediaPlayer = new MediaPlayer();
        if(flag==0)
            mediaPlayer = MediaPlayer.create(this, R.raw.bg1);
        else
            mediaPlayer = MediaPlayer.create(this, R.raw.bg2);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
