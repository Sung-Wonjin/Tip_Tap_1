package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class UploadThread extends Thread{
    private Handler handler;
    private int time;

    public UploadThread(Handler handler,int time) {
        this.handler = handler;
        this.time = time;
    }

    @Override
    public void run() {

        Message msg = new Message();

        try {
            Thread.sleep(time*1000);
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
