package com.byteshaft.towerinfo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SignalsInfo extends Fragment implements View.OnClickListener {

    private View mBaseView;
    TextView textBatteryLevel = null;
    TelephonyManager Tel;
    MyPhoneStateListener    MyListener;
    String passingVariabls ;
    String ssignal;
    String allsendingVars ;
    private static final int[] info_ids= {
            R.id.serviceState_info,
            R.id.cellLocation_info,
            R.id.callState_info,
            R.id.connectionState_info,
            R.id.signalLevel,
            R.id.dataDirection,
    };

    String result;
    ProgressDialog pdialog;
    private final String SPACE = "          ";
    private TextView mSignalStrength;
    private TextView gsmBitError;
    private TextView cdmaDbm;
    private TextView cdmaEcio;
    private TextView evdoDbm;
    private TextView evdoEcio;
    private TextView evdoSnr;
    private TextView lteSignalStrength;
    private TextView lteRsrp;
    private TextView lteRsrq;
    private TextView lteRssnr;
    private TextView lteCqi;
    private TextView gsm;
    private Button uploadButton;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    public static SignalsInfo sInstance;

    public static SignalsInfo getInstance() {
        return sInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_signals, container, false);
        sInstance = this;
//        textBatteryLevel = (TextView) mBaseView.findViewById(R.id.batterylevel);
        mSignalStrength = (TextView) mBaseView.findViewById(R.id.signal_strength);
        gsmBitError = (TextView) mBaseView.findViewById(R.id.bit_error);
        cdmaDbm = (TextView) mBaseView.findViewById(R.id.cdma_dbm);
        cdmaEcio = (TextView) mBaseView.findViewById(R.id.cdma_ecio);
        evdoDbm = (TextView) mBaseView.findViewById(R.id.evdo_dbm);
        evdoEcio = (TextView) mBaseView.findViewById(R.id.evdo_ecio);
        evdoSnr = (TextView) mBaseView.findViewById(R.id.evdo_snr);
        lteSignalStrength = (TextView) mBaseView.findViewById(R.id.lte_signal_strength);
        lteRsrp = (TextView) mBaseView.findViewById(R.id.lte_rsrp);
        lteRsrq = (TextView) mBaseView.findViewById(R.id.lte_rsrq);
        lteRssnr = (TextView) mBaseView.findViewById(R.id.lte_rssnr);
        lteCqi = (TextView) mBaseView.findViewById(R.id.lte_cqi);
        gsm = (TextView) mBaseView.findViewById(R.id.gsm);
        uploadButton = (Button) mBaseView.findViewById(R.id.upload);
        uploadButton.setOnClickListener(this);
        Tel = ( TelephonyManager ) getActivity().getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        if(checkAndRequestPermissions()) {
            start();
        }


//	      try {
//	    	    final TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//	    	    for (final CellInfo info : tm.getAllCellInfo()) {
//	    	        if (info instanceof CellInfoGsm) {
//	    	            final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
//	    	           System.out.println("gsmmmmmm : "+ gsm);
//	    	        } else if (info instanceof CellInfoCdma) {
//	    	            final CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
//	    	            System.out.println("cdmaaaa : "+ cdma);
//	    	        } else if (info instanceof CellInfoLte) {
//	    	            final CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
//	    	            System.out.println("lte"+ lte);
//	    	        } else {
//	    	            throw new Exception("Unknown type of cell signal!");
//	    	        }
//	    	    }
//	    	} catch (Exception e) {
//	    	    Log.e("TAG", "Unable to obtain cell signal information", e);
//	    	}

        return mBaseView;
    }

    public void start() {
        displayTelephonyInfo();
        MyListener   = new MyPhoneStateListener();
        Tel.listen(MyListener , PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    private boolean checkAndRequestPermissions() {
        int coarseLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int bootComplete = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECEIVE_BOOT_COMPLETED);
        int networkState = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_NETWORK_STATE);
        int wakeLock = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WAKE_LOCK);
        int locationPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (bootComplete != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        }
        if (networkState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (wakeLock != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (coarseLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        Tel.listen(MyListener, PhoneStateListener.LISTEN_NONE);
    }

    /* Called when the application resumes */
    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission_group.LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Tel.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }
    }

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

        deviceinfo += ("Device ID: " + deviceid + "\n");
        deviceinfo += ("Phone Number: " + phonenumber + "\n");
        deviceinfo += ("Software Version: " + softwareversion + "\n");
        deviceinfo += ("Operator Name: " + operatorname + "\n");
        deviceinfo += ("SIM Country Code: " + simcountrycode + "\n");
        deviceinfo += ("SIM Operator: " + simoperator + "\n");
        deviceinfo += ("SIM Serial No.: " + simserialno + "\n");
        deviceinfo += ("Subscriber ID: " + subscriberid + "\n");
        deviceinfo += ("Network Type: " + networktype + "\n");
        deviceinfo += ("Phone Type: " + phonetype + "\n");
