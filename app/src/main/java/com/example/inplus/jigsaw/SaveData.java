package com.example.inplus.jigsaw;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by wangzhenan on 2018/7/5.
 */

public class SaveData {
    public static int difficulty_save;    //难度
    public static int isRot_save;         //是否可旋转
    public static int reCode_save;     //用于图库读取的活动ID
    public static ImageView[] images_save;    //图片序列
    public static boolean[] rotated_save;     //每个图片是否旋转
    public static int[] rarray_save;      //图片序列
    public static long recordTime_save;   //游戏时间
    public static boolean saved = false;    //是否有存档
    public static Bitmap image_save;
}
