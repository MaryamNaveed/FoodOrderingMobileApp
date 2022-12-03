package com.project.i190426_i190435_i190660;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {



        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            boolean conn = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if(conn){
//                Log.d("con", "connected");
//                context.startActivity(intent);
//                ((Activity) context).recreate();
//                Toast.makeText(context, "Online", Toast.LENGTH_LONG).show();

            }
            else{

//                Toast.makeText(context, "Offline", Toast.LENGTH_LONG).show();
            }
        }
    }


}