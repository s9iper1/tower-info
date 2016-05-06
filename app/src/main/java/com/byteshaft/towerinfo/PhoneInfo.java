package com.byteshaft.towerinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;


public class PhoneInfo extends Fragment {

    private  static View mBaseView;
    private static final int EXCELLENT_LEVEL = 75;
    private static final int GOOD_LEVEL = 50;
    private static final int MODERATE_LEVEL = 25;
    private static final int WEAK_LEVEL = 0;
    public static final int INFO_SERVICE_STATE_INDEX = 0;
    public static final int INFO_CELL_LOCATION_INDEX = 1;
    public static final int INFO_CALL_STATE_INDEX = 2;
    public static final int INFO_CONNECTION_STATE_INDEX = 3;
    public static final int INFO_SIGNAL_LEVEL_INDEX = 4;
    public static final int INFO_SIGNAL_LEVEL_INFO_INDEX = 5;
    public static final int INFO_DATA_DIRECTION_INDEX = 6;
    public static final int INFO_DEVICE_INFO_INDEX = 7;

    public  final int[] info_ids= {
            R.id.serviceState_info,
            R.id.cellLocation_info,
            R.id.callState_info,
            R.id.connectionState_info,
            R.id.signalLevel,
            R.id.signalLevelInfo,
            R.id.dataDirection,

    };
    private String passingVariabls;
    private TextView deviceId;
    private TextView phoneNumber;
    private TextView softwareVersion;
    private TextView operatorName;
    private TextView simCountryCode;
    private TextView simOperator;
    private TextView simSerialNUmber;
    private TextView subscriberId;
    private TextView networkType;
    private TextView phoneType;
    public TextView serviceState_info;
    public TextView cellLocation_info;
    public TextView callState_info;
    public TextView connectionState_info;
    public ArcProgress signalLevel;
    public TextView signalLevelInfo;
    public ImageView dataDirection;
    public static PhoneInfo instance;


    public static PhoneInfo getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_main, container, false);
        instance = this;
        AppGlobals.APP_FOREGROUND = true;
        deviceId = (TextView) mBaseView.findViewById(R.id.device_id);
        phoneNumber = (TextView) mBaseView.findViewById(R.id.phone_number);
        softwareVersion = (TextView) mBaseView.findViewById(R.id.software_version);
        operatorName = (TextView) mBaseView.findViewById(R.id.operator_name);
        simCountryCode = (TextView) mBaseView.findViewById(R.id.sim_country);
        simOperator = (TextView) mBaseView.findViewById(R.id.sim_operator);
        simSerialNUmber = (TextView) mBaseView.findViewById(R.id.sim_serial_number);
        subscriberId = (TextView) mBaseView.findViewById(R.id.subscriber_id);
        networkType = (TextView) mBaseView.findViewById(R.id.network_type);
        phoneType = (TextView) mBaseView.findViewById(R.id.phone_type);
        serviceState_info = (TextView) mBaseView.findViewById(R.id.serviceState_info);
        cellLocation_info = (TextView) mBaseView.findViewById(R.id.cellLocation_info);
        callState_info = (TextView) mBaseView.findViewById(R.id.callState_info);
        connectionState_info = (TextView) mBaseView.findViewById(R.id.connectionState_info);
        signalLevel = (ArcProgress) mBaseView.findViewById(R.id.signalLevel);
        signalLevelInfo = (TextView) mBaseView.findViewById(R.id.signalLevelInfo);
        dataDirection = (ImageView) mBaseView.findViewById(R.id.dataDirection);
        displayTelephonyInfo();
        GlobalReceiver.startSignalLevelListener(getActivity().getApplicationContext());

        return mBaseView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        //Stop listening to the telephony events
