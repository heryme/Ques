package test.de.recrutement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import test.de.recrutement.activities.MainActivity;
import test.de.recrutement.activities.SplashActivity;
import test.de.recrutement.activities.SplashActivity_;

/**
 * Created by Rahul Padaliya on 2/21/2017.
 */
public class InstallReferrerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        Log.d("referrer", "--->" + referrer);
        Intent redirect = new Intent(context,SplashActivity_.class);
        redirect.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        redirect.putExtra("data",referrer);
        context.startActivity(redirect);


        //Use the referre {
        //Run Using Adb
        //am broadcast -a com.android.vending.INSTALL_REFERRER -n com.deeplinkingdermo/.InstallReferrerReceiver --es "referrer" "utm_source=test_source\&utm_medium=test_medium\&utm_term=test_term\&utm_content=test_content\&utm_campaign=test_name"

    }
}
