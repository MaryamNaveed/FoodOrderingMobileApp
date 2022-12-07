package com.project.i190426_i190435_i190660;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;

public class MyApplication extends Application {
    private static final String ONESIGNAL_APP_ID = "37fe61d2-e428-4d28-95cb-7a9763ff2473";

    @Override
    public void onCreate() {
        super.onCreate();


        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();

        OneSignal.setNotificationOpenedHandler(new OneSignal.OSNotificationOpenedHandler() {
            @Override
            public void notificationOpened(OSNotificationOpenedResult osNotificationOpenedResult) {

                String title=osNotificationOpenedResult.getNotification().getTitle();

                System.out.println("Title: "+ title);

                SharedPreferences mPref;
                mPref=getSharedPreferences("com.project.i190426_i190435_i190660", MODE_PRIVATE);

                if(mPref.getBoolean("loggedInCustomer", false)){
                    Intent intent= new Intent(getApplicationContext(), PreviousOrders.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else if(mPref.getBoolean("loggedInAdmin", false)){
                    Intent intent= new Intent(getApplicationContext(), RestaurantOrders.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }


        });

        OneSignal.setNotificationWillShowInForegroundHandler(new OneSignal.OSNotificationWillShowInForegroundHandler() {

            @Override
            public void notificationWillShowInForeground(OSNotificationReceivedEvent osNotificationReceivedEvent) {

            }
        });
    }
}
