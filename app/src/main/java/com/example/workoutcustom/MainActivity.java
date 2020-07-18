package com.example.workoutcustom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity  {
    private Animation animalpha;
    public static Activity mainAct;
    private DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //static act
        mainAct=this;

        //load animation
        animalpha = AnimationUtils.loadAnimation(this,R.anim.animalpha);

        BottomNavigationView bottomNav =findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.menu_myworkouts);

        //start animation
        bottomNav.startAnimation(animalpha);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new MyWorkoutsFragment()).commit(); //DEFAULT FRAGMENT
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.menu_myworkouts:
                            selectedFragment = new MyWorkoutsFragment();
                            break;
                        case R.id.menu_stopwatch:
                            selectedFragment = new StopwatchFragment();
                            break;
                        case R.id.menu_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };


}
