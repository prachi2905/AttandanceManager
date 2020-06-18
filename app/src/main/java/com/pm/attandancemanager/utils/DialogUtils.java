package com.pm.attandancemanager.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pm.attandancemanager.R;


/**
 * Created by above on 01/03/17.
 * Dialog utils for showing progress dialog, Toast, SnackBar etc...
 */

@SuppressWarnings("ConstantConditions")
public class DialogUtils {
    private static ProgressBar progressBar;

    public static void appDialog(final Activity activity, String title, String Message) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
        alertbox.setTitle(title);
        alertbox.setMessage(Message);
        alertbox.setPositiveButton("Ok", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        alertbox.show();
    }

    public static void appDialogWithCallBack(final Activity activity, String title, String Message, final OnClickListener listener) {
        onClickListener = listener;
        AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
        alertbox.setTitle(title);
        alertbox.setMessage(Message);
        alertbox.setPositiveButton("Logout", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        onClickListener.onOk();
                    }
                });
        alertbox.setNegativeButton("Cancel", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        onClickListener.onCancel();
                        dialog.dismiss();
                    }
                });

        alertbox.show();
    }

    private static OnClickListener onClickListener;

    public interface OnClickListener {
        public void onOk();

        public void onCancel();
    }

    public static void appDialog(final Activity activity, String Message) {
        appDialog(activity, "MyFarm", Message);
    }

    public static void appToast(final Context context, String Message) {
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }

    public static void appToastLong(final Activity activity, String Message) {
        Toast.makeText(activity, Message, Toast.LENGTH_LONG).show();
    }

    public static void showProgressDialog(Context context, String message) {
        progressBar = new ProgressBar(context);
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void dismissProgressDialog() {
        if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
        }
    }

    public static void appSnakeBar(final Activity activity, String message) {
        Snackbar sb = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        sb.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.colorAccent));
        sb.show();
    }

    public interface DelayCallback {
        void afterDelay();
    }

    public static void delay(int secs, final DelayCallback delayCallback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, secs * 1000); // afterDelay will be executed after (secs*1000) milliseconds.
    }
}
