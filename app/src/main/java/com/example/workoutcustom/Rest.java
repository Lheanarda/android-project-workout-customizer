package com.example.workoutcustom;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.util.Locale;

public class Rest extends Workouts {
    int seconds,listnumber;

    public Rest(int workoutid, String workoutName, int totalTime, int sets, int seconds,int listnumber) {
        super(workoutid, workoutName, totalTime, sets);
        setWorkoutid(workoutid);
        setWorkoutName(workoutName);
        setTotalTime(totalTime);
        setSets(sets);
        this.seconds = seconds;
        this.listnumber=listnumber;
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
