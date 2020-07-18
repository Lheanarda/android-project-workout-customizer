package com.example.workoutcustom;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyWorkoutsFragment extends Fragment  {
    DatabaseHelper myDB;
    private int workoutid,totalTime,sets;
    private String workoutname;
    private RecyclerView rvMyWorkouts;
    private MyWorkoutsAdapter mMyWorkoutsAdapter;
    private TextView tvMyWorkout,txtline,txtDescription;
    private Animation bottomtop,animalpha;
    private Button btnnewworkout;

    //ArrayList
    ArrayList<String> listwname;
    ArrayList<Integer> listwtotaltime,listwsets,listwid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myworkouts,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //db
        myDB =new DatabaseHelper(getContext(),null,null,1);

        //ui
        rvMyWorkouts = getView().findViewById(R.id.rvMyWorkouts);
        tvMyWorkout = getView().findViewById(R.id.textViewMyWorkout);
        txtline = getView().findViewById(R.id.txtlinemyworkouts);
        txtDescription = getView().findViewById(R.id.textViewDescription);
        btnnewworkout = getView().findViewById(R.id.btnnewworkout);

        //animation
        animalpha = AnimationUtils.loadAnimation(getContext(),R.anim.animalpha);
        bottomtop = AnimationUtils.loadAnimation(getContext(),R.anim.main_bottomtop);

        //start animation
        tvMyWorkout.startAnimation(animalpha);
        txtDescription.startAnimation(animalpha);
        btnnewworkout.startAnimation(animalpha);
        txtline.startAnimation(animalpha);
        rvMyWorkouts.startAnimation(bottomtop);

        //ArrayList
        listwname = new ArrayList<String>();
        listwtotaltime = new ArrayList<Integer>();
        listwsets = new ArrayList<Integer>();
        listwid = new ArrayList<Integer>();

        //method
        btnNewClick();
        ReadAllWorkout();
        implementAdapter();

    }
    public void btnNewClick(){
        btnnewworkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewWorkout();
                Intent intent = new Intent(getContext(),UpdateWorkoutsAct.class);
                startActivity(intent);
            }
        });

    }

    public void addNewWorkout(){
        myDB.insertWorkouts("",0,0);
    }

    public void ReadAllWorkout (){
        Cursor res = myDB.getAllWorkouts();
        if(res.getCount()>0){
            while (res.moveToNext()){
                workoutid =Integer.parseInt(res.getString(0));
                workoutname = res.getString(1);
                totalTime = Integer.parseInt(res.getString(2));
                sets = Integer.parseInt(res.getString(3));

                listwid.add(workoutid);
                listwname.add(workoutname);
                listwsets.add(sets);
                listwtotaltime.add(totalTime);
            }
        }
    }

    public void implementAdapter(){
        mMyWorkoutsAdapter = new MyWorkoutsAdapter(getContext(),listwid,listwname,listwtotaltime,listwsets);
        rvMyWorkouts.setAdapter(mMyWorkoutsAdapter);
        rvMyWorkouts.setLayoutManager(new LinearLayoutManager(getContext()));
    }




}
