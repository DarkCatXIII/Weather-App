package com.play.ata.weather;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

public class Notification_reciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try{NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeatingIntent=new Intent(context,MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent= PendingIntent.getActivities(context,100, new Intent[]{repeatingIntent},PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder= (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.weatherapp)
                .setContentTitle("The Weather TO Day")
                .setContentText("See the weather before going out");
        notificationManager.notify(100,builder.build());
        }catch (Exception e){}
    }
}
