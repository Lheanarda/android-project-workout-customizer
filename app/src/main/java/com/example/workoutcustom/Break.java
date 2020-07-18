package com.example.workoutcustom;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Locale;

public class Break extends Workouts {
    int seconds;
    private  MediaPlayer mMediaPlayer;

    public Break(int workoutid, String workoutName, int totalTime, int sets,int seconds) {
        super(workoutid, workoutName, totalTime, sets);
        setWorkoutid(workoutid);
        setWorkoutName(workoutName);
        setTotalTime(totalTime);
        setSets(sets);
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    //set text
    public void UpdateSet(Context context,final TextView txtSet, final int counterset){
        final Animation animleftright = AnimationUtils.loadAnimation(context,R.anim.animleftright);
        Handler threadHandler= new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                txtSet.setText(counterset+"");
                txtSet.startAnimation(animleftright);
            }
        });
    }

    public void ReadAndSetFirstList(final Context context, String workoutid, final TextView txtNextWorkout, final TextView txtTitle){
        //animation
        final Animation animtopbottom = AnimationUtils.loadAnimation(context,R.anim.animtopbottom);
        DatabaseHelper myDB = new DatabaseHelper(context,null,null,1);
        String firstList = "";
        //by rep
        Cursor res = myDB.getByRepsByWorkoutIDANDListNumber(workoutid,"1");
        if(res.moveToFirst()){
            firstList = res.getString(0);
        }

        //by time
        res = myDB.getByTimeByWorkoutIDANDListNumber(workoutid,"1");
        if(res.moveToFirst()){
            firstList = res.getString(0);
        }

        //rest
        res = myDB.getRestByWorkoutIDANDListNumber(workoutid,"1");
        if(res.moveToFirst()){
            firstList = res.getString(0);
        }
        Handler threadHandler = new Handler(Looper.getMainLooper());
        final String finalFirstList = firstList;
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                mMediaPlayer =MediaPlayer.create(context,R.raw.google_preparenextlap);
                mMediaPlayer.start();
                txtTitle.setText("Prepare For Next Lap !");
                txtNextWorkout.setText(finalFirstList);

                //start animation
                txtNextWorkout.startAnimation(animtopbottom);
                txtTitle.startAnimation(animtopbottom);
            }
        });
    }

}
