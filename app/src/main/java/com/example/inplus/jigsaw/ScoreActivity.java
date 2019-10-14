package com.example.inplus.jigsaw;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {
    //存储本地记录，返回值表示当前成绩是否是最好成绩
    static public boolean saveRecord(int difficulty, int isRot, String user, Context context, int recordNum, String timeCost, String date){
        boolean isBest = false;

        SharedPreferences prefs = context.getSharedPreferences(user, MODE_PRIVATE);
        Item records[] = readRecord(difficulty, isRot, user, context, recordNum);
        if(timeCost.compareTo(records[records.length - 1].timeCost) < 0 || records[records.length - 1].timeCost == ""){
            records[records.length - 1] = new Item("", timeCost, date);
            for(int i = records.length - 1; i > 0; --i)
                if(records[i].timeCost.compareTo(records[i - 1].timeCost) < 0 || records[i - 1].timeCost == ""){
                    Item tmp = records[i];
                    records[i] = records[i - 1];
                    records[i - 1] = tmp;
                    if(i == 1)isBest = true;
                }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < records.length; ++i){
            stringBuilder.append(records[i].timeCost);
            stringBuilder.append("#");
            stringBuilder.append(records[i].date);
            stringBuilder.append("#");
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("record" + (difficulty * 2 + isRot), stringBuilder.toString());
        editor.apply();
        return isBest;
    }

    static public Item[] readRecord(int difficulty, int isRot, String user, Context context, int recordNum){
        SharedPreferences prefs = context.getSharedPreferences(user, MODE_PRIVATE);
        Item records[] = new Item[recordNum];
        for(int i = 0; i < recordNum; ++i)
            records[i] = new Item("", "", "");
        String resultStr[] = prefs.getString("record" + (difficulty * 2 + isRot), "").split("#");
        for(int i = 0; i < resultStr.length; ++i) {
            if((i & 1) == 0)
                records[i >> 1].timeCost = resultStr[i];
            else
                records[i >> 1].date = resultStr[i];
        }
        return records;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        ImageView backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent =new Intent(ScoreActivity.this,MainFrameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //获取当前用户名
        UserMessage userMessage = (UserMessage) getApplication();
        String user = userMessage.User;
        //获取结果并展示
        Intent intent = getIntent();
        String timeCost = intent.getStringExtra("timeCost");
        String date = intent.getStringExtra("date");
        int iDifficulty = (intent.getIntExtra("difficulty", 2) - 2);
        int wyDifficulty = iDifficulty+1;
        int iIsRot = intent.getIntExtra("isRot", 0);
        if(saveRecord(iDifficulty, iIsRot, user, this, 10, timeCost, date))
            findViewById(R.id.bestScore).setVisibility(TextView.VISIBLE);
//        Toast.makeText(this, date + " " + timeCost + " ", Toast.LENGTH_LONG).show();
        TextView difficulty = findViewById(R.id.difficulty);
        difficulty.setText("难度" + wyDifficulty);
        TextView isRot = findViewById(R.id.isRot);
        isRot.setText(iIsRot == 1 ? "是" : "否");
        TextView score = findViewById(R.id.score);
        score.setText(timeCost);
        TextView btnRankList = findViewById(R.id.btnRankList);
        btnRankList.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent =new Intent(ScoreActivity.this,RankActivity.class);
                startActivity(intent);
            }
        });
    }
}
