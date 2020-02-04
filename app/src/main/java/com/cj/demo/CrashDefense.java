package com.cj.demo;

import android.graphics.Color;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrashDefense {

    public static int parseColor(String color) {
        try {
            return Color.parseColor(color);
        } catch (IllegalArgumentException e) {
            CrashDefenseReporter.reportCrash(e);
            return 0;
        }
    }

    public static Object get(ConcurrentHashMap concurrentHashMap, Object key) {
        try {
            return concurrentHashMap.get(key);
        } catch (Throwable t) {
            CrashDefenseReporter.reportCrash(t);
            return null;
        }
    }

    public static Object get(Map map, Object key) {
        try {
            return map.get(key);
        } catch (Throwable t) {
            CrashDefenseReporter.reportCrash(t);
            return null;
        }
    }

    public static int valueOf(String s) {
        try {
            return Integer.valueOf(s);
        } catch (Exception e) {
            CrashDefenseReporter.reportCrash(e);
            return -1;
        }
    }

}
