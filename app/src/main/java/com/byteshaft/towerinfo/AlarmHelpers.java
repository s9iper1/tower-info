package com.byteshaft.towerinfo;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.TimeUnit;


public class AlarmHelpers extends IntentService {

    private static AlarmManager mAlarmManager;
    private static PendingIntent mPendingIntent;
    final int ONE_SECOND = 1000;
    final int ONE_MINUTE = ONE_SECOND * 60;
    final int TEN_MINUTES = ONE_MINUTE * 10;
    final int THIRTY_MINUTES= TEN_MINUTES * 3;

    public AlarmHelpers() {
        super("AlarmHelpers");
    }

    private static void setAlarm(long time) {
        Log.i("Alarm",
                String.format("Setting alarm for: %d", TimeUnit.MILLISECONDS.toMinutes(time)));
        Intent intent = new Intent("com.byteshaft.gsmDetails");
        mPendingIntent = PendingIntent.getBroadcast(AppGlobals.getContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT < 19) {
            mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()
                    + time, mPendingIntent);
        } else if(Build.VERSION.SDK_INT < 23){
            mAlarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()
                    + time, mPendingIntent);
        } else{
            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + time, mPendingIntent);
        }
    }

    private static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mAlarmManager = getAlarmManager(AppGlobals.getContext());
        setAlarm(ONE_MINUTE * 5);
    }
}
