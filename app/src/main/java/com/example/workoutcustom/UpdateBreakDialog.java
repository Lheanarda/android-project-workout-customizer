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

public class UpdateBreakDialog extends DialogFragment {
    private EditText edtMinute,edtSecond;
    private ImageView imgPlusMinute,imgMinusMinute,imgPlusSecond,imgMinusSecond;
    private BreakDialogListener mBreakDialogListener;
    private int currentmin,currentsec;
    private String act,seconds;

    public UpdateBreakDialog(String act, String seconds) {
        this.act = act;
        this.seconds = seconds;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_break_dialog,null);
        builder.setView(view)
                .setTitle(act+" Break")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(act, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentmin = Integer.parseInt(edtMinute.getText().toString());
                        currentsec = Integer.parseInt(edtSecond.getText().toString());
                        int totalSec = currentsec+currentmin*60;
                        mBreakDialogListener.insertBreak(totalSec+"",currentmin+"",currentsec+"");
                    }
                });
        edtMinute=view.findViewById(R.id.edtMinuteBreak);
        edtSecond=view.findViewById(R.id.edtSecondBreak);
        imgPlusMinute=view.findViewById(R.id.imgPlusMinBreak);
        imgPlusSecond = view.findViewById(R.id.imgPlusSecBreak);
        imgMinusMinute = view.findViewById(R.id.imgMinusMinBreak);
        imgMinusSecond = view.findViewById(R.id.imgMinusSecBreak);

        //method
        setText();
        PlusMin();
        PlusSec();
        MinusMin();
        MinusSec();
        return builder.create();
    }

    public void setText(){
        if(!seconds.equals("")){
            int total= Integer.parseInt(seconds);
            int min = total/60;
            int sec = total%60;
            String durmin = String.format(Locale.getDefault(),"%02d",min);
            String dursec = String.format(Locale.getDefault(),"%02d",sec);
            edtMinute.setText(durmin);
            edtSecond.setText(dursec);
        }
    }

    public void PlusMin(){
        currentmin = Integer.parseInt(edtMinute.getText().toString());
        imgPlusMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentmin ++;
                edtMinute.setText(currentmin+"");
            }
        });
    }

    public void MinusMin(){
        currentmin = Integer.parseInt(edtMinute.getText().toString());
        imgMinusMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentmin>0){
                    currentmin=currentmin-1;
                    edtMinute.setText(currentmin+"");
                }
            }
        });
    }

    public void PlusSec(){
        currentsec = Integer.parseInt(edtSecond.getText().toString());
        imgPlusSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentsec++;
                edtSecond.setText(currentsec+"");
            }
        });
    }

    public void MinusSec(){
        currentsec = Integer.parseInt(edtSecond.getText().toString());
        imgMinusSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentsec>0){
                    currentsec--;
                    edtSecond.setText(currentsec+"");
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mBreakDialogListener = (BreakDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement BreakDialogListener");
        }
    }

    public interface BreakDialogListener{
        void insertBreak(String totalseconds,String minute, String second);
    }

}
