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

public class UpdateRestDialog extends DialogFragment {
    private EditText edtSecondRest;
    private ImageView imgPlus,imgMinus;
    private RestDialogListener mRestDialogListener;
    private int currentNumber;
    private String act,seconds,currentlist;

    public UpdateRestDialog( String act, String seconds, String currentlist) {
        this.act = act;
        this.seconds = seconds;
        this.currentlist = currentlist;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_rest_dialog,null);
        builder.setView(view)
                .setTitle(act+" Rest")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(act, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String seconds = edtSecondRest.getText().toString();
                        mRestDialogListener.insertRest(seconds,currentlist);
                    }
                });
        edtSecondRest= view.findViewById(R.id.edtSecondRest);
        imgPlus = view.findViewById(R.id.imgPlus);
        imgMinus = view.findViewById(R.id.imgMinus);

        //method
        setText();
        PlusSeconds();
        MinusSeconds();
        return builder.create();
    }

    public void setText(){
        if(!seconds.equals("")){
            edtSecondRest.setText(seconds);
        }
    }

    public void PlusSeconds(){
        currentNumber = Integer.parseInt(edtSecondRest.getText().toString());
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentNumber = currentNumber+1;
                edtSecondRest.setText(currentNumber+"");
            }
        });
    }

    public void MinusSeconds(){
        currentNumber = Integer.parseInt(edtSecondRest.getText().toString());
        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentNumber >0){
                    currentNumber= currentNumber -1;
                    edtSecondRest.setText(currentNumber+"");
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mRestDialogListener = (RestDialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement RestDialogListener");
        }

    }

    public interface RestDialogListener{
        void insertRest(String seconds,String currentlist);
    }
}
