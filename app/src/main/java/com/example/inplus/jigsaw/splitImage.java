package com.example.inplus.jigsaw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class splitImage {

    public static List<ImagePiece> split(Bitmap bitmap, int xPiece, int yPiece) {

        List<ImagePiece> pieces = new ArrayList<ImagePiece>(xPiece * yPiece);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap midBitmap = null;
        if(width>=height) {
            midBitmap = Bitmap.createBitmap(bitmap, width / 2 - height / 2, 0, height, height);
            width = height;
        }
        else {
            midBitmap = Bitmap.createBitmap(bitmap, 0, height / 2 - width / 2, width, width);
            height = width;
        }

        int pieceWidth = width / xPiece;
        int pieceHeight = height / yPiece;
        for (int i = 0; i < yPiece; i++) {
            for (int j = 0; j < xPiece; j++) {
                ImagePiece piece = new ImagePiece();
                piece.index = j + i * xPiece;
                int xValue = j * pieceWidth;
                int yValue = i * pieceHeight;
                piece.bitmap = Bitmap.createBitmap(midBitmap, xValue, yValue,
                        pieceWidth, pieceHeight);
                pieces.add(piece);
            }
        }
        return pieces;
    }
    public static Bitmap getTriangleBitmap(Bitmap bitmap, int type){
        Paint paint = new Paint();
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Path path = new Path();
        if(type==0) {
            path.moveTo(10, 10);
            path.lineTo(10, bitmap.getHeight() - 10);
            path.lineTo(bitmap.getWidth() - 10, 10);
            path.lineTo(10, 10);
            path.close();
        }
        else{
            path.moveTo(10, bitmap.getHeight() - 10);
            path.lineTo(bitmap.getWidth() - 10, bitmap.getHeight() - 10);
            path.lineTo(bitmap.getWidth() - 10, 10);
            path.lineTo(10, bitmap.getHeight() - 10);
            path.close();
        }
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap addFrame(Bitmap bitmap, int type) {
        Paint paint = new Paint();
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Path path = new Path();
        if(type==0) {
            path.moveTo(0, 0);
            path.lineTo(0, bitmap.getHeight());
            path.lineTo(bitmap.getWidth(), 0);
            path.lineTo(0, 0);
            path.close();
        }
        else{
            path.moveTo(0, bitmap.getHeight());
            path.lineTo(bitmap.getWidth(), bitmap.getHeight());
            path.lineTo(bitmap.getWidth(), 0);
            path.lineTo(0, bitmap.getHeight());
            path.close();
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(15);

        canvas.drawPath(path, paint);
        canvas.drawBitmap(bitmap, rect, rect, null);

        return output;
    }
}

class ImagePiece {

    public int index = 0;
    public Bitmap bitmap = null;

}