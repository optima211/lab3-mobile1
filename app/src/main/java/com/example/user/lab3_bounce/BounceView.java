package com.example.user.lab3_bounce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class BounceView extends SurfaceView {

    private long lastClick;
    private List<BounceItem> items = new ArrayList<>();
    private List<Integer> resources = new ArrayList<>();
    private Integer lastUsedResourceIndex;
    private BounceThread bounceThread;

    public BounceView(Context context) {
        super(context);
        resources.add(R.drawable.n);
        resources.add(R.drawable.e);
        resources.add(R.drawable.d);
        resources.add(R.drawable.z);
        resources.add(R.drawable.e);
        resources.add(R.drawable.l);
        resources.add(R.drawable.yu);
        resources.add(R.drawable.k);
        lastUsedResourceIndex = 0;

        try {
            bounceThread = new BounceThread(this);

            getHolder().addCallback(new SurfaceHolder.Callback() {

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    boolean retry = true;
                    bounceThread.setRunning(false);
                    while (retry) {
                        try {
                            bounceThread.join();
                            retry = false;
                        } catch (InterruptedException e) {
                            Log.v(e.getMessage(), "error");
                        }
                    }
                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    bounceThread.setRunning(true);
                    bounceThread.start();
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }
            });

        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        try {
            canvas.drawColor(Color.WHITE);
            @SuppressLint("DrawAllocation") List<BounceItem> bounceItemIdsToRemove = new ArrayList<>();

            canvas.save();

            for (int i = items.size() - 1; i >= 0; i--) {
                BounceItem item = items.get(i);

                int screenH = getHeight() - item.ball.getHeight();
                int screenW = getWidth() - item.ball.getWidth();
                if ((item.Y != screenH) || (item.lastY != screenH)) {
                    item.lastY = item.Y;

                    if (item.Y == screenH) item.speed = -0.9f * item.speed;

                    item.speed = item.speed + item.grav;

                    float part1 = (item.speed * item.accel);
                    float part2 = (item.grav * item.accel * item.accel / 2);
                    item.Y = item.Y - part1 - part2;

                    if (item.Y > screenH) item.Y = screenH;

                    if (item.toRight) {
                        item.X += 2;
                    } else {
                        item.X -= 2;
                    }

                    if (item.X >= screenW) {
                        item.toRight = false;
                        item.X = screenW;
                    }
                    if (item.X <= 0) {
                        item.toRight = true;
                        item.X = 0;
                    }


                    canvas.drawBitmap(item.ball, item.X, item.Y, null);
                } else {
                    if (item.liveCounter >= 15) {
                        bounceItemIdsToRemove.add(item);
                    } else {
                        item.liveCounter++;
                        canvas.drawBitmap(item.ball, item.X, item.Y, null);
                    }

                }
            }

            if (bounceItemIdsToRemove.size() > 0) {
                for (BounceItem item : bounceItemIdsToRemove) {
                    items.remove(item);
                }
            }

            canvas.restore();
            invalidate();
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    private BounceItem createBounceItem(float x, float y, Bitmap ball) {
        BounceItem bounceItem = new BounceItem();
        bounceItem.X = x;
        bounceItem.Y = y;
        bounceItem.lastY = y;
        bounceItem.ball = ball;
        bounceItem.accel = 0.1f;
        bounceItem.grav = -500f * 0.1f;
        bounceItem.speed = 10f;
        bounceItem.toRight = true;
        return bounceItem;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();

            Integer lastUsedResource = resources.get(lastUsedResourceIndex);

            synchronized (getHolder()) {
                Bitmap ball = BitmapFactory.decodeResource(getResources(), lastUsedResource);
                BounceItem bounceItem = createBounceItem(x, y, ball);
                items.add(bounceItem);
            }

            Integer charsCount = resources.size();
            if (lastUsedResourceIndex + 1 == charsCount) {
                lastUsedResourceIndex = 0;
                lastUsedResource = resources.get(lastUsedResourceIndex);
            } else {
                lastUsedResourceIndex++;
                lastUsedResource = resources.get(lastUsedResourceIndex);
            }

        }
        return true;
    }

    public void resume() {
        try {
            if (bounceThread != null) {
                bounceThread.setRunning(true);
            }
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    public void pause() {
        try {
            bounceThread.setRunning(false);
            boolean retry = true;
            while (retry) {
                try {
                    bounceThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    Log.v(e.getMessage(), "error");
                }
            }
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }
}


