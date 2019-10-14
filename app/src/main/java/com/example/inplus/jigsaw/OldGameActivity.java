package com.example.inplus.jigsaw;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class OldGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_game);

        ImageView backbtn=(ImageView)findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        Button oldgame = (Button)findViewById(R.id.bt_oldgame);
        if (SaveData.saved == true) {
            oldgame.setVisibility(View.VISIBLE);
        }

        oldgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(OldGameActivity.this, SaveGameActivity.class);
                startActivity(intent);
            }
        });
    }
}
