package test.de.recrutement.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import test.de.recrutement.R;
import test.de.recrutement.interfaces.INTFAlertOk;


/**
 * Created by Ankur on 01-12-2015.
 */
public class DialogUtility {

    private static final String TAG = DialogUtility.class.getSimpleName();

    private static final String SUCCESS_MESSAGE = "Records submitted successfully.";

    private static final String ERROR_MESSAGE = "Oops !! Please try again later";

    public static final String PREFERENCE_NAME = "MitMoldPref";

    private static SharedPreferences sharedPreferences;


    public static ProgressDialog processDialog(final Context context, final String message, final boolean isCancelable) {
        final ProgressDialog dialog = new ProgressDialog(context);
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    dialog.setMessage(message);
                    dialog.setCancelable(isCancelable);
                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
        return dialog;
    }

    public static void dismissProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    /**
     * AlertDialog which is override ok click
     * using interface
     * @param context
     * @param message
     * @param intfAlertOk
     * @return
     */
    public static Dialog alertOk(Context context,
                                 String message,
                                 String btnTitle,
                                 final INTFAlertOk intfAlertOk) {
       AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);
        builder.setMessage(message);
        builder.setCancelable(false);
        String positiveText = context.getString(R.string.OK);/*getString(android.R.string.yes);*/
        builder.setPositiveButton(btnTitle,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        intfAlertOk.alertOk();
                    }
                });
       AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
        return dialog;
    }
}
