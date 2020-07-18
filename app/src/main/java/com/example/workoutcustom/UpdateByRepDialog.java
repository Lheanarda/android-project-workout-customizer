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

public class UpdateByRepDialog extends DialogFragment {
    private EditText edtRep,edtName;
    private ImageView imgPlusRep,imgMinusRep;
    private int currentRep,currentlist;
    private ByRepDialogListener mByRepDialogListener;
    private String name,rep,act;

    public UpdateByRepDialog(String act,String name, String rep,int currentlist) {
        this.act = act;
        this.name = name;
        this.rep = rep;
        this.currentlist = currentlist;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_byrep_dialog,null);
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
                        String name = edtName.getText().toString();
                        currentRep = Integer.parseInt(edtRep.getText().toString());
                        mByRepDialogListener.insertByRep(name,currentRep+"",currentlist);
                    }
                });
        edtName = view.findViewById(R.id.edtByRepName);
        edtRep = view.findViewById(R.id.edtRep);
        imgPlusRep = view.findViewById(R.id.imgPlusRep);
        imgMinusRep = view.findViewById(R.id.imgMinusRep);
        //method
        setText();
        PlusRep();
        MinusRep();
        return builder.create();
    }

    public void setText(){
        if(!name.equals("")){
            edtName.setText(name);
        }
        if(!rep.equals("")){
            edtRep.setText(rep);
        }
    }
    public void PlusRep (){
        currentRep = Integer.parseInt(edtRep.getText().toString());
        imgPlusRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentRep = currentRep+1;
                edtRep.setText(currentRep+"");
            }
        });
    }
    public void MinusRep(){
        currentRep = Integer.parseInt(edtRep.getText().toString());
        imgMinusRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentRep>0){
                    currentRep = currentRep-1;
                    edtRep.setText(currentRep+"");
                }
            }
        });
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mByRepDialogListener=(ByRepDialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement ByRepDialogListener");
        }
    }

    public interface ByRepDialogListener{
        void insertByRep(String name,String reps,int listNumber);
    }
}
