package com.example.workoutcustom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class UpdateWorkoutsAct extends AppCompatActivity implements UpdateRestDialog.RestDialogListener,
        UpdateByTimeDialog.ByTimeDialogListener, UpdateByRepDialog.ByRepDialogListener, UpdateBreakDialog.BreakDialogListener
        , DeleteWorkoutInputDialog.DeleteDialogListener {

    //db
    private DatabaseHelper myDB;
    //ui
    private FloatingActionButton fabRep,fabTime,fabRest,fabBreak;
    private ImageView btnPlus,btnMinus,imgDeleteBreak;
    private TextView txtSave,txtTitleWorkout,txtDuration,txtTotalExercises,txtBreak,txtUpdateBreak,textBreak;
    private EditText edtSets,edtTitle;
    private RecyclerView rvUpdateWorkouts;
    //variable
    private int workoutid_lastinserted,listnumber=1,hour,minute,second,totalsecond=0,totalworkout=0;
    private String workoutid_lastinserted_String="0",duration,breaktime="";
    private long backPressedTime;
    private Toast backToast;
    private ArrayList name,repsecond,textrepsecond;
    private WorkoutListInputAdapter mWorkoutListInputAdapter;
    private boolean onFinished = false, onUpdate = false, onInputRep=false
            ,onInputTime=false,onInputRest = false,onInputBreak = false;
    private String INPUT_WORKOUT="Input";
    //class
    private ByReps mByReps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_workouts);
        //db
        myDB = new DatabaseHelper(UpdateWorkoutsAct.this,null,null,1);

        //ui
        fabBreak = findViewById(R.id.fabBreak);
        fabRep = findViewById(R.id.fabByRep);
        fabTime = findViewById(R.id.fabByTime);
        fabRest= findViewById(R.id.fabRest);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        txtSave = findViewById(R.id.txtSave);
        txtTitleWorkout = findViewById(R.id.txtTitleWorkout);
        txtDuration =findViewById(R.id.txtDuration);
        txtTotalExercises = findViewById(R.id.txtTotalWorkout);
        edtSets = findViewById(R.id.edtSets);
        edtTitle = findViewById(R.id.edtWorkoutTitle);
        rvUpdateWorkouts = findViewById(R.id.rvUpdateWorkouts);
        txtBreak = findViewById(R.id.txtBreak);
        txtUpdateBreak = findViewById(R.id.txtUpdateBreak);
        textBreak = findViewById(R.id.textBreak);
        imgDeleteBreak = findViewById(R.id.imgDeleteBreak);

        //get extra
        onUpdate = getIntent().getBooleanExtra("update",false);

        //ArrayList
        name = new ArrayList<String>();
        repsecond = new ArrayList<String>();
        textrepsecond = new ArrayList<String>();

        //method
        ifUpdate();
        ReadBreak();
        ShowBreakFab();
        fabRepClick();
        fabTimeClick();
        fabRestClick();
        fabBreakClick();
        UpdateBreak();
        DeleteBreakClick();
    }

    public void ifUpdate(){
        if(onUpdate){
            getUpdateID();
            InputArrayListFromDB();
            ImplementAdapterInput();
            listnumber = ReadTotalWorkout()+1;
            Cursor res = myDB.getWorkoutById(workoutid_lastinserted_String);
            if(res.moveToFirst()){
                edtTitle.setText(res.getString(1));
                edtSets.setText(res.getString(3));
                int set = Integer.parseInt(res.getString(3));
                int total = Integer.parseInt(res.getString(2));
                if(total!=0 && set!=0){
                    totalsecond = total/set;
                    Log.i("checksecond",total+"");
                    Log.i("checksecond",set+"");
                    Log.i("checksecond",totalsecond+"");
                }else{
                    totalsecond = 0;
                }

            }
            totalworkout = ReadTotalExercise();
            txtTotalExercises.setText(totalworkout+"");
            ConvertDuration();
        }else{
            getLastInserted();
        }
    }

    public int ReadTotalWorkout(){
        int total=0;
        Cursor res = myDB.getTotalWorkoutByWorkoutID(workoutid_lastinserted_String,workoutid_lastinserted_String,workoutid_lastinserted_String);
        if(res.moveToFirst()){
            total = Integer.parseInt(res.getString(0));
            Log.i("checktotal",total+"");
        }
        return total;
    }
    public int ReadTotalExercise(){
        int total=0;
        Cursor res= myDB.getByTimeByWorkoutID(workoutid_lastinserted_String);
        if(res.getCount()>0)total = total+res.getCount();

        res = myDB.getByRepsByWorkoutID(workoutid_lastinserted_String);
        if(res.getCount()>0)total = total+res.getCount();

        return total;
    }
    public void ReadBreak(){
        Cursor res =myDB.getBreakByWorkoutid(workoutid_lastinserted_String);
        if(res.getCount()>0){
            if(res.moveToFirst()){
                breaktime=res.getString(1);

                textBreak.setVisibility(View.VISIBLE);
                txtUpdateBreak.setVisibility(View.VISIBLE);
                txtBreak.setVisibility(View.VISIBLE);
                imgDeleteBreak.setVisibility(View.VISIBLE);
                int totalsecs = Integer.parseInt(breaktime);
                int min = totalsecs/60;
                int sec = totalsecs%60;
                String textBreak = String.format(Locale.getDefault(),"%2d min %2d sec / lap",min,sec);
                txtBreak.setText(textBreak);
            }
        }else{
            textBreak.setVisibility(View.GONE);
            txtUpdateBreak.setVisibility(View.GONE);
            txtBreak.setVisibility(View.GONE);
            imgDeleteBreak.setVisibility(View.GONE);
            breaktime = "";
        }
    }
    public void ShowBreakFab(){
        if(!breaktime.equals("")){
            fabBreak.setVisibility(View.GONE);
        }else{
            fabBreak.setVisibility(View.VISIBLE);
        }
    }
    public void InputArrayListFromDB(){
        int total= ReadTotalWorkout();
        for(int i=1;i<=total;i++){
            for(int j = i; j<=total;j++){
                Cursor resByRep = myDB.getByRepsByWorkoutIDANDListNumber(workoutid_lastinserted_String,j+"");
                Cursor resByTime = myDB.getByTimeByWorkoutIDANDListNumber(workoutid_lastinserted_String,j+"");
                Cursor resRest = myDB.getRestByWorkoutIDANDListNumber(workoutid_lastinserted_String,j+"");
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

    }
    public void DeleteArrayList(){
        name.clear();
        repsecond.clear();
        textrepsecond.clear();
    }
    public void AssignTotalWorkout(){
        totalworkout++;
        txtTotalExercises.setText(totalworkout+"");
    }
    public void ImplementAdapterInput(){
        mWorkoutListInputAdapter = new WorkoutListInputAdapter
                (name,repsecond,textrepsecond,UpdateWorkoutsAct.this,workoutid_lastinserted_String);
        rvUpdateWorkouts.setAdapter(mWorkoutListInputAdapter);
        rvUpdateWorkouts.setLayoutManager(new LinearLayoutManager(UpdateWorkoutsAct.this));
    }
    public void PlusSet(View view){
        int currentset = Integer.parseInt(edtSets.getText().toString());
        currentset=currentset+1;
        edtSets.setText(currentset+"");
    }
    public void MinusSet(View view){
        int currentset = Integer.parseInt(edtSets.getText().toString());
        if(currentset>1){
            currentset=currentset-1;
        }
        edtSets.setText(currentset+"");
    }
    public void ConvertDuration(){
        second = totalsecond%60;
        minute = (totalsecond/60)%60;
        hour = totalsecond/3600;
        duration = String.format(Locale.getDefault(),"%02d:%02d:%02d",hour,minute,second);
        txtDuration.setText(duration);
    }
    public void fabRepClick(){
        fabRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInputRep=true;
                UpdateByRepDialog repDialog = new UpdateByRepDialog(INPUT_WORKOUT,"","",0);
                repDialog.show(getSupportFragmentManager(),"Rep Dialog");
            }
        });
    }
    public void fabTimeClick(){
        fabTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateByTimeDialog byTimeDialog = new UpdateByTimeDialog(INPUT_WORKOUT,"","","");
                byTimeDialog.show(getSupportFragmentManager(),"By Time Dialog");
                onInputTime=true;
            }
        });
    }
    public void fabRestClick(){
        fabRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateRestDialog restDialog = new UpdateRestDialog(INPUT_WORKOUT,"","");
                restDialog.show(getSupportFragmentManager(),"Rest Dialog");
                onInputRest = true;
            }
        });
    }
    public void fabBreakClick(){
        fabBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateBreakDialog breakDialog = new UpdateBreakDialog(INPUT_WORKOUT,"");
                breakDialog.show(getSupportFragmentManager(),"Break Dialog");
                onInputBreak = true;
            }
        });
    }
    public void UpdateBreak(){
        txtUpdateBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadBreak();
                UpdateBreakDialog breakDialog = new UpdateBreakDialog("Update",breaktime);
                breakDialog.show(getSupportFragmentManager(),"Break Dialog");
            }
        });
    }
    public void getLastInserted(){
        Cursor res = myDB.getLastInserted();
        if(res.moveToFirst()){
            workoutid_lastinserted_String = res.getString(0);
            workoutid_lastinserted = Integer.parseInt(workoutid_lastinserted_String);
        }
    }
    public void getUpdateID(){
        workoutid_lastinserted_String = getIntent().getStringExtra("workoutidUPD");
        workoutid_lastinserted = Integer.parseInt(workoutid_lastinserted_String);
    }
    public void DeleteLastInserted(){
        myDB.deleteUnfinishedByRep(workoutid_lastinserted_String);
        myDB.deleteUnfinishedByTime(workoutid_lastinserted_String);
        myDB.deleteUnfinishedRest(workoutid_lastinserted_String);
        myDB.deleteUnfinishedBreak(workoutid_lastinserted_String);
        myDB.deleteUnfinishedWorkouts(workoutid_lastinserted_String);
    }

    public void Save(){
        String name = edtTitle.getText().toString();
        int sets = Integer.parseInt(edtSets.getText().toString());
        if(name.equals("")){
            name = "Untitled";
        }
        if(sets==0 || edtSets.getText().toString().equals("")){
            sets=1;
        }
        totalsecond=totalsecond*sets;
        Log.i("checksecond",totalsecond+"");
        myDB.updateLastInsertedWorkout(workoutid_lastinserted_String,name,totalsecond,sets);

    }
    public void btnSaveClick(View view){
        Save();
        onFinished = true;
        MainActivity.mainAct.finish();
        Intent intent = new Intent(UpdateWorkoutsAct.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void Back(){
        if(!onUpdate){
            TextView mymsg = new TextView(UpdateWorkoutsAct.this);
            mymsg.setText("\nProgress will be deleted, are you sure ?");
            mymsg.setTextColor(ContextCompat.getColor(UpdateWorkoutsAct.this,R.color.colorBlack));
            mymsg.setTextSize(16);
            mymsg.setTypeface(Typeface.DEFAULT_BOLD);
            mymsg.setGravity(Gravity.CENTER);
            new AlertDialog.Builder(UpdateWorkoutsAct.this)
                    .setTitle("Delete Workout")
                    .setView(mymsg)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DeleteLastInserted();
                            finish();
                        }
                    })
                    .setIcon(R.drawable.warningicon)
                    .show();
        }else{
            Save();
            MainActivity.mainAct.finish();
            Intent intent = new Intent(UpdateWorkoutsAct.this,MainActivity.class);
            startActivity(intent);
            finish();
            onFinished = true;
        }
    }
    public void imgBackClick(View view){
        Back();
    }
    //implements
    @Override
    public void insertRest(String seconds,String currentlist) {
        if(onInputRest){
            if(seconds.equals("0")){
                Toast.makeText(UpdateWorkoutsAct.this,"Failed : Insert Valid Time",Toast.LENGTH_SHORT).show();
                onInputRest = false;
            }else{
                myDB.insertrest(Integer.parseInt(seconds.trim()),listnumber,workoutid_lastinserted);
                listnumber++;
                totalsecond = totalsecond+Integer.parseInt(seconds);
                ConvertDuration();
                int current = ReadTotalWorkout();
                if(current==1){
                    DeleteArrayList();
                    InputArrayListFromDB();
                    ImplementAdapterInput();
                }else{
                    mWorkoutListInputAdapter.addItems("Rest",seconds,getString(R.string.seconds));
                }
                onInputRest = false;
            }
        }else{
            if(seconds.equals("0")){
                Toast.makeText(UpdateWorkoutsAct.this,"Failed : Insert Valid Time",Toast.LENGTH_SHORT).show();
                onInputRest = false;
            }else{
                Cursor res = myDB.getRestByWorkoutIDANDListNumber(workoutid_lastinserted_String,currentlist);
                if(res.moveToFirst()){
                    int readsec = Integer.parseInt(res.getString(1));
                    totalsecond = totalsecond-readsec;
                }
                totalsecond = totalsecond+Integer.parseInt(seconds);
                myDB.UpdateRest(workoutid_lastinserted_String,currentlist,Integer.parseInt(seconds));
                ConvertDuration();
                mWorkoutListInputAdapter.updateItems(Integer.parseInt(currentlist)-1
                        ,"Rest",seconds,getString(R.string.seconds));
            }
        }


    }

    @Override
    public void insertByTime(String name, String totalseconds,String min, String sec, String currentlist) {
        int minute = Integer.parseInt(min);
        int second = Integer.parseInt(sec);
        if(onInputTime){
            if(name.equals("")){
                Toast.makeText(UpdateWorkoutsAct.this,"Failed : Insert Workout Name ",Toast.LENGTH_SHORT).show();
                onInputTime = false;
            }else if(minute>=60 ||second >=60 || totalseconds.equals("0")){
                Toast.makeText(UpdateWorkoutsAct.this,"Failed : Insert Valid Time",Toast.LENGTH_SHORT).show();
                onInputTime=false;
            }else{
                myDB.insertbytime(name,Integer.parseInt(totalseconds),listnumber,workoutid_lastinserted);
                listnumber++;
                totalsecond = totalsecond+Integer.parseInt(totalseconds);
                ConvertDuration();
                int current = ReadTotalWorkout();
                if(current==1){
                    DeleteArrayList();
                    InputArrayListFromDB();
                    ImplementAdapterInput();
                }else{
                    mWorkoutListInputAdapter.addItems(name,totalseconds,getString(R.string.seconds));
                }
                AssignTotalWorkout();
                onInputTime = false;
            }
        }else{
            if(name.equals("")){
                Toast.makeText(UpdateWorkoutsAct.this,"Failed : Insert Workout Name ",Toast.LENGTH_SHORT).show();
            }else if(minute>=60 || second>=60 || totalseconds.equals("0")){
                Toast.makeText(UpdateWorkoutsAct.this,"Failed : Insert Valid Time",Toast.LENGTH_SHORT).show();
            }else{
                Cursor res = myDB.getByTimeByWorkoutIDANDListNumber(workoutid_lastinserted_String,currentlist);
                if(res.moveToFirst()){
                    int readsec = Integer.parseInt(res.getString(1));
                    totalsecond = totalsecond-readsec;
                }
                totalsecond = totalsecond+Integer.parseInt(totalseconds);
                myDB.UpdateByTime(workoutid_lastinserted_String,currentlist,name,totalseconds);
                ConvertDuration();
                mWorkoutListInputAdapter.updateItems(Integer.parseInt(currentlist)-1
                        ,name,totalseconds,getString(R.string.seconds));
            }
        }

    }

    @Override
    public void insertByRep(String name, String reps,int currentlist) {
        if(onInputRep){
            if(name.equals("")){
                Toast.makeText(UpdateWorkoutsAct.this,"Failed : Insert Workout Name ",Toast.LENGTH_SHORT).show();
                onInputRep=false;
            }else if(reps.equals("0")){
                Toast.makeText(UpdateWorkoutsAct.this,"Failed : Insert Valid Time",Toast.LENGTH_SHORT).show();
                onInputRep=false;
            }else{
                myDB.insertbyreps(name,Integer.parseInt(reps),listnumber,workoutid_lastinserted);
                listnumber++;
                totalsecond = totalsecond+Integer.parseInt(reps)*2;
                ConvertDuration();
                int current = ReadTotalWorkout();
                if(current==1){
                    DeleteArrayList();
                    InputArrayListFromDB();
                    ImplementAdapterInput();
                }else{
                    mWorkoutListInputAdapter.addItems(name,reps,getString(R.string.reps));
                }
                AssignTotalWorkout();
                onInputRep=false;
            }
        }else{
            if(name.equals("")){
                Toast.makeText(UpdateWorkoutsAct.this,"Failed : Insert Workout Name Update",Toast.LENGTH_SHORT).show();
            }else if(reps.equals("0")){
                Toast.makeText(UpdateWorkoutsAct.this,"Failed : Insert Valid Time Update",Toast.LENGTH_SHORT).show();
            }else{
                Cursor res=myDB.getByRepsByWorkoutIDANDListNumber(workoutid_lastinserted_String,currentlist+"");
                if(res.moveToFirst()){
                    int readrep = Integer.parseInt(res.getString(1));
                    totalsecond = totalsecond-readrep*2;
                }
                totalsecond=totalsecond+Integer.parseInt(reps)*2;
                myDB.UpdateByRep(workoutid_lastinserted_String,currentlist+"",name,Integer.parseInt(reps));
                ConvertDuration();
                mWorkoutListInputAdapter.updateItems(currentlist-1,name,reps,getString(R.string.reps));
            }
        }

    }

    @Override
    public void insertBreak(String totalseconds, String minute, String second) {
        int min = Integer.parseInt(minute);
        int sec = Integer.parseInt(second);
        if(onInputBreak){
            if(totalseconds.equals("0") || min >=60 || sec>=60){
                Toast.makeText(UpdateWorkoutsAct.this,"Failed : Insert Valid Time ",Toast.LENGTH_SHORT).show();
                onInputBreak=false;
            }else{
                myDB.insertbreak(Integer.parseInt(totalseconds),workoutid_lastinserted);
                this.totalsecond = this.totalsecond+ Integer.parseInt(totalseconds);
                ConvertDuration();

                ReadBreak();
                ShowBreakFab();
                onInputBreak = false;
            }
        }else{
            if(totalseconds.equals("0") ||min>=60 || sec>=60){
                Toast.makeText(UpdateWorkoutsAct.this,"Failed : Insert Valid Time ",Toast.LENGTH_SHORT).show();
            }else{
                Cursor res = myDB.getBreakByWorkoutid(workoutid_lastinserted_String);
                if(res.moveToFirst()){
                    int breaksec = Integer.parseInt(res.getString(1));
                    totalsecond = totalsecond-breaksec;
                }
                totalsecond = totalsecond+Integer.parseInt(totalseconds);
                myDB.UpdateBreak(workoutid_lastinserted_String,totalseconds);
                ConvertDuration();
                ReadBreak();
                ShowBreakFab();
            }
        }

    }
    //backpressed
    @Override
    public void onBackPressed() {
        Back();
    }

    @Override
    protected void onStop() {
        if(!onFinished){
            Save();
        }
        super.onStop();
    }

    @Override
    public void DeleteWorkout(String listnumber) {
        Log.i("checkupd",listnumber);
        Cursor res = myDB.getByRepsByWorkoutIDANDListNumber(workoutid_lastinserted_String,listnumber);
        int list = Integer.parseInt(listnumber);
        if(res.moveToFirst()){
            DeleteUpdateByRep(list,listnumber);
            //set text total workout
            this.totalworkout--;
            txtTotalExercises.setText(totalworkout+"");

            //set total time
            int actTime = Integer.parseInt(res.getString(1))*2;
            this.totalsecond = this.totalsecond-actTime;
            this.listnumber--;
        }else{
            res = myDB.getRestByWorkoutIDANDListNumber(workoutid_lastinserted_String,listnumber);
            if(res.moveToFirst()){
                DeleteUpdateRest(list,listnumber);
                //set total time
                int actTime = Integer.parseInt(res.getString(1));
                this.totalsecond = this.totalsecond-actTime;
                this.listnumber--;
            }else{
                res = myDB.getByTimeByWorkoutIDANDListNumber(workoutid_lastinserted_String,listnumber);
                if(res.moveToFirst()){
                    DeleteUpdateByTime(list,listnumber);
                    //set text total workout
                    this.totalworkout--;
                    txtTotalExercises.setText(totalworkout+"");

                    //set total time
                    int actTime = Integer.parseInt(res.getString(1));
                    this.totalsecond = this.totalsecond-actTime;
                    this.listnumber--;
                }
            }
        }

        if(listnumber.equals("0")){
            Log.i("checkupd",listnumber);
            ReadBreak();
            totalsecond = totalsecond-Integer.parseInt(breaktime);
            myDB.deleteBreakByWorkoutid(workoutid_lastinserted_String);
            ReadBreak();
            ShowBreakFab();
        }
        ConvertDuration();
        LoadWorkoutList();
        Toast.makeText(this,"Item Deleted",Toast.LENGTH_SHORT).show();
    }

    public void DeleteBreakClick(){
        imgDeleteBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteWorkoutInputDialog deleteWorkoutInputDialog = new DeleteWorkoutInputDialog("0");
                deleteWorkoutInputDialog.show(getSupportFragmentManager(),"Delete Dialog");
            }
        });

    }

    public void LoadWorkoutList(){
        DeleteArrayList();
        InputArrayListFromDB();
        ImplementAdapterInput();
    }

    public void updateFromDeletedItems(int list){
        for(int i=list+1;i<=name.size();i++){
            int newlist = i-1;
            Cursor read = myDB.getByRepsByWorkoutIDANDListNumber(workoutid_lastinserted_String,i+"");
            if(read.moveToFirst()){
                myDB.UpdateByRepForDelete(workoutid_lastinserted_String,i+"",newlist+"");
            }
            read = myDB.getByTimeByWorkoutIDANDListNumber(workoutid_lastinserted_String,i+"");
            if(read.moveToFirst()){
                myDB.UpdateByTimeForDelete(workoutid_lastinserted_String,i+"",newlist+"");
            }
            read = myDB.getRestByWorkoutIDANDListNumber(workoutid_lastinserted_String,i+"");
            if(read.moveToFirst()){
                myDB.UpdateRestForDelete(workoutid_lastinserted_String,i+"",newlist+"");
            }
        }
    }
    public void DeleteUpdateByRep(int list, String listnumber){
        myDB.deleteByRepByWorkoutid(workoutid_lastinserted_String,listnumber);
        updateFromDeletedItems(list);
    }
    public void DeleteUpdateByTime(int list,String listnumber){
        myDB.deleteByTimeByWorkoutid(workoutid_lastinserted_String,listnumber);
        updateFromDeletedItems(list);
    }
    public void DeleteUpdateRest(int list,String listnumber){
        myDB.deleteRestByWorkoutid(workoutid_lastinserted_String,listnumber);
        updateFromDeletedItems(list);
    }

}
