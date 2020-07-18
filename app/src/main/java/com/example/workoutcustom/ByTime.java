package com.example.workoutcustom;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ByTime extends Workouts {
    String name;
    int seconds,listnumber;

    private boolean stopPlaying = false;

    public ByTime(int workoutid, String workoutName, int totalTime, int sets, String name, int seconds, int listnumber) {
        super(workoutid, workoutName, totalTime, sets);
        setWorkoutid(workoutid);
        setWorkoutName(workoutName);
        setTotalTime(totalTime);
        setSets(sets);
        this.name = name;
        this.seconds = seconds;
        this.listnumber = listnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getListnumber() {
        return listnumber;
    }

    public void setListnumber(int listnumber) {
        this.listnumber = listnumber;
    }



}


