package com.cj.demo;

import android.graphics.Color;

public class CrashDefenseTest {




    public static void test() {

        int color = 0;

        color = Color.parseColor("test");

        try {
            color = Color.parseColor("test");
        } catch (IllegalArgumentException e) {
            CrashDefenseReporter.reportCrash(e);
        }



    }

}
