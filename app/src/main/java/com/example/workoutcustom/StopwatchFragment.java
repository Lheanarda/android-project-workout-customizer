package com.example.workoutcustom;

import android.os.Bundle;
import android.os.SystemClock;
import android.print.PrinterId;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StopwatchFragment extends Fragment {
    private ImageButton btnPlay,btnPause,btnStop;
    private ImageView imgStopwatchBackground, imgAnchor;
    private Animation rotate,stop_rotate;
    private Chronometer chronoTimer;
    private boolean startStopwatchFlag = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stopwatch,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnPlay = view.findViewById(R.id.imgPlay);
        btnPause = view.findViewById(R.id.imgBtnPause);
        btnStop = view.findViewById(R.id.imgStop);
        imgAnchor = view.findViewById(R.id.imgAnchor);
        imgStopwatchBackground = view.findViewById(R.id.imgStopwatchBackground);
        chronoTimer = view.findViewById(R.id.chronotimer);

        //animation
        rotate = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_anchor);
        stop_rotate = AnimationUtils.loadAnimation(getContext(),R.anim.stop_anchor);

        //method
        btnStop.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        startStopwatch();
        pauseStopwatch();
        stopStopwatch();
    }

    public void startStopwatch(){
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgAnchor.startAnimation(rotate);
                //start
                if(startStopwatchFlag){
                    chronoTimer.start();
                }else{
                    chronoTimer.setBase(SystemClock.elapsedRealtime());
                    chronoTimer.start();
                }
                startStopwatchFlag = true;
                btnStop.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.GONE);
            }
        });
    }

    public void pauseStopwatch(){
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgAnchor.startAnimation(stop_rotate);
                //pause
                chronoTimer.stop();
                btnPause.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);
            }
        });
    }

    public void stopStopwatch(){
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgAnchor.startAnimation(stop_rotate);
                startStopwatchFlag = false;
                btnPause.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);

                chronoTimer.setBase(SystemClock.elapsedRealtime());
                chronoTimer.stop();
            }
        });

    }

    public void alertDialogSave(){
        TextView msg = new TextView(getContext());
        msg.setText("Do you want to save the result ?");

    }

}
