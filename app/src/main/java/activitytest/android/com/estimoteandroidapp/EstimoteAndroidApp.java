package activitytest.android.com.estimoteandroidapp;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

public class EstimoteAndroidApp extends Application {
    private String TAG = "EstimoteAndroidApp";

    private static final String APP_ID = "lodo-marchesi-gmail-com-s--fcu";
    private static final String APP_TOKEN = "4ff0a7cba332f736b651eace30056fda";

    @Override
    public void onCreate() {
        super.onCreate();

        //  App ID & App Token can be taken from App section of Nearable Cloud.
        EstimoteSDK.initialize(getApplicationContext(), APP_ID, APP_TOKEN);
        // Optional, debug logging.
        EstimoteSDK.enableDebugLogging(true);
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, EstimoteListActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
