package com.example.workoutcustom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class StartWorkoutAct extends AppCompatActivity {
    DatabaseHelper myDB;

    //variabel
    private int totaltime,sets,totalworkouts;
    private String workoutid,workoutname;
    private Toast backToast;
    private long backPressedTime;
    private volatile boolean stopThread = false, startWorkout = false,doReps=false,lastRep = false, lasttworep = false, dolasttworep = false;
    private String firstList;
    private int i,counterset,breaktime, saveProgress_i=1,saveProgress_set=1;
    private boolean activetime = false,activerest=false;
    //class
    private Break mBreak;
    private Workouts mWorkouts;
    private Rest mRest;
    private ByTime mByTime;
    private ByReps mByReps;
    //ui
    private TextView txtNextWorkout,txtSets
            ,txtTitleWorkout,txtTimerDone,txtTotalSet,txtClickToContinue;
    private Chronometer chromElapsedTime;
    private ImageView imgCircleTimer,btnSkipNext,btnSkipPrev;
    private ImageButton btnPauseStart;
    private int mInterval ;
    //animation
    private Animation animtopbottom,animleftright,animalphalong;

    //for audio
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        myDB = new DatabaseHelper(StartWorkoutAct.this,null,null,1);

        //ui
        imgCircleTimer = findViewById(R.id.imgCircleTimer);
        chromElapsedTime = findViewById(R.id.chronElapsedTime);
        txtNextWorkout = findViewById(R.id.txtNextWorkout);
        txtSets = findViewById(R.id.txtSets);
        txtTotalSet = findViewById(R.id.txtTotalSet);
        txtTitleWorkout = findViewById(R.id.txtTitleWorkout);
        txtTimerDone = findViewById(R.id.txtTimerDone);
        btnPauseStart = findViewById(R.id.btnPausePlay);
        btnSkipNext = findViewById(R.id.btnSkipNext);
        btnSkipPrev = findViewById(R.id.btnSkipPrevious);
        txtClickToContinue = findViewById(R.id.txtClickToContinue);

        //get extra intent
        workoutid = getIntent().getStringExtra("workoutidKEY");
        workoutname = getIntent().getStringExtra("workoutnameKEY");
        totaltime = Integer.parseInt(getIntent().getStringExtra("workouttotaltimeKEY"));
        sets = Integer.parseInt(getIntent().getStringExtra("workouttotalsetsKEY"));
        totalworkouts = Integer.parseInt(getIntent().getStringExtra("totalworkoutsKEY"));

        //Animation
        animtopbottom = AnimationUtils.loadAnimation(StartWorkoutAct.this,R.anim.animtopbottom);
        animleftright = AnimationUtils.loadAnimation(StartWorkoutAct.this,R.anim.animleftright);
        animalphalong = AnimationUtils.loadAnimation(StartWorkoutAct.this,R.anim.main_animalphalong);

        //class
        mWorkouts = new Workouts(Integer.parseInt(workoutid),workoutname,totaltime,sets);
        ReadBreakTime();
        mBreak = new Break(Integer.parseInt(workoutid),workoutname,totaltime,sets,breaktime);

        //setup
        txtTimerDone.setVisibility(View.INVISIBLE);

        //start first animation
        txtTitleWorkout.startAnimation(animtopbottom);
        txtSets.startAnimation(animleftright);
        chromElapsedTime.startAnimation(animtopbottom);

        //method
        btnPauseStart.setVisibility(View.GONE);
        btnSkipPrev.setVisibility(View.GONE);
        btnSkipNext.setVisibility(View.GONE);
        ReadAndSetFirstList();
        ReadAndSetLaps();
        startThread();

    }
    //Thread
    public void startThread(){
        stopThread = false; //2
        LoadRunnable runnable =new LoadRunnable(mInterval);
        new Thread(runnable).start();
    }
    public void stop_Thread(){
        stopThread = true; //3
    }
    class LoadRunnable implements Runnable{
        LoadRunnable(int interval){
            mInterval= interval;
        }
        @Override
        public void run() {
            LoadWorkoutsList();
        }
    }
    public void LoadWorkoutsList(){
        for (counterset = saveProgress_set;counterset<=sets;counterset++){
            if(stopThread==true) return;
            saveProgress_set = counterset;
            Log.i("check","set : "+saveProgress_set); //check
            StampStartWorkouts();
            lastRep=false; //control force, NextLap and BreakActivity
            ForceNextSetForLastRep(); //problem

            if(saveProgress_set<=sets){

                NextLap(); //problem
                Log.i("check","lastRep = "+lastRep+" + last2rep = "+lasttworep);
                BreakActivity();
                LoadWorkouts();
            }

        }
        if(stopThread==true) return;
        StopElapsedTime();
        mWorkouts.DoneAudio(StartWorkoutAct.this);
        moveToFinishedAct();
    }
    public void moveToFinishedAct(){
        Intent intent = new Intent(StartWorkoutAct.this,FinishWorkoutAct.class);
        intent.putExtra("workoutnameKEY",workoutname);
        startActivity(intent);
        finish();
    }

    //progress
    public void StampStartWorkouts(){
        if(startWorkout == false){
            StartingWorkoutPreperations();
            startWorkout = true;
        }
    }

    public void ReadAndSetNextWorkoutList(){
        String result="";
        //code finder
        int nextlistnumber = i+1;
        Cursor res = myDB.getByRepsByWorkoutIDANDListNumber(workoutid,nextlistnumber+"");
        if(res.moveToFirst()){
            result = res.getString(0);
        }

        res = myDB.getByTimeByWorkoutIDANDListNumber(workoutid,nextlistnumber+"");
        if(res.moveToFirst()){
            result = res.getString(0);
        }

        res = myDB.getRestByWorkoutIDANDListNumber(workoutid,nextlistnumber+"");
        if(res.moveToFirst()){
            result = res.getString(0);
        }

        if (result==""){
            result = "Finished";
        }
        //end code finder

        Handler threadHandler = new Handler(Looper.getMainLooper());
        final String finalResult = result;
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                txtNextWorkout.setText(finalResult);
                txtNextWorkout.startAnimation(animtopbottom);
            }
        });
    }
    public void LoadWorkouts(){
        for(i=saveProgress_i;i<=totalworkouts;i++){
            if(stopThread==true) return;
            saveProgress_i = i;
            Log.i("check","interval : "+i); //check
            for(int j = i; j<=totalworkouts;j++){
                Cursor res = myDB.getByRepsByWorkoutIDANDListNumber(workoutid,j+"");
                if(res.moveToFirst()){
                    if(Integer.parseInt(res.getString(2)) == i){
                        //by rep class
                        mByReps = new ByReps(Integer.parseInt(workoutid),workoutname,totaltime,sets
                                ,res.getString(0)
                                ,Integer.parseInt(res.getString(1))
                                ,Integer.parseInt(res.getString(2)));

                        //pass method
                        RepClick();
                        //set next workout
                        ReadAndSetNextWorkoutList();
                    }
                }
                res = myDB.getByTimeByWorkoutIDANDListNumber(workoutid,j+"");
                if(res.moveToFirst()){
                    doReps=false;
                    Log.i("ByTime","rep : "+doReps);
                    if(Integer.parseInt(res.getString(2)) == i){
                        //by time class
                        mByTime = new ByTime(Integer.parseInt(workoutid),workoutname,totaltime,sets
                                ,res.getString(0)
                                ,Integer.parseInt(res.getString(1))
                                ,Integer.parseInt(res.getString(2)));
                        activetime=true;
                        mByTime.DisableClickable(txtTimerDone,txtClickToContinue);
                        //pass method
                        final String titleWorkout = res.getString(0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtTitleWorkout.setText(titleWorkout);
                                txtTitleWorkout.startAnimation(animtopbottom);
                            }
                        });
                        mByTime.startTimerByTime(txtTimerDone,StartWorkoutAct.this,mByTime.getSeconds());
                        mInterval = mByTime.getSeconds()*1000;

                        //set next workout
                        ReadAndSetNextWorkoutList();
                        try {
                            Thread.sleep(mInterval);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }

                res = myDB.getRestByWorkoutIDANDListNumber(workoutid,j+"");
                if(res.moveToFirst()){
                    doReps=false;

                    Log.i("Rest","rep : "+doReps);
                    if(Integer.parseInt(res.getString(2)) == i){
                        //rest class
                        mRest = new Rest(Integer.parseInt(workoutid),workoutname,totaltime,sets
                                ,Integer.parseInt(res.getString(1))
                                ,Integer.parseInt(res.getString(2)));
                        activerest=true;
                        mRest.DisableClickable(txtTimerDone,txtClickToContinue);
                        //pass method
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtTitleWorkout.setText("Rest");
                                txtTitleWorkout.startAnimation(animtopbottom);
                            }
                        });
                        mRest.startTimerByTime(txtTimerDone,StartWorkoutAct.this,mRest.getSeconds());
                        mInterval = mRest.getSeconds()*1000;
                        //set next workout
                        ReadAndSetNextWorkoutList();
                        try {
                            Thread.sleep(mInterval);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void RepClick(){
        mByReps.ClickToProgress(StartWorkoutAct.this,txtTimerDone,txtTitleWorkout,txtClickToContinue);
        stop_Thread();
        Handler threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                txtTimerDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doReps=true;
                        if(saveProgress_i==totalworkouts){
                            dolasttworep = true;
                        }else{
                            saveProgress_i = saveProgress_i+1;
                        }
                        startThread();

                        mMediaPlayer = MediaPlayer.create(StartWorkoutAct.this,R.raw.beep);
                        mMediaPlayer.start();
                    }
                });

                txtClickToContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doReps=true;
                        if(saveProgress_i==totalworkouts)lasttworep = false;
                        saveProgress_i = saveProgress_i+1;
                        startThread();

                        mMediaPlayer = MediaPlayer.create(StartWorkoutAct.this,R.raw.beep);
                        mMediaPlayer.start();
                    }
                });
            }
        });

    }

    public void NextLap(){
        ReadLastLapForRep();
        checkLastTwoWorkout();
        if(saveProgress_i==totalworkouts && doReps==false  || lastRep && lasttworep==false  ){
            Log.i("check","called :" +doReps);
            saveProgress_i=1;
        }
    }
    public void ForceNextSetForLastRep(){
        ReadLastLapForRep();
        checkLastTwoWorkout();
        if(lastRep && lasttworep==false){
            saveProgress_set=saveProgress_set+1;
            Log.i("check","Force set : " + saveProgress_set);
        }
    }
    public void BreakActivity(){
        ReadLastLapForRep();
        checkLastTwoWorkout();
        if(saveProgress_set!=1 && doReps==false || lastRep && lasttworep==false )  {
            Log.i("check","Interrupted Break");
            mBreak.DisableClickable(txtTimerDone,txtClickToContinue);
            mBreak.ReadAndSetFirstList(StartWorkoutAct.this,workoutid,txtNextWorkout,txtTitleWorkout);
            mBreak.startTimerByTime(txtTimerDone,StartWorkoutAct.this,mBreak.getSeconds());
            mInterval = mBreak.getSeconds()*1000;
            dolasttworep = false;
            try {
                Thread.sleep(mInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mBreak.UpdateSet(StartWorkoutAct.this,txtSets,saveProgress_set);
        }
    }

    public void checkLastTwoWorkout(){
        Cursor res = myDB.getByRepsByWorkoutIDANDListNumber(workoutid,totalworkouts+"");
        if(res.moveToFirst()){
            int listprev = totalworkouts-1;
            res = myDB.getByRepsByWorkoutIDANDListNumber(workoutid,listprev+"");
            if(res.moveToFirst()) {
                if(saveProgress_i==totalworkouts && dolasttworep ==false)lasttworep = true;
                else lasttworep = false;
            }
        }
    }
    public void ReadLastLapForRep(){
        Cursor res = myDB.getByRepsByWorkoutIDANDListNumber(workoutid,totalworkouts+"");
        if(res.moveToFirst()){
            if(saveProgress_i==totalworkouts){
                lastRep=true;
            }
        }
    }

    public void StopElapsedTime(){
        if(counterset>sets){
            Handler threadHandler = new Handler(Looper.getMainLooper());
            threadHandler.post(new Runnable() {
                @Override
                public void run() {
                    chromElapsedTime.stop();
                }
            });
        }
    }

    //pause play
    public void PausePlayWorkout(View view){
        if(Workouts.onPauseWorkout == false){
            mByTime.PauseOnGoingWorkout(btnPauseStart);
            stop_Thread();
        }
        else{
            mByTime.StartOnPauseWorkout(btnPauseStart,txtTimerDone,StartWorkoutAct.this);
            startThread();
        }
    }

    //preparation
    public void ReadBreakTime(){
        Cursor res = myDB.getBreakSecond(workoutid);
        if(res.moveToFirst()){
            breaktime = Integer.parseInt(res.getString(0));
        }
    }
    public void ReadAndSetLaps (){
        Cursor res = myDB.getWorkoutById(workoutid);
        if(res.moveToFirst()){
            String set = res.getString(3);
            txtTotalSet.setText("/ "+set);
        }
    }
    public void ReadAndSetFirstList(){
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
        txtNextWorkout.setText(firstList);
        txtNextWorkout.startAnimation(animtopbottom);
//        Handler threadHandler = new Handler(Looper.getMainLooper());
//        threadHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                txtNextWorkout.setText(firstList);
//            }
//        });
    }
    public void StartingWorkoutPreperations(){
        mWorkouts.WelcomeAudio(StartWorkoutAct.this,chromElapsedTime,txtTimerDone);

        //for matching elapsed time
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //stop
    public void StopAudio(){
        mWorkouts.stopPlayingTrue();
        mWorkouts.StopActivity();
        if(activerest) {
            mRest.stopPlayingTrue();
        }

        if(activetime){
            mByTime.stopPlayingTrue();
        }
        mBreak.stopPlayingTrue();
    }

    //backpressed
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            Thread.interrupted();
            stop_Thread();
            StopAudio();
            this.finish();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
