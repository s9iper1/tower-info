package com.byteshaft.towerinfo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.TimeUnit;


public class AlarmHelpers extends Service {

    private static AlarmManager mAlarmManager;
    private static PendingIntent mPendingIntent;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setAlarm(AppGlobals.ONE_MINUTE * 5);
        Notification.Builder notification = new Notification.Builder(getApplicationContext());
        notification.setTicker("Test");
        notification.setContentText("Test");
        notification.setSmallIcon(R.drawable.ic_launcher);
        startForeground(2112, notification.build());
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(getApplicationContext(), AlarmHelpers.class));
    }

    public static void setAlarm(long time) {
        mAlarmManager = getAlarmManager(AppGlobals.getContext());
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
