package com.example.user.lab3_bounce;

import android.graphics.Bitmap;

class BounceItem
{
    float X = 0;
    float Y = 0;
    Bitmap ball;
    float accel = 0;
    float grav = 0;
    float speed = 0;
    float lastY = 0;
    int liveCounter = 0;
    boolean toRight = true;
}
