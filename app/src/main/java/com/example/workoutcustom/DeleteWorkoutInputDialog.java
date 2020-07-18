package com.example.workoutcustom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteWorkoutInputDialog extends DialogFragment {

    private String listnumber;
    private DeleteDialogListener mDeleteDialogListener;

    public DeleteWorkoutInputDialog(String listnumber) {
        this.listnumber = listnumber;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.deleteworkout_input_dialog,null);
        builder.setView(view)
                .setTitle("Delete Workout")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDeleteDialogListener.DeleteWorkout(listnumber);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mDeleteDialogListener = (DeleteDialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement DeleteDialog");
        }
    }

    public interface DeleteDialogListener{
        void DeleteWorkout(String listnumber);
    }
}
