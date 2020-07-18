package com.example.workoutcustom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class FinishWorkoutAct extends AppCompatActivity {

    private Animation loadend;
    private Animation alpha,startalpha;
    private TextView txtbgend, txtend,txtworkoutname,txtcongrats,textfinished,textworkout;
    private ImageView imgFinished;
    private String workoutname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_workout);

        //ui
        txtbgend = findViewById(R.id.txtbgEnd);
        txtend = findViewById(R.id.txtEnd);
        txtworkoutname = findViewById(R.id.txtWorkoutName);
        txtcongrats = findViewById(R.id.txtcongrats);
        textfinished = findViewById(R.id.textfinished);
        textworkout = findViewById(R.id.textworkout);
        imgFinished = findViewById(R.id.imgdoneend);

        //get extra
        workoutname = getIntent().getStringExtra("workoutnameKEY");

        //Animation
        loadend = AnimationUtils.loadAnimation(FinishWorkoutAct.this,R.anim.loadend);
        alpha = AnimationUtils.loadAnimation(FinishWorkoutAct.this,R.anim.animalphaend);
        startalpha = AnimationUtils.loadAnimation(FinishWorkoutAct.this,R.anim.animalpha);

        //method
        txtworkoutname.setText(workoutname);
        txtbgend.startAnimation(loadend);
        txtend.startAnimation(alpha);

        txtcongrats.startAnimation(startalpha);
        textfinished.startAnimation(startalpha);
        textworkout.startAnimation(startalpha);
        imgFinished.startAnimation(startalpha);
        txtworkoutname.startAnimation(startalpha);

        endClick();
    }

    public void endClick(){
        txtbgend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishWorkoutAct.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
