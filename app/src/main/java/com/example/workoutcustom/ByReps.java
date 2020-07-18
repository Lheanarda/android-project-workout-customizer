package com.example.workoutcustom;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class ByReps extends Workouts {
    private String name;
    private int reps,listnumber;
    private Animation animalpha,animtopbottom;

    public ByReps(int workoutid, String workoutName, int totalTime, int sets, String name, int reps, int listnumber) {
        super(workoutid, workoutName, totalTime, sets);
        setWorkoutid(workoutid);
        setWorkoutName(workoutName);
        setTotalTime(totalTime);
        setSets(sets);
        this.name = name;
        this.reps = reps;
        this.listnumber = listnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getListnumber() {
        return listnumber;
    }

    public void setListnumber(int listnumber) {
        this.listnumber = listnumber;
    }

    //SHOW BUTTON
    public void ClickToProgress(final Context context, final TextView txtTimerDone, final TextView txtTitle, final TextView txtClick){
        Handler threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                animalpha = AnimationUtils.loadAnimation(context,R.anim.animalpha);
                animtopbottom = AnimationUtils.loadAnimation(context,R.anim.animtopbottom);
                txtTimerDone.setText(getReps()+"x");
                txtTimerDone.setGravity(Gravity.CENTER);
                txtTitle.setText(getName());
                txtClick.setVisibility(View.VISIBLE);

                //start animation
                txtTitle.startAnimation(animtopbottom);
                txtTimerDone.startAnimation(animalpha);
                txtClick.startAnimation(animalpha);
            }
        });

    }
}
