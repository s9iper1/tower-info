package com.byteshaft.towerinfo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GsmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TEst", String.valueOf((NetworkService.getInstance() == null)));
        if (NetworkService.getInstance() == null) {
            Intent intent1 = new Intent(context, NetworkService.class);
            intent1.putExtra(AppGlobals.SEND_BROAD_CAST, true);
            context.startService(intent1);
            NetworkService.getInstance().startLocationUpdate();
        }
        if (NetworkService.getInstance() != null) {
            NetworkService.getInstance().startLocationUpdate();
        }
        AppGlobals.SCHEDULE_STATE = true;
        AppGlobals.CURRENT_STATE = AppGlobals.schedule;
        AlarmHelpers.setAlarm(AppGlobals.ONE_MINUTE*5);

    }
}
