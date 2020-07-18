package com.example.workoutcustom;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class Workouts {
    public static boolean onPauseWorkout = false;
    private String workoutName;
    private int workoutid,totalTime,sets,timerSecondValue;

    private CountDownTimer mCountDownTimer;
    private long  timervalue ;
    Context mContext;

    //for audio
    private MediaPlayer mMediaPlayer;

    //kill audio
    private boolean stopPlaying = false;

    public Workouts(int workoutid,String workoutName, int totalTime, int sets) {
        this.workoutName = workoutName;
        this.workoutid = workoutid;
        this.totalTime = totalTime;
        this.sets = sets;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public int getWorkoutid() {
        return workoutid;
    }

    public void setWorkoutid(int workoutid) {
        this.workoutid = workoutid;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    //ElapsedTime
    public void startWorkoutElapsedTime(Chronometer chronotimer){
        chronotimer.setBase(SystemClock.elapsedRealtime());
        chronotimer.start();
    }

    //Welcome Audio
    public void WelcomeAudio(Context context, final Chronometer chronotimer, final TextView txtTimer){
        //animation
        Handler threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                txtTimer.setVisibility(View.VISIBLE);
                txtTimer.setText("Welcome");
            }
        });
        //welcome
        mMediaPlayer = MediaPlayer.create(context,R.raw.google_welcome);
        mMediaPlayer.start();
        StopActivity();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //start in
        mMediaPlayer = MediaPlayer.create(context,R.raw.google_startin);
        mMediaPlayer.start();
        StopActivity();
        try {
            Thread.sleep(2200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //5
        mMediaPlayer = MediaPlayer.create(context,R.raw.google_5);
        mMediaPlayer.start();
        StopActivity();
        threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                txtTimer.setText("5");
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //4
        mMediaPlayer = MediaPlayer.create(context,R.raw.google_4);
        mMediaPlayer.start();
        StopActivity();
        threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                txtTimer.setText("4");
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //3
        mMediaPlayer = MediaPlayer.create(context,R.raw.google_3);
        mMediaPlayer.start();
        StopActivity();
        threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                txtTimer.setText("3");
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //2
        mMediaPlayer = MediaPlayer.create(context,R.raw.google_2);
        mMediaPlayer.start();
        StopActivity();
        threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                txtTimer.setText("2");
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //1
        mMediaPlayer = MediaPlayer.create(context,R.raw.google_1);
        mMediaPlayer.start();
        StopActivity();
        threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                txtTimer.setText("1");
            }
        });
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //0
        mMediaPlayer = MediaPlayer.create(context,R.raw.beep);
        StopActivity();
        mMediaPlayer.start();
        threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                txtTimer.setText("GO");
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                startWorkoutElapsedTime(chronotimer);
            }
        });

    }

    //done workout
    public void DoneAudio (Context context){
        mMediaPlayer = MediaPlayer.create(context,R.raw.google_congratulations);
        Handler threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                mMediaPlayer.start();
                StopActivity();
            }
        });
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //START TIMER
    public void startTimerByTime (final TextView txtTimer, Context context, final int seconds){
        mContext = context;
        timervalue = seconds*1000;
        timerSecondValue = seconds;
        Handler threadHandler= new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                mCountDownTimer =new CountDownTimer(timervalue,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timervalue = millisUntilFinished;
                        updateCountDownText(mContext,txtTimer,seconds);
                    }

                    @Override
                    public void onFinish() {
                        //NEXT ACTION
                    }
                }.start();
            }
        });

    }

    public void updateCountDownText (Context context,TextView txtTimer,int seconds){
        //load audio
        int timeminute = (int) (timervalue/1000)/60;
        int timesecond = (int) (timervalue/1000)%60;

        if(timeminute==1 && timesecond==0 && timervalue!=seconds*1000){
            mMediaPlayer=MediaPlayer.create(context,R.raw.google_1minleft);
            mMediaPlayer.start();
            StopActivity();
        }

        if(timeminute==0 && timesecond==30 && timervalue!=seconds*1000){
            mMediaPlayer = MediaPlayer.create(context,R.raw.google_30sleft);
            mMediaPlayer.start();
            StopActivity();
        }

        if(timeminute==0 && timesecond==10 && timervalue!=seconds*1000) {
            //INSERT SOUND
            mMediaPlayer = MediaPlayer.create(context,R.raw.google_10sleft);
            mMediaPlayer.start();
            StopActivity();
        }

        if(timeminute==0 && timesecond<=5 && timesecond >=1){
            mMediaPlayer = MediaPlayer.create(context,R.raw.clocktik);
            mMediaPlayer.start();
            StopActivity();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mMediaPlayer.release();
                }
            });
        }

        if(timeminute==0 && timesecond==0){
            mMediaPlayer = MediaPlayer.create(context,R.raw.beep);
            mMediaPlayer.start();
            StopActivity();
            mCountDownTimer.cancel();
        }

        String timeLeft = String.format(Locale.getDefault(),"%02d:%02d",timeminute,timesecond);
        txtTimer.setText(timeLeft);
        timerSecondValue--;
    }

    //STOP TIMER
    public void StopActivity(){
        if(stopPlaying){
            mMediaPlayer.pause();
            mMediaPlayer.stop();
            if(mCountDownTimer!=null){
                mCountDownTimer.cancel();
            }
        }

    }

    public void stopPlayingTrue(){
        stopPlaying = true;
    }

    //DISABLE CLICK
    public void DisableClickable(final TextView txtTimer, final TextView txtClick){
        Handler threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                txtTimer.setClickable(false);
                txtClick.setClickable(false);
                txtClick.setVisibility(View.INVISIBLE);
            }
        });
    }

    //PAUSE WORKOUT
    public void PauseOnGoingWorkout(ImageButton btnPlayPause){
        onPauseWorkout = true;
        btnPlayPause.setImageResource(R.drawable.ic_play_white);
        mCountDownTimer.cancel();
    }

    //START PAUSE WORKOUT
    public void StartOnPauseWorkout(ImageButton btnPlayPause, TextView txtTimer, Context context){
        onPauseWorkout = false;
        btnPlayPause.setImageResource(R.drawable.ic_pause);
        startTimerByTime(txtTimer,context,timerSecondValue);
    }
}