//        StopListener();

    }

    @Override
    public void onPause() {
        super.onPause();
        //Stop listening to the telephony events
//        StopListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        //subscribes to the telephony related events
        AppGlobals.APP_FOREGROUND = true;
    }

    /*
     * Display the telephony related information
     * */
    private void displayTelephonyInfo() {

        Log.i("displayTelephonyInfo", "<<------- START ------- >>");
        //access to the telephony services
        TelephonyManager tm = (TelephonyManager) getActivity().getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        //	int	iCdmaDbm = SignalStrength.getCdmaDbm();
        //access to the gsm info ,..requires ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permission
        GsmCellLocation gsmLoc = (GsmCellLocation)tm.getCellLocation();

        //Get the IMEI code
        String deviceid = tm.getDeviceId();
        //Get  the phone number string for line 1, for example, the MSISDN for a GSM phone
        String phonenumber = tm.getLine1Number();
        //Get  the software version number for the device, for example, the IMEI/SV for GSM phones
        String softwareversion = tm.getDeviceSoftwareVersion();
        //Get  the alphabetic name of current registered operator.
        String operatorname = tm.getNetworkOperatorName();
        //Get  the ISO country code equivalent for the SIM provider's country code.
        String simcountrycode = tm.getSimCountryIso();
        //Get  the Service Provider Name (SPN).
        String simoperator = tm.getSimOperatorName();
        //Get  the serial number of the SIM, if applicable. Return null if it is unavailable.
        String simserialno = tm.getSimSerialNumber();
        //Get  the unique subscriber ID, for example, the IMSI for a GSM phone
        String subscriberid = tm.getSubscriberId();
        //Get the type indicating the radio technology (network type) currently in use on the device for data transmission.
        //EDGE,GPRS,UMTS  etc
        String networktype = getNetworkTypeString(tm.getNetworkType());
        //indicating the device phone type. This indicates the type of radio used to transmit voice calls
        //GSM,CDMA etc
        String phonetype = getPhoneTypeString(tm.getPhoneType());

        String deviceinfo = "";

        this.deviceId.setText(deviceid);
        this.phoneNumber.setText(phonenumber);
        this.softwareVersion.setText(softwareversion);
        this.operatorName.setText(operatorname);
        this.simCountryCode.setText(simcountrycode);
        this.simOperator.setText(simoperator);
        this.simSerialNUmber.setText(simserialno);
        this.subscriberId.setText(subscriberid);
        this.networkType.setText(networktype);
        this.phoneType.setText(phonetype);
//        passingVariabls = deviceid+","+phonenumber+","+softwareversion+","+operatorname+","+simcountrycode+","+simoperator
//                +","+simserialno+","+subscriberid+","+networktype+","+phonetype;

        // Toast.makeText(getApplicationContext(), "var"+passingVariabls, Toast.LENGTH_SHORT).show();
    }

    public void setDataDirection(int direction){
        int resid = getDataDirectionRes(direction);
        //((TextView)findViewById(id)).setCompoundDrawables(null, null,getResources().getDrawable(resid), null);
        ((ImageView) mBaseView.findViewById(R.id.dataDirection)).setImageResource(resid);
    }

    private int getDataDirectionRes(int direction){
        int resid;
        switch(direction)  {
            case TelephonyManager.DATA_ACTIVITY_IN:    		resid = R.drawable.indata;break;
            case TelephonyManager.DATA_ACTIVITY_OUT:        resid = R.drawable.outdata; break;
            case TelephonyManager.DATA_ACTIVITY_INOUT:      resid = R.drawable.bidata; break;
            case TelephonyManager.DATA_ACTIVITY_NONE:       resid = R.drawable.nodata; break;
            default:
                resid = R.drawable.nodata; break;
        }
        return resid;
    }

    public void setSignalLevel(int id,int infoid,int level){

        int progress = (int) ((((float)level)/31.0) * 100);

        String signalLevelString = getSignalLevelString(progress);

        //set the status
        ArcProgress arcProgress = (ArcProgress) mBaseView.findViewById(id);
        arcProgress.setProgress(progress);
        if (progress < 40) {
            arcProgress.setFinishedStrokeColor(Color.RED);
        } else if (progress < 60) {
            arcProgress.setFinishedStrokeColor(Color.YELLOW);
        } else if (progress < 80) {
            arcProgress.setFinishedStrokeColor(Color.parseColor("#FFA500"));
        } else if (progress > 80) {
            arcProgress.setFinishedStrokeColor(Color.GREEN);
        }
        arcProgress.setBottomText(signalLevelString);
    }


    public String getSignalLevelString(int level) {

        String signalLevelString = "Weak";

        if(level > EXCELLENT_LEVEL)             signalLevelString = "Excellent";
        else if(level > GOOD_LEVEL)             signalLevelString = "Good";
        else if(level > MODERATE_LEVEL) 		signalLevelString = "Moderate";
        else if(level > WEAK_LEVEL)             signalLevelString = "Weak";
        else if (level < WEAK_LEVEL)            signalLevelString = "No Service";

        return signalLevelString;
    }


    private String getNetworkTypeString(int type){
        Log.i("NETWORK TYE", "" + type);
        switch(type)
        {
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return  "GPRS";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return  "UMTS";
            default:
                return  "UNKNOWN";
        }
    }

    private String getPhoneTypeString(int type){
        String typeString = "Unknown";

        switch(type)
        {
            case TelephonyManager.PHONE_TYPE_GSM:   typeString = "GSM"; break;
            case TelephonyManager.PHONE_TYPE_NONE:  typeString = "UNKNOWN"; break;
            default:
                typeString = "UNKNOWN"; break;
        }

        return typeString;
    }


