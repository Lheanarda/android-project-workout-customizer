package com.example.workoutcustom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyWorkoutsAdapter extends RecyclerView.Adapter<MyWorkoutsAdapter.MyViewHolder> {
    ArrayList<String> workoutNames;
    ArrayList<Integer> totalTime,sets,workoutid;
    Context mContext;
    private int currentposition;
    public MyWorkoutsAdapter(Context context,ArrayList<Integer> workoutid,ArrayList<String> workoutName,ArrayList<Integer> totalTime,ArrayList<Integer> sets){
        mContext = context;
        this.workoutid = workoutid;
        this.workoutNames = workoutName;
        this.totalTime = totalTime;
        this.sets = sets;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.workouts_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DatabaseHelper myDB=new DatabaseHelper(mContext,null,null,1);
        //set text
        int second = totalTime.get(position);
        int minute = Math.round(second/60);
        holder.txtWorkoutName.setText(workoutNames.get(position));
        holder.txtTotalSets.setText(sets.get(position).toString());
        holder.txtTotalTime.setText(minute+"");

        //codes  animation
        holder.layoutrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PickedWorkoutsAct.class);
                intent.putExtra("workoutidKEY",workoutid.get(position).toString());
                mContext.startActivity(intent);
            }
        });

        holder.imgEdit.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,UpdateWorkoutsAct.class);
                intent.putExtra("workoutidUPD",workoutid.get(position).toString());
                intent.putExtra("update",true);
                mContext.startActivity(intent);
            }
        }));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView mymsg = new TextView(mContext);
                mymsg.setText("\nAre you sure ?");
                mymsg.setTextColor(ContextCompat.getColor(mContext,R.color.colorBlack));
                mymsg.setTextSize(18);
                mymsg.setTypeface(Typeface.DEFAULT_BOLD);
                mymsg.setGravity(Gravity.CENTER);
                new AlertDialog.Builder(mContext)
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
                                myDB.deleteUnfinishedByRep(workoutid.get(position).toString());
                                myDB.deleteUnfinishedByTime(workoutid.get(position).toString());
                                myDB.deleteUnfinishedRest(workoutid.get(position).toString());
                                myDB.deleteUnfinishedBreak(workoutid.get(position).toString());
                                myDB.deleteUnfinishedWorkouts(workoutid.get(position).toString());
                                Intent intent = new Intent(mContext,MainActivity.class);
                                mContext.startActivity(intent);
                                ((Activity)mContext).finish();
                            }
                        })
                        .setIcon(R.drawable.warningicon)
                        .show();


            }
        });

    }

    @Override
    public int getItemCount() {

        return workoutNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtWorkoutName,txtTotalTime,txtTotalSets;
        LinearLayout layoutrow;
        ImageView imgEdit,imgDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtWorkoutName = itemView.findViewById(R.id.txtWorkoutName);
            txtTotalTime = itemView.findViewById(R.id.txtTotalTime);
            txtTotalSets = itemView.findViewById(R.id.txtTotalSets);
            layoutrow = itemView.findViewById(R.id.layoutrow);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
