package com.example.space_shooter.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import com.example.space_shooter.Content;
import com.example.space_shooter.R;
import java.util.ArrayList;

public class GameDrawThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private boolean firstTimeOpen = true;
    private Paint paint = new Paint();
    private Context contextGlobal;
    private volatile boolean running = true;
    private Paint backgroundPaint = new Paint();
    ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    ArrayList<Bullet> activeBullets = new ArrayList<Bullet>();
    ArrayList<Bitmap> explosionsList = new ArrayList<Bitmap>();
    private Bitmap enemy, player, space, bullet;
    private Bitmap explosionImg;
    private long bulletTimer, bulletInterval1 = 1500, bulletInterval2 = 1000, bulletInterval3 = 500;
    private ArrayList<Explosion> exps = new ArrayList<>();
    {
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(50.0f);
    }

    public GameDrawThread(Context context, SurfaceHolder surfaceHolder) {
        player = BitmapFactory.decodeResource(context.getResources(), getResId("player", String.valueOf(Content.player.playerImg), "drawable",context));
        enemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        space = BitmapFactory.decodeResource(context.getResources(), R.drawable.space3);
        enemy = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        bullet = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        explosionImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);
        contextGlobal = context;

        for (int i = 0; i < 17; i++){
            explosionsList.add(BitmapFactory.decodeResource(context.getResources(), getResId("e", String.valueOf(i), "drawable",context)));
        }

        if (firstTimeOpen) {
            for (int i = 0; i <= 10; i++) {
                Player p = Content.player;
                float rx, ry;
                do {
                    rx = (float) ((Math.random() * ((2046 + 2046) + 1)) - 2046);
                    ry = (float) ((Math.random() * ((1616 + 1616) + 1)) - 1616);
                } while (Math.sqrt(Math.pow(rx - 300, 2) + Math.pow(ry - 600, 2)) < 500);
                Enemy enemy = new Enemy(rx, ry, (Math.random() * 361));
                enemyList.add(enemy);
            }
            firstTimeOpen = false;
        }
        this.surfaceHolder = surfaceHolder;
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
                    ArrayList<Enemy> delEnemies = new ArrayList<>();
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
                    canvas.drawBitmap(space, Content.player.x, Content.player.y, backgroundPaint);

                    for (int i = 0; i < enemyList.size(); i++) {
                        Enemy e = enemyList.get(i);

                        float d = (float)(Math.sqrt(Math.pow(e.x - 300f, 2) + Math.pow(e.y - 600f, 2)));

                        if (d > 500) {
                            e.vx = e.normalSpeed * (300f - e.x) / d - Content.player.vx;
                            e.vy = e.normalSpeed * (600f - e.y) / d - Content.player.vy;
                        } else {
                            e.vx = 0 - Content.player.vx;
                            e.vy = 0 - Content.player.vy;

                        }

                        if (e.angle - 90 >= 0) {
                            e.angle = (int) Math.toDegrees(Math.asin(Math.abs((double) e.y - 600) / (double) d))-90;
                        } else {
                            e.angle = (int) Math.toDegrees(Math.asin(Math.abs((double) e.y - 600) / (double) d))+270;
                        }

                        enemyList.set(i, e);
                    }

                    for (int i = 0; i < enemyList.size(); i++){
                        Enemy e = enemyList.get(i);

                        e.x += e.vx;
                        e.y += e.vy;
                        canvas.drawBitmap(e.rotate(enemy, e.angle), e.x, e.y, backgroundPaint);

                        enemyList.set(i, e);
                    }

                   if (Content.player.shootMode > 0 && !Content.player.canShoot) {
                       if (Content.player.shootMode == 1 && System.currentTimeMillis() - this.bulletTimer > this.bulletInterval1 && !Content.player.canShoot) {
                           this.bulletTimer = System.currentTimeMillis();
                           Bullet bullet = new Bullet(500, Content.player.angle, 300f, 600f);
                           activeBullets.add(bullet);
                       }
                       if (Content.player.shootMode == 2 && System.currentTimeMillis() - this.bulletTimer > this.bulletInterval2 && !Content.player.canShoot) {
                           this.bulletTimer = System.currentTimeMillis();
                           Bullet bullet = new Bullet(250, Content.player.angle, 300f, 600f);
                           activeBullets.add(bullet);
                       }
                       if (Content.player.shootMode == 3 && System.currentTimeMillis() - this.bulletTimer > this.bulletInterval3 && !Content.player.canShoot) {
                           this.bulletTimer = System.currentTimeMillis();
                           Bullet bullet = new Bullet(100, Content.player.angle, 300f, 600f);
                           activeBullets.add(bullet);
                       }
                   }

                   ArrayList<Bullet> delBullets = new ArrayList<>();
                   for (int i = 0; i < activeBullets.size(); i++){
                        Bullet b = activeBullets.get(i);

                        b.x += b.vx;
                        b.y += b.vy;

                        if (System.currentTimeMillis() - b.createTime > 5000) {
                            delBullets.add(b);
                        }

                        canvas.drawBitmap(b.rotate(bullet, b.angle), b.x, b.y, backgroundPaint);
                        activeBullets.set(i, b);
                   }

                   for (int i = 0; i < enemyList.size(); i++) {
                        Enemy e = enemyList.get(i);
                        for (Bullet b : activeBullets) {
                            if (Math.abs(e.x - b.x) < 150 && Math.abs(e.y - b.y) < 150) {
                                exps.add(new Explosion(e.x, e.y, System.currentTimeMillis()));
                                delEnemies.add(e);
                                delBullets.add(b);
                            }
                        }

                        for (int j = 0; j < enemyList.size(); j++) {
                            if (i == j) { continue; }
                            Enemy enemyThis = enemyList.get(i);
                            Enemy enemyAnother = enemyList.get(j);

                            float d = (float)(Math.sqrt(Math.pow(enemyThis.x - enemyAnother.x, 2) + Math.pow(enemyThis.y - enemyAnother.y, 2)));
                            if (d < 250) {
                                exps.add(new Explosion(enemyThis.x, enemyThis.y, System.currentTimeMillis()));
                                delEnemies.add(enemyThis);
                                delEnemies.add(enemyAnother);
                            }
                        }
                    }

                    ArrayList<Explosion> delExps = new ArrayList<>();
                    for (int i = 0; i < exps.size(); i++) {
                        Explosion e = exps.get(i);
                        if (e.update()) {
                            delExps.add(e);
                            continue;
                        }
                        exps.set(i, e);
                        canvas.drawBitmap(explosionsList.get(e.explosionState), e.eX, e.eY, backgroundPaint);
                    }

                    for (Explosion e : delExps) {
                        exps.remove(e);
                    }

                    for (Enemy e : delEnemies) {
                        enemyList.remove(e);
                    }

                    for (Bullet b : delBullets) {
                        activeBullets.remove(b);
                    }

                    Content.player.x -= Content.player.vx;
                    Content.player.y -= Content.player.vy;
                    canvas.drawBitmap(Content.player.rotate(player, Content.player.angle), 300f, 600f, backgroundPaint);
                } finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private int getResId(String text, String number, String directory, Context context){
        String name = text + number;
        int holderImg = context.getResources().getIdentifier(name, directory,
                context.getPackageName());
        return holderImg;
    }
}