//    private final PhoneStateListener phoneListener = new  PhoneStateListener(){
//
//
//        @Override
//        public void onCallStateChanged(int state, String incomingNumber) {
//            super.onCallStateChanged(state, incomingNumber);
//            Log.e("onCallStateChanged", "onCallStateChanged");
//            String phoneState = "UNKNOWN";
//            switch(state){
//
//                case TelephonyManager.CALL_STATE_IDLE :
//                    phoneState = "IDLE";
//                    break;
//                case TelephonyManager.CALL_STATE_RINGING :
//                    phoneState = "Ringing (" + incomingNumber + ") ";
//                    break;
//                case TelephonyManager.CALL_STATE_OFFHOOK :
//                    phoneState = "Offhook";
//                    break;
//
//            }
//
//            callState_info.setText(phoneState);
////            setTextViewText(info_ids[INFO_CALL_STATE_INDEX], phoneState);
//
//        }
//
//        @Override
//        public void onCellLocationChanged(CellLocation location) {
//            super.onCellLocationChanged(location);
//            Log.e("onCellLocationChanged", "onCellLocationChanged");
//            String strLocation = location.toString();
//            cellLocation_info.setText(strLocation);
////            setTextViewText(info_ids[INFO_CELL_LOCATION_INDEX], strLocation);
//        }
//
//        /*
//         * Cellphone data connection status
//         * */
//
//        @Override
//        public void onDataConnectionStateChanged(int state, int networkType) {
//            super.onDataConnectionStateChanged(state, networkType);
//            Log.e("onDataConnectionState", "onDataConnectionStateChanged");
//            String phoneState = "UNKNOWN";
//            switch(state){
//                case TelephonyManager.DATA_CONNECTED :
//                    phoneState = "Connected";
//                    break;
//                case TelephonyManager.DATA_CONNECTING :
//                    phoneState = "Connecting..";
//                    break;
//                case TelephonyManager.DATA_DISCONNECTED :
//                    phoneState = "Disconnected";
//                    break;
//                case TelephonyManager.DATA_SUSPENDED :
//                    phoneState = "Suspended";
//                    break;
//            }
//            connectionState_info.setText(phoneState);
////            setTextViewText(info_ids[INFO_CONNECTION_STATE_INDEX], phoneState);
//
//        }
//
//        @Override
//        public void onDataActivity(int direction) {
//            super.onDataActivity(direction);
//            Log.e("onDataActivity", "onDataActivity");
//            String strDirection = "NONE";
//            switch(direction){
//
//                case TelephonyManager.DATA_ACTIVITY_IN:
//                    strDirection = "IN";
//                    break;
//                case TelephonyManager.DATA_ACTIVITY_INOUT:
//                    strDirection = "IN-OUT";
//                    break;
//                case TelephonyManager.DATA_ACTIVITY_DORMANT:
//                    strDirection = "Dormant";
//                    break;
//                case TelephonyManager.DATA_ACTIVITY_NONE:
//                    strDirection="NONE";
//                    break;
//                case TelephonyManager.DATA_ACTIVITY_OUT:
//                    strDirection="OUT";
//                    break;
//
//            }
//            setDataDirection(direction);
//
//        }
//
//        @Override
//        public void onServiceStateChanged(ServiceState serviceState) {
//            super.onServiceStateChanged(serviceState);
//            Log.e("onServiceStateChanged", "onServiceStateChanged");
//            String strServiceState = "NONE";
//            switch(serviceState.getState()){
//
//                case ServiceState.STATE_EMERGENCY_ONLY:
//                    strServiceState = "Emergency";
//                    break;
//                case ServiceState.STATE_IN_SERVICE:
//                    strServiceState = "In Service";
//                    break;
//                case ServiceState.STATE_OUT_OF_SERVICE:
//                    strServiceState = "Out of Service";
//                    break;
//                case ServiceState.STATE_POWER_OFF:
//                    strServiceState = "Power off";
//                    break;
//            }
//
//            serviceState_info.setText(strServiceState);
////            setTextViewText(info_ids[INFO_SERVICE_STATE_INDEX], strServiceState);
//        }
//
//        @Override
//        public void onSignalStrengthChanged(int asu) {
//            super.onSignalStrengthChanged(asu);
//            setSignalLevel(info_ids[INFO_SIGNAL_LEVEL_INDEX], info_ids[INFO_SIGNAL_LEVEL_INFO_INDEX],asu);
//        }
//    };
}