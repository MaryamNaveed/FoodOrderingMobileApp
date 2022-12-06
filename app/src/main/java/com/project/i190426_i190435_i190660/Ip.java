package com.project.i190426_i190435_i190660;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Ip {
    public static String ipAdd="http://192.168.10.8/finalProject";

    public static  boolean isConnected(Context c){
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();

        return  connected;

    }
}