//          setTextViewText(info_ids[INFO_DEVICE_INFO_INDEX],deviceinfo);

        passingVariabls = deviceid+","+phonenumber+","+softwareversion+","+operatorname+","+simcountrycode+","+simoperator
                +","+simserialno+","+subscriberid+","+networktype+","+phonetype;

        Toast.makeText(getActivity().getApplicationContext(),
                "var"+passingVariabls, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload:
                if (AppGlobals.Uploaded) {
                    Toast.makeText(getActivity(), "Acquiring Data", Toast.LENGTH_SHORT).show();
                    if (NetworkService.getInstance() == null) {
                        Intent intent1 = new Intent(getActivity().getApplicationContext(),
                                NetworkService.class);
                        intent1.putExtra(AppGlobals.SEND_BROAD_CAST, true);
                        getActivity().startService(intent1);
                    }
                    if (NetworkService.getInstance() != null) {
                        AppGlobals.CURRENT_STATE = AppGlobals.schedule;
                        NetworkService.getInstance().startLocationUpdate();
                    }
                } else {
                    Set<String> strings = Helpers.getHashSet();
                    if (!strings.isEmpty()) {
                        new NetworkService.UploadDataTask().execute();
                    }
                }
                break;
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener
    {
        /* Get the Signal strength from the provider, each tiome there is an update */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength)  {
            super.onSignalStrengthsChanged(signalStrength);
            ssignal = signalStrength.toString();
            String[] parts = ssignal.split(" ");

            mSignalStrength.setText(parts[1]);
            gsmBitError.setText(parts[2]);
            cdmaDbm.setText(parts[3]);
            cdmaEcio.setText(parts[4]);
            evdoDbm.setText(parts[5]);
            evdoEcio.setText(parts[6]);
            evdoSnr.setText(parts[7]);
            lteSignalStrength.setText(parts[8]);
            lteRsrp.setText(parts[9]);
            lteRsrq.setText(parts[10]);
            lteRssnr.setText(parts[11]);
            lteCqi.setText(parts[12]);
            gsm.setText(parts[13]);

//            textBatteryLevel.setText("Signalstrength : " +SPACE +  parts[1].toString()
//                    +"\n"+"GsmBitErrorRate : "  +SPACE+  parts[2].toString()
//                    +"\n"+"CdmaDbm : "  +SPACE +  parts[3].toString()
//                    +"\n"+"CdmaEcio : " +SPACE +  parts[4].toString()
//                    +"\n"+"EvdoDbm : " +SPACE +  parts[5].toString()
//                    +"\n"+"EvdoEcio : +SPACE " +  parts[6].toString()
//                    +"\n"+"EvdoSnr : " +SPACE +  parts[7].toString()
//                    +"\n"+"LteSignalStrength : " +SPACE +  parts[8].toString()
//                    +"\n"+"LteRsrp : " +SPACE +  parts[9].toString()
//                    +"\n"+"LteRsrq : " +SPACE +  parts[10].toString()
//                    +"\n"+"LteRssnr : "  +SPACE+  parts[11].toString()
//                    +"\n"+"LteCqi : " +SPACE +  parts[12].toString()
//                    +"\n"+"gsm|lte|cdma : " +SPACE +  parts[13].toString()
//
//            );

            allsendingVars = ssignal+""+passingVariabls;

//         textBatteryLevel.setText("GSM Cinr = "+ String.valueOf(mSignalStrength.getGsmSignalStrength())
//        		 +"\n"+"GSM CdmaDbm = "+ String.valueOf(mSignalStrength.getGsmSignalStrength())
//        		 +"\n"+ "GSM EvdoDbm = "+ String.valueOf(mSignalStrength.getEvdoDbm())
//        		 +"\n"+ "GSM EvdoEcio = "+ String.valueOf(mSignalStrength.getEvdoEcio())
//        		 +"\n"+ "GSM EvdoSnr = "+ String.valueOf(mSignalStrength.getEvdoSnr())
//        		 +"\n"+ "GSM BitErrorRate = "+ String.valueOf(mSignalStrength.getGsmBitErrorRate())
//        		 +"\n"+ "DescribeContents = "+ String.valueOf(mSignalStrength.describeContents()));
//
//
//
//            System.out.println("GSM Cinr = "+ String.valueOf(signalStrength.getGsmSignalStrength()));
//            System.out.println("GSM CdmaDbm = "+ String.valueOf(signalStrength.getGsmSignalStrength()));
//            System.out.println("GSM EvdoDbm = "+ String.valueOf(signalStrength.getEvdoDbm()));
//            System.out.println("GSM EvdoEcio = "+ String.valueOf(signalStrength.getEvdoEcio()));
//            System.out.println("GSM EvdoSnr = "+ String.valueOf(signalStrength.getEvdoSnr()));
//            System.out.println("GSM BitErrorRate = "+ String.valueOf(signalStrength.getGsmBitErrorRate()));
//            System.out.println("GSM DescribeContents = "+ String.valueOf(signalStrength.describeContents()));
        }

    };

    private String getNetworkTypeString(int type){
        String typeString = "Unknown";

        switch(type)  {
            case TelephonyManager.NETWORK_TYPE_EDGE:        typeString = "EDGE"; break;
            case TelephonyManager.NETWORK_TYPE_GPRS:        typeString = "GPRS"; break;
            case TelephonyManager.NETWORK_TYPE_UMTS:        typeString = "UMTS"; break;
            default:
                typeString = "UNKNOWN"; break;
        }

        return typeString;
    }

    private String getPhoneTypeString(int type) {
        String typeString = "Unknown";

        switch (type) {
            case TelephonyManager.PHONE_TYPE_GSM:
                typeString = "GSM";
                break;
            case TelephonyManager.PHONE_TYPE_NONE:
                typeString = "UNKNOWN";
                break;
            default:
                typeString = "UNKNOWN";
                break;
        }

        return typeString;
    }

    public void showMsg(String msg) {
        Toast.makeText(getActivity() , msg, Toast.LENGTH_SHORT).show();
    }


}
