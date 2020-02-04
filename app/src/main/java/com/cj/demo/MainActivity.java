package com.cj.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends AppCompatActivity {

    public ConcurrentHashMap<String, String> concurrentHashMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CrashDefenseTest.test();
    }
}
