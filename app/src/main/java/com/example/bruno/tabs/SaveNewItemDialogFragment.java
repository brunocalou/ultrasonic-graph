package com.example.bruno.tabs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by bruno on 18/09/2016.
 */
public class SaveNewItemDialogFragment extends DialogFragment {

    public interface SaveNewItemDialogListener {
        void onFinishSaveNewItemDialog(String name);
    }

    public SaveNewItemDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        String title = getArguments().getString("title");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_save_new, null))
                // Add action buttons
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        sendBackResult();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setTitle(getResources().getString(R.string.dialog_save_title));
        return builder.create();
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        SaveNewItemDialogListener listener = (SaveNewItemDialogListener) getTargetFragment();
        listener.onFinishSaveNewItemDialog(((EditText)getDialog().findViewById(R.id.name)).getText().toString());
        dismiss();
    }
}
