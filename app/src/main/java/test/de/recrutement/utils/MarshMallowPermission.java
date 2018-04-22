package test.de.recrutement.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import test.de.recrutement.R;


/**
 * Created by HirenK on 06/06/16.
 */
public class MarshMallowPermission {

    public static final int RECORD_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 3;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4;
    public static final int ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE = 5;
    public static final int ACCESS_COARSE_LOCATION_PERMISSION_REQUEST_CODE = 6;
    public static final int READ_CONTACTS_PERMISSION_REQUEST_CODE = 7;
    public static final int ACCESS_ACCOUNT_PERMISSION_REQUEST_CODE = 8;
    public static final int ACCESS_CALL_PHONE_PERMISSION_REQUEST_CODE = 9;
    private static final int REQUEST_PERMISSION_SETTING = 123;

    Activity activity;

    public MarshMallowPermission(Activity activity) {
        this.activity = activity;
    }

    public static boolean checkPermissionForRecord(Activity activity) {

        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForExternalStorage(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForReadExternalStorage(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForCamera(Context activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForAccessFineLocation(Context activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForAccessCrossLocation(Context activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForReadContacts(Context activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForGetAccounts(Context activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean checkPermissionForPhoneCall(Context activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static void requestPermissionForPhoneCall(Context activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity, Manifest.permission.CALL_PHONE)) {
            Toast.makeText(activity, activity.getString(R.string.requestPermissionForPhoneCall), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions((Activity) activity, new String[]{Manifest.permission.CALL_PHONE}, ACCESS_CALL_PHONE_PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestPermissionForGetAccounts(Context activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity, Manifest.permission.GET_ACCOUNTS)) {
            Toast.makeText(activity, activity.getString(R.string.requestPermissionForGetAccounts), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions((Activity) activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestPermissionForReadContacts(Context activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity, Manifest.permission.READ_CONTACTS)) {
            Toast.makeText(activity, activity.getString(R.string.requestPermissionForReadContacts), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions((Activity) activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestPermissionForAccessFineLocation(Context activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(activity, activity.getString(R.string.requestPermissionForAccessFineLocation), Toast.LENGTH_LONG).show();
            redirectToSettings(activity);
        } else {
            ActivityCompat.requestPermissions((Activity) activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestPermissionForAccessCrossLocation(Context activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Toast.makeText(activity,activity.getString(R.string.requestPermissionForAccessCrossLocation), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions((Activity) activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    public static void requestPermissionForRecord(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(activity, activity.getString(R.string.requestPermissionForRecord), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestPermissionForExternalStorage(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity, activity.getString(R.string.requestPermissionForExternalStorage), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestPermissionForReadExternalStorage(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(activity, activity.getString(R.string.requestPermissionForReadExternalStorage), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestPermissionForCamera(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            Toast.makeText(activity, activity.getString(R.string.requestPermissionForCamera), Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    public static void redirectToSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        ((Activity)context).startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }
}
