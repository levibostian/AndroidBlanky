package com.levibostian.androidblanky.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class DialogUtil {

    public static AlertDialog getSimpleOkDialog(final Activity context, final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, closeDialogListener());

        return builder.create();
    }

    public static DialogInterface.OnClickListener closeDialogListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        };
    }

}
