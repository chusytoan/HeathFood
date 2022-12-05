package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ManHinhchao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinhchao);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    startActivity(new Intent(ManHinhchao.this, MainActivity.class));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}