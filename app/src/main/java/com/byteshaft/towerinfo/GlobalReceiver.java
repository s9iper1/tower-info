package com.byteshaft.towerinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;


public class GlobalReceiver extends BroadcastReceiver {

    private static Context mContext;
    public static boolean wifiAction;
    private static final String TAG = "GlobalReceiver";
    private static boolean dataState = false;
    private static boolean inComingCall = false;
    private static boolean outGoingCall = false;
    private static boolean calledAttended = false;
    private static boolean calledOnce = false;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Receiver Called");
        mContext = context;
        if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
            wifiAction = true;
        }
        startSignalLevelListener(mContext);

    }


    public static void startSignalLevelListener(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressWarnings("deprecation")
        int events = PhoneStateListener.LISTEN_SIGNAL_STRENGTH |
                PhoneStateListener.LISTEN_DATA_ACTIVITY |
                PhoneStateListener.LISTEN_CELL_LOCATION |
                PhoneStateListener.LISTEN_CALL_STATE |
                PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR |
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                PhoneStateListener.LISTEN_SERVICE_STATE;
        tm.listen(phoneStateListener, events);
    }

    static PhoneStateListener phoneStateListener = new PhoneStateListener() {

        boolean connection = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            String phoneState = "UNKNOWN";
            AppGlobals.IS_CALL_DROPPED = true;
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    AppGlobals.IS_CALL_DROPPED = false;
                    phoneState = "IDLE";
                    if (inComingCall && calledAttended || outGoingCall) {
                        if (NetworkService.getInstance() != null) {
                            Log.i("IDLE: ", "state idle ");
                            AppGlobals.CURRENT_STATE = AppGlobals.call_dropped;
                            NetworkService.getInstance().startLocationUpdate();
                            inComingCall = false;
                            outGoingCall = false;
                            calledAttended = false;
                        }
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    inComingCall = true;
                    Log.i("Ringing: ", "New Phone Call Event. Incomming Number : " + incomingNumber);
                    phoneState = "Ringing (" + incomingNumber + ") ";
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    phoneState = "Offhook";
                    if (inComingCall) {
                        calledAttended = true;
                    }
                    outGoingCall = true;
                    Log.i("OFFHOOK: ", "New Phone Call Event. Incomming Number : " + incomingNumber);
                    break;

            }
            if (AppGlobals.APP_FOREGROUND) {
                PhoneInfo.getInstance().callState_info.setText(phoneState);
            }
//            setTextViewText(info_ids[INFO_CALL_STATE_INDEX], phoneState);

        }

        @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
            String strLocation = location.toString();
            if (AppGlobals.APP_FOREGROUND) {
                PhoneInfo.getInstance().cellLocation_info.setText(strLocation);
            }
            if (AppGlobals.APP_FOREGROUND) {
                PhoneInfo.getInstance().cellLocation_info.setText(strLocation);
            }
        }

        /*
         * Cellphone data connection status
         * */

        @Override
        public void onDataConnectionStateChanged(int state, int networkType) {
            super.onDataConnectionStateChanged(state, networkType);
            String phoneState = "UNKNOWN";
            switch (state) {

                case TelephonyManager.DATA_DISCONNECTED:
                    if (NetworkService.getInstance() == null) {
                        mContext.startService(new Intent(mContext.getApplicationContext(), NetworkService.class));
                    }
                    Log.i("TAG", "onDataConnectionStateChanged" + AppGlobals.IS_CALL_DROPPED);
                    if (NetworkService.getInstance() != null) {
                        if (dataState || !wifiAction) {
                            if (!AppGlobals.IS_CALL_DROPPED) {
                                AppGlobals.CURRENT_STATE = AppGlobals.suspend;
                                NetworkService.getInstance().startLocationUpdate();
                                dataState = false;
                            }
                        }
                    }
                    Log.i(TAG, "onDataConnectionStateChanged: DATA_DISCONNECTED");
                    phoneState = "Disconnected";
                    break;
                case TelephonyManager.DATA_CONNECTING:
                    Log.i(TAG, "onDataConnectionStateChanged: DATA_CONNECTING");
                    phoneState = "Connecting..";
                    break;
                case TelephonyManager.DATA_CONNECTED:
                    dataState = true;
                    phoneState = "Connected";
                    Log.i(TAG, "onDataConnectionStateChanged: DATA_CONNECTED");
                    break;
                case TelephonyManager.DATA_SUSPENDED:
                    if (dataState || !wifiAction) {
                        phoneState = "Suspended";
                        Log.i(TAG, "onDataConnectionStateChanged: DATA_SUSPENDED");
                        if (!AppGlobals.IS_CALL_DROPPED) {
                            AppGlobals.CURRENT_STATE = AppGlobals.suspend;
                            NetworkService.getInstance().startLocationUpdate();
                        }
                    }
                    break;
                default:
                    Log.w(TAG, "onDataConnectionStateChanged: UNKNOWN " + state);
                    if (dataState || !wifiAction) {
                        if (!AppGlobals.IS_CALL_DROPPED) {
                            AppGlobals.CURRENT_STATE = AppGlobals.suspend;
                            NetworkService.getInstance().startLocationUpdate();
                        }
                    }
                    break;

            }
            if (AppGlobals.APP_FOREGROUND) {
                PhoneInfo.getInstance().connectionState_info.setText(phoneState);
            }
//            setTextViewText(info_ids[INFO_CONNECTION_STATE_INDEX], phoneState);

        }

        @Override
        public void onDataActivity(int direction) {
            super.onDataActivity(direction);
            String strDirection = "NONE";
            switch (direction) {

                case TelephonyManager.DATA_ACTIVITY_IN:
                    strDirection = "IN";
                    break;
                case TelephonyManager.DATA_ACTIVITY_INOUT:
                    strDirection = "IN-OUT";
                    new CheckInternet().execute();
                    break;
                case TelephonyManager.DATA_ACTIVITY_DORMANT:
                    strDirection = "Dormant";
                    break;
                case TelephonyManager.DATA_ACTIVITY_NONE:
                    strDirection = "NONE";
                    break;
                case TelephonyManager.DATA_ACTIVITY_OUT:
                    strDirection = "OUT";
                    break;

            }
            if (AppGlobals.APP_FOREGROUND) {
                PhoneInfo.getInstance().setDataDirection(direction);
            }

        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);
            String strServiceState = "NONE";
            switch (serviceState.getState()) {

                case ServiceState.STATE_EMERGENCY_ONLY:
                    strServiceState = "Emergency";
                    break;
                case ServiceState.STATE_IN_SERVICE:
                    strServiceState = "In Service";
                    break;
                case ServiceState.STATE_OUT_OF_SERVICE:
                    strServiceState = "Out of Service";
                    break;
                case ServiceState.STATE_POWER_OFF:
                    strServiceState = "Power off";
                    break;
            }
            if (AppGlobals.APP_FOREGROUND) {
                PhoneInfo.getInstance().serviceState_info.setText(strServiceState);
//            setTextViewText(info_ids[INFO_SERVICE_STATE_INDEX], strServiceState);
            }
        }

        @Override
        public void onSignalStrengthChanged(int asu) {
            super.onSignalStrengthChanged(asu);
            if (AppGlobals.APP_FOREGROUND) {
                PhoneInfo.getInstance().setSignalLevel(PhoneInfo.getInstance().info_ids
                                [PhoneInfo.INFO_SIGNAL_LEVEL_INDEX],
                        PhoneInfo.getInstance().info_ids[PhoneInfo.INFO_SIGNAL_LEVEL_INFO_INDEX], asu);
            }
        }
    };

    static class CheckInternet extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return Helpers.isInternetWorking();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!aBoolean) {
                if (AppGlobals.APP_FOREGROUND) {
                    PhoneInfo.getInstance().setDataDirection(0);
                }
            } else {
                if (AppGlobals.APP_FOREGROUND) {
                    Log.e("TAG", "wifiAction" + AppGlobals.APP_FOREGROUND);
                    PhoneInfo.getInstance().connectionState_info.setText("Connected");
                }
            }
        }
    }
}
