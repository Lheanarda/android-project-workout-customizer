package com.example.workoutcustom;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PickedWorkoutsAdapter extends RecyclerView.Adapter<PickedWorkoutsAdapter.MyViewHolder> {

    ArrayList<String> name,repsecond,textrepsecond;
    String workoutid;
    Context mContext;

    public PickedWorkoutsAdapter(ArrayList name, ArrayList repsecond, ArrayList textrepsecond,  Context context, String workoutid) {
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
        View view = inflater.inflate(R.layout.workoutlist,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int list = position+1;
        DatabaseHelper myDB = new DatabaseHelper(mContext,null,null,1);
        Cursor res = myDB.getByRepsByWorkoutIDANDListNumber(workoutid,list+"");
        if(res.moveToFirst()){
            holder.workoutlistrow.setBackgroundResource(R.drawable.bglistworkout_v2);
        }
        res = myDB.getByTimeByWorkoutIDANDListNumber(workoutid,list+"");
        if(res.moveToFirst()){
            holder.workoutlistrow.setBackgroundResource(R.drawable.bglistworkout_v2);
        }

        res = myDB.getRestByWorkoutIDANDListNumber(workoutid,list+"");
        if(res.moveToFirst()){
            holder.workoutlistrow.setBackgroundResource(R.drawable.bglistworkout);
        }

        res =myDB.getBreakByWorkoutid(workoutid);
        if(res.moveToFirst() && list==name.size()){
            holder.workoutlistrow.setBackgroundResource(R.drawable.bglistworkout_v3);
        }
         holder.txtlistname.setText(name.get(position));
         holder.txtRepSecond.setText(repsecond.get(position));
         holder.textViewRepSecond.setText(textrepsecond.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView txtlistname,txtRepSecond,textViewRepSecond;
        LinearLayout workoutlistrow;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutlistrow=itemView.findViewById(R.id.workoutlistrow);
            txtlistname = itemView.findViewById(R.id.txtlistname);
            txtRepSecond = itemView.findViewById(R.id.txtRepSecond);
            textViewRepSecond = itemView.findViewById(R.id.textViewRepSecond);
        }
    }
}
