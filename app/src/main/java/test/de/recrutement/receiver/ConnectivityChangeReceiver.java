package test.de.recrutement.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import test.de.recrutement.background_services.UploadData;
import test.de.recrutement.utils.AndroidUtils;

/**
 * Created by Rahul Padaliya on 4/11/2017.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ComponentName comp = new ComponentName(context.getPackageName(),
                UploadData.class.getName());

        //intent.putExtra("isNetworkConnected",isConnected(context));
        if(isConnected(context)){
            //Toast.makeText(context,"From Broadcast Connected",Toast.LENGTH_LONG).show();
            Log.d("TAG","From Broadcast Connected---->");

            if(AndroidUtils.getImageZipFile() != null && AndroidUtils.getImageZipFile().exists()) {
                context.startService(intent.setComponent(comp));
            }

        }else{
            //Toast.makeText(context,"From Broadcast Not Connected",Toast.LENGTH_LONG).show();
            Log.d("TAG","From Broadcast Not Connected---->");
        }

    }

    public  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
