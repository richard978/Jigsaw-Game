package com.example.inplus.jigsaw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity {
    public RecordAdapter recordAdapter;
    public List<Item> list;

    //更新列表
    private void reFlashList(String tab, boolean which, boolean isRot){
        list.clear();
        //读取用户名
        UserMessage userMessage = (UserMessage) getApplication();
        String user = userMessage.User;
        int difficulty;
        switch (tab){
            case "level1":difficulty = 0;break;
            case "level2":difficulty = 1;break;
            case "level3":difficulty = 2;break;
            default:difficulty = -1;
        }
        //从服务器读取全体排行
        if(which){

        }
        //读取本地的个人排行
        else{
            Item records[] = ScoreActivity.readRecord(difficulty, isRot ? 1 : 0, user, this, 10);
            for(int i = 0; i < records.length; ++i)
                if(records[i].timeCost == "")
                    list.add(new Item("", records[i].timeCost, records[i].date));
                else
                    list.add(new Item(user, records[i].timeCost, records[i].date));
        }
//        list.add(new Item("wy54224", "59'59\"999", tab + " " + (which ? "全体" : "个人") + " " + (isRot ? " 旋转" : "不旋转")));
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        ImageView backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        ListView listView = findViewById(R.id.rankList);
        list = new ArrayList<>();
        recordAdapter = new RecordAdapter(this, R.layout.record, list);
        listView.setAdapter(recordAdapter);

        CheckBox isRot = findViewById(R.id.isRot);
        isRot.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck){
                Switch s = findViewById(R.id.rankListSwitch);
                FragmentTabHost mTabHost = findViewById(android.R.id.tabhost);
                reFlashList(mTabHost.getCurrentTabTag(), s.isChecked(), isCheck);
                recordAdapter.notifyDataSetChanged();
            }
        });

        Switch s = findViewById(R.id.rankListSwitch);
        s.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                FragmentTabHost mTabHost = findViewById(android.R.id.tabhost);
                CheckBox isRot = findViewById(R.id.isRot);
                reFlashList(mTabHost.getCurrentTabTag(), isCheck, isRot.isChecked());
                recordAdapter.notifyDataSetChanged();
            }
        });

        FragmentTabHost mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.contentLayout);
        mTabHost.setOnTabChangedListener(new FragmentTabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tab){
                Switch s = findViewById(R.id.rankListSwitch);
                CheckBox isRot = findViewById(R.id.isRot);
                reFlashList(tab, s.isChecked(), isRot.isChecked());
                recordAdapter.notifyDataSetChanged();
            }
        });
        mTabHost.addTab(mTabHost.newTabSpec("level1").setIndicator("等级1"), RankListActivity.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("level2").setIndicator("等级2"), RankListActivity.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("level3").setIndicator("等级3"), RankListActivity.class, null);
    }
}
