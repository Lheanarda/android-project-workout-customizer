package com.example.workoutcustom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class PickedWorkoutsAct extends AppCompatActivity {

    private DatabaseHelper myDB;
    private ArrayList name,repsecond,textrepsecond;
    private PickedWorkoutsAdapter mPickedWorkoutsAdapter;

    private String workoutid,workoutname,workouttotaltime,workouttotalsets,breaktime="";
    //for ui
    private RecyclerView rvWorkoutList;
    private Button btnStart;
    private TextView txtTitle,txtTotalTime,txtSets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picked_workouts);

        //db
        myDB = new DatabaseHelper (PickedWorkoutsAct.this,null,null,1);

        //ui
        btnStart = findViewById(R.id.btnStart);
        rvWorkoutList = findViewById(R.id.rvWorkoutList);
        txtTitle = findViewById(R.id.txtTitle);
        txtSets = findViewById(R.id.txtTotalSets);
        txtTotalTime = findViewById(R.id.txtTotalTime);

        //get extra intent
        workoutid = getIntent().getStringExtra("workoutidKEY");

        //ArrayList
        name = new ArrayList<String>();
        repsecond = new ArrayList<String>();
        textrepsecond = new ArrayList<String>();

        //method
        ReadWorkoutByWorkoutid();
        InputArrayListFromDB();
        ImplementAdapterPicked();

    }

    public String setTimeText(String timesecond){
        int totalsecond = Integer.parseInt(timesecond);
        //7500
        int hour = totalsecond/3600;
        int minute = totalsecond/60;
        minute = minute%60;
        int second = totalsecond%60;

        String time = String.format(Locale.getDefault(),"%02d:%02d:%02d",hour,minute,second);
        return time;
    }

    public void ReadWorkoutByWorkoutid(){
        Cursor res = myDB.getWorkoutById(workoutid);
        if(res.moveToFirst()){
            workoutname = res.getString(1);
            workouttotaltime = res.getString(2);
            workouttotalsets = res.getString(3);

            txtTitle.setText(workoutname);
            txtTotalTime.setText(setTimeText(workouttotaltime));
            txtSets.setText(workouttotalsets);
        }
    }

    public int ReadTotalWorkout(){
        int total=0;
        Cursor res = myDB.getTotalWorkoutByWorkoutID(workoutid,workoutid,workoutid);
        if(res.moveToFirst()){
            total = Integer.parseInt(res.getString(0));
            Log.i("checktotal",total+"");
        }
        return total;
    }

    public void ReadBreak(){
        Cursor res =myDB.getBreakByWorkoutid(workoutid);
        if(res.moveToFirst()){
            breaktime=res.getString(1);
        }
    }

    public void InputArrayListFromDB(){
        int total= ReadTotalWorkout();
        for(int i=1;i<=total;i++){
            for(int j = i; j<=total;j++){
                Cursor resByRep = myDB.getByRepsByWorkoutIDANDListNumber(workoutid,j+"");
                Cursor resByTime = myDB.getByTimeByWorkoutIDANDListNumber(workoutid,j+"");
                Cursor resRest = myDB.getRestByWorkoutIDANDListNumber(workoutid,j+"");
                if(resByRep.moveToFirst()){
                    if(Integer.parseInt(resByRep.getString(2)) == i){
                        name.add(resByRep.getString(0));
                        repsecond.add(resByRep.getString(1));
                        textrepsecond.add(resByRep.getString(3));
                    }
                }
                else if(resByTime.moveToFirst()){
                    if(Integer.parseInt(resByTime.getString(2)) == i){
                        name.add(resByTime.getString(0));
                        repsecond.add(resByTime.getString(1));
                        textrepsecond.add(resByTime.getString(3));
                    }
                }
                else if(resRest.moveToFirst()){
                    if(Integer.parseInt(resRest.getString(2)) == i){
                        name.add(resRest.getString(0));
                        repsecond.add(resRest.getString(1));
                        textrepsecond.add(resRest.getString(3));
                    }
                }
            }
        }
        ReadBreak();
        if(!breaktime.equals("")){
            name.add("Break");
            repsecond.add(breaktime);
            textrepsecond.add("seconds");
        }

    }

    public void ImplementAdapterPicked(){
        mPickedWorkoutsAdapter = new PickedWorkoutsAdapter(name,repsecond,textrepsecond,PickedWorkoutsAct.this,workoutid);
        rvWorkoutList.setAdapter(mPickedWorkoutsAdapter);
        rvWorkoutList.setLayoutManager(new LinearLayoutManager(PickedWorkoutsAct.this));
    }

    public void btnStartClick(View view){
        Intent intent = new Intent(PickedWorkoutsAct.this,StartWorkoutAct.class);
        String total = ReadTotalWorkout()+"";
        intent.putExtra("totalworkoutsKEY",total);
        intent.putExtra("workoutidKEY",workoutid);
        intent.putExtra("workoutnameKEY",workoutname);
        intent.putExtra("workouttotaltimeKEY",workouttotaltime);
        intent.putExtra("workouttotalsetsKEY",workouttotalsets);
        startActivity(intent);
    }
}
