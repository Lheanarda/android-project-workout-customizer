package com.example.workoutcustom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

public class UpdateByTimeDialog extends DialogFragment {
    private EditText edtName,edtMinute,edtSecond;
    private ImageView imgPlusMin,imgMinusMinute,imgPlusSec,imgMinusSecond;
    private int totalSecond,currentMin,currentSec;
    private ByTimeDialogListener mByTimeDialogListener;
    public String act,bytimename,bytimesecond,currentlist;

    public UpdateByTimeDialog(String act, String bytimename, String bytimesecond, String currentlist) {
        this.act = act;
        this.bytimename = bytimename;
        this.bytimesecond = bytimesecond;
        this.currentlist = currentlist;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_bytime_dialog,null);
        builder.setView(view)
                .setTitle(act+" Workout")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(act, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ConvertToSecond();
                        String name = edtName.getText().toString();
                        currentMin = Integer.parseInt(edtMinute.getText().toString());
                        currentSec = Integer.parseInt(edtSecond.getText().toString());
                        mByTimeDialogListener.insertByTime(name,totalSecond+"",currentMin+"",currentSec+"",currentlist);
                    }
                });
        edtName = view.findViewById(R.id.edtNameWorkout);
        edtMinute = view.findViewById(R.id.edtMinuteByTime);
        edtSecond = view.findViewById(R.id.edtSecondByTime);
        imgPlusMin = view.findViewById(R.id.imgPlusMinute);
        imgMinusMinute = view.findViewById(R.id.imgMinMinute);
        imgPlusSec = view.findViewById(R.id.imgPlusSecond);
        imgMinusSecond = view.findViewById(R.id.imgMinSecond);

        //method
        setText();
        PlusMinute();
        MinusMinute();
        PlusSecond();
        MinusSecond();
        return builder.create();
    }

    public void setText(){
        if(!bytimename.equals("")){
            edtName.setText(bytimename);
        }
        if(!bytimesecond.equals("")){
            int total = Integer.parseInt(bytimesecond);
            int min = total/60;
            int sec = total%60;
            String durationmin = String.format(Locale.getDefault(),"%02d",min);
            String durationsec = String.format(Locale.getDefault(),"%02d",sec);
            edtMinute.setText(durationmin);
            edtSecond.setText(durationsec);
        }
    }
    public void PlusMinute(){
        currentMin = Integer.parseInt(edtMinute.getText().toString());
        imgPlusMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMin = currentMin +1;
                edtMinute.setText(currentMin+"");
            }
        });
    }
    public void MinusMinute(){
        currentMin = Integer.parseInt(edtMinute.getText().toString());
        imgMinusMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentMin>0){
                    currentMin = currentMin -1;
                    edtMinute.setText(currentMin+"");
                }
            }
        });
    }
    public void PlusSecond(){
        currentSec = Integer.parseInt(edtSecond.getText().toString());
        imgPlusSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSec = currentSec +1;
                edtSecond.setText(currentSec+"");
            }
        });
    }
    public void MinusSecond(){
        currentSec = Integer.parseInt(edtSecond.getText().toString());
        imgMinusSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentSec>0){
                    currentSec = currentSec -1;
                    edtSecond.setText(currentSec+"");
                }
            }
        });
    }
    public void ConvertToSecond(){
        int min = Integer.parseInt(edtMinute.getText().toString());
        int sec = Integer.parseInt(edtSecond.getText().toString());
        totalSecond = min*60+sec;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mByTimeDialogListener = (ByTimeDialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement ByTimeDialogListener");
        }

    }

    public interface ByTimeDialogListener{
        void insertByTime(String name,String totalseconds,String min, String sec,String currentlist);
    }
}
