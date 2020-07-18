package com.example.workoutcustom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashAct extends AppCompatActivity {

    private TextView txtTitle,txtSubText,btnExercise;
    private ImageView imgPage;
    private Animation animAlpha,animbottomtop,animalphalong,progressload;
    private View bgprogresstop;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //db
        db = new DatabaseHelper(this,null,null,1);

        //ui
        txtTitle = findViewById(R.id.txtTitle);
        txtSubText = findViewById(R.id.txtSubText);
        btnExercise = findViewById(R.id.btnExercise);
        imgPage = findViewById(R.id.imgPage);
        bgprogresstop = findViewById(R.id.bgprogresstop);

        //load animation
        animAlpha = AnimationUtils.loadAnimation(this,R.anim.animalpha);
        animbottomtop = AnimationUtils.loadAnimation(this,R.anim.animbottomtop);
        animalphalong = AnimationUtils.loadAnimation(this,R.anim.animalphalong);
        progressload = AnimationUtils.loadAnimation(this,R.anim.progressload);

        //pass animation
        imgPage.startAnimation(animAlpha);
        txtTitle.startAnimation(animbottomtop);
        txtSubText.startAnimation(animbottomtop);
        bgprogresstop.startAnimation(progressload);
        btnExercise.startAnimation(animalphalong);

        //method
        btnExercise.setClickable(false);
        clickAfterAnimation();

    }

    public void clickAfterAnimation(){
        progressload.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                btnExercise.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashAct.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
