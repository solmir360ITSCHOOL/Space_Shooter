package com.example.space_shooter.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import com.example.space_shooter.Content;
import com.example.space_shooter.R;

public class DrawThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private Paint pt = new Paint();
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
    private Bitmap bitmap;

    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.space2);
        this.surfaceHolder = surfaceHolder;
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);
        pt.setColor(Color.WHITE);
        pt.setTextSize(50.0f);
    }

    public void requestStop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                try {
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
                    canvas.drawBitmap(bitmap, Content.space.x1, 0, backgroundPaint);
                    canvas.drawBitmap(bitmap, Content.space.x2, 0, backgroundPaint);
                    Content.space.x1++;
                    Content.space.x2++;
                    Content.space.Check();
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}