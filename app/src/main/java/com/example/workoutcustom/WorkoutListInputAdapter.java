package com.example.workoutcustom;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutListInputAdapter extends RecyclerView.Adapter<WorkoutListInputAdapter.MyViewHolder> {

    ArrayList<String> name,repsecond,textrepsecond;
    String workoutid;
    Context mContext;
    String listnumb;
    String UPDATE_WORKOUT = "Update";
    public WorkoutListInputAdapter(ArrayList name, ArrayList repsecond, ArrayList textrepsecond,  Context context, String workoutid) {
        this.name = name;
        this.repsecond = repsecond;
        this.textrepsecond = textrepsecond;
        this.workoutid = workoutid;
        mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.workoutlist_input,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int list = position+1;
        listnumb = list+"";
        final DatabaseHelper myDB = new DatabaseHelper(mContext,null,null,1);
        Cursor resRep = myDB.getByRepsByWorkoutIDANDListNumber(workoutid,list+"");
        Cursor resTime = myDB.getByTimeByWorkoutIDANDListNumber(workoutid,list+"");
        Cursor resRest = myDB.getRestByWorkoutIDANDListNumber(workoutid,list+"");
//        Cursor resBreak =myDB.getBreakByWorkoutid(workoutid);
        if(resRep.moveToFirst()){
            //set background
            holder.workoutlistrow.setBackgroundResource(R.drawable.bglistworkout_v2);
            //update
            final Cursor finalRes = resRep;
            holder.workoutlistrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateByRepDialog repDialog = new UpdateByRepDialog(UPDATE_WORKOUT
                            ,finalRes.getString(0),finalRes.getString(1),list);
                    repDialog.show(((FragmentActivity)mContext).getSupportFragmentManager(),"Rep Dialog");
                }
            });

        }else if(resTime.moveToFirst()){
            holder.workoutlistrow.setBackgroundResource(R.drawable.bglistworkout_v2);
            final Cursor finalRes2 = resTime;
            holder.workoutlistrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateByTimeDialog byTimeDialog = new UpdateByTimeDialog(UPDATE_WORKOUT, finalRes2.getString(0),
                            finalRes2.getString(1),list+"");
                    byTimeDialog.show(((FragmentActivity)mContext).getSupportFragmentManager(),"ByTime Dialog");
                }
            });
        }
        else if(resRest.moveToFirst()){
            holder.workoutlistrow.setBackgroundResource(R.drawable.bglistworkout);
            final Cursor finalRes1 = resRest;
            holder.workoutlistrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateRestDialog restDialog = new UpdateRestDialog
                            (UPDATE_WORKOUT, finalRes1.getString(1),list+"");
                    restDialog.show(((FragmentActivity)mContext).getSupportFragmentManager(),"Rest Dialog");
                }
            });
        }
//        else if(resBreak.moveToFirst() && list==name.size()){
//            Log.i("break","problem here");
//            holder.workoutlistrow.setBackgroundResource(R.drawable.bglistworkout_v3);
//            final Cursor finalRes3 = resBreak;
//            holder.workoutlistrow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    UpdateBreakDialog breakDialog = new UpdateBreakDialog(UPDATE_WORKOUT, finalRes3.getString(1));
//                    breakDialog.show(((FragmentActivity)mContext).getSupportFragmentManager(),"Break Dialog");
//                }
//            });
//        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteWorkoutInputDialog deleteWorkoutInputDialog = new DeleteWorkoutInputDialog(list+"");
                deleteWorkoutInputDialog.show(((FragmentActivity)mContext).getSupportFragmentManager(),"Delete Dialog");
            }
        });

        holder.txtlistname.setText(name.get(position));
        holder.txtRepSecond.setText(repsecond.get(position));
        holder.textViewRepSecond.setText(textrepsecond.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public boolean haveBreak(){
        DatabaseHelper myDB = new DatabaseHelper(mContext,null,null,1);
        Cursor res = myDB.getBreakByWorkoutid(workoutid);
        if(res.moveToFirst()) return  true;
        else return false;
    }

    public void addItems(String addName,String addRepSecond, String addText){
            name.add(addName);
            repsecond.add(addRepSecond);
            textrepsecond.add(addText);
            notifyItemInserted(name.size()+1);
        Log.i("break",haveBreak()+"");
    }

    public void updateItems(int position,String updName, String updRepSecond, String updText){
        name.set(position,updName);
        repsecond.set(position,updRepSecond);
        textrepsecond.set(position,updText);
        notifyItemChanged(position);
    }

    public void updateBreak(String updName, String updRepSecond, String updText){
        int position = name.size()-1;
        name.set(position,updName);
        repsecond.set(position,updRepSecond);
        textrepsecond.set(position,updText);
        notifyItemChanged(position);
    }


    public class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView txtlistname,txtRepSecond,textViewRepSecond;
        LinearLayout workoutlistrow;
        ImageView imgDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDelete=itemView.findViewById(R.id.imgDeleteInput);
            workoutlistrow=itemView.findViewById(R.id.workoutlistrow);
            txtlistname = itemView.findViewById(R.id.txtlistname);
            txtRepSecond = itemView.findViewById(R.id.txtRepSecond);
            textViewRepSecond = itemView.findViewById(R.id.textViewRepSecond);
        }
    }
}
