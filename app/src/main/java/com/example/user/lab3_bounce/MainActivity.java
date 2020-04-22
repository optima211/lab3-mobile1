package com.example.user.lab3_bounce;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    BounceView bounceView;
    private Boolean processStarted;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processStarted = false;

        bounceView = new BounceView(this);
        setContentView(bounceView);

        processStarted = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        try {
            super.onStop();
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    @Override
    public void onBackPressed() {
        try {
            bounceView.pause();
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
            if (processStarted) {
                bounceView.pause();
            }
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            if (processStarted) {
                bounceView.resume();
            }
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }

    @Override
    public void onDestroy() {
        try {
            super.onDestroy();
            if (processStarted) {
                bounceView.pause();
            }
        } catch (Exception ex) {
            Log.v(ex.getMessage(), "error");
        }
    }
}


