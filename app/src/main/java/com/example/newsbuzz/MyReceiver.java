package com.example.newsbuzz;

import android.accounts.NetworkErrorException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(!mobileInfo.isConnected() && !wifiInfo.isConnected()){
                Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(context, "Internet available", Toast.LENGTH_SHORT).show();
            }
        }else if(action.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)){
            if(intent.getBooleanExtra("state",false)){
                Toast.makeText(context, "Airplane mode on", Toast.LENGTH_SHORT).show();
            }
        }
    }
}