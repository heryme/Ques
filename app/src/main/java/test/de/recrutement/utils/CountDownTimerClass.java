package test.de.recrutement.utils;

import android.os.CountDownTimer;

import test.de.recrutement.activities.MainActivity;

import java.util.concurrent.TimeUnit;

/**
 * Created by Rahul Padaliya on 2/21/2017.
 */
public class CountDownTimerClass extends CountDownTimer {
    long progress;

    public CountDownTimerClass(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        long millis = millisUntilFinished;
        progress = (long) (millisUntilFinished/1000);
        String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

        MainActivity.tv_main_activity_timer.setText(hms + " Min's");

    }
    @Override
    public void onFinish() {
       // MainActivity.tv_main_activity_timer.setText("0/30");
    }
}

