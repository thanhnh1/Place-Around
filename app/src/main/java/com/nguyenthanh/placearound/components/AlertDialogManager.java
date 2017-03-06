package com.nguyenthanh.placearound.components;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.nguyenthanh.placearound.R;

public class AlertDialogManager {

    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(title);
        // Setting Dialog Message
        alertDialog.setMessage(message);
        if (status != null) {
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.ic_dialog_success : R.drawable.ic_dialog_fail);
        }
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
}