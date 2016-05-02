package com.levibostian.androidblanky.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import com.afollestad.materialdialogs.MaterialDialog;
import com.levibostian.androidblanky.R;

public class DialogUtil {

    private static final int INVALID_STRING_RES = -1;

    public static AlertDialog getSimpleOkDialog(final Activity context, final String title, final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (title != null) {
            builder.setTitle(title);
        }
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, closeDialogListener());

        return builder.create();
    }

    public static MaterialDialog getSingleChoiceRadioListDialog(Activity context, int titleRes, int arrayRes, int positiveButtonRes, MaterialDialog.ListCallbackSingleChoice listener) {
        return new MaterialDialog.Builder(context)
                       .title(titleRes)
                       .items(arrayRes)
                       .alwaysCallSingleChoiceCallback()
                       .itemsCallbackSingleChoice(-1, listener)
                       .positiveText(positiveButtonRes)
                       .build();
    }

    public static MaterialDialog getInputDialog(Activity context, int messageRes, int editTextHint, int positiveTextRes, MaterialDialog.InputCallback listener) {
        return new MaterialDialog.Builder(context)
                       //.title(R.string.input)
                       .content(messageRes)
                       .positiveText(positiveTextRes)
                       .inputType(InputType.TYPE_CLASS_TEXT)
                       .input(editTextHint, R.string.blank, listener).build();
    }

    public static MaterialDialog getCustomViewDialog(Activity context, int layoutRes, int positiveButton) {
        boolean wrapInScrollView = true;

        return new MaterialDialog.Builder(context)
                       //.title(R.string.title)
                       .customView(layoutRes, wrapInScrollView)
                       .positiveText(positiveButton)
                       .build();
    }

    public static AlertDialog getSimpleOkDialog(final Activity context, final String message) {
        return getSimpleOkDialog(context, null, message);
    }

    public static AlertDialog getSimpleOkDialog(final Activity context, final int titleStringRes, final int messageStringRes) {
        return getSimpleOkDialog(context, (titleStringRes == INVALID_STRING_RES) ? null : context.getString(titleStringRes), context.getString(messageStringRes));
    }

    public static AlertDialog getSimpleOkDialog(final Activity context, final int messageStringRes) {
        return getSimpleOkDialog(context, INVALID_STRING_RES, messageStringRes);
    }

    public static DialogInterface.OnClickListener closeDialogListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        };
    }

}
