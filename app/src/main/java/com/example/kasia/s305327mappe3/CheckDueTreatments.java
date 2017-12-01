package com.example.kasia.s305327mappe3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Kasia on 30.11.2017.
 */

public class CheckDueTreatments extends Service {

    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    private DBHandler db;
    int due;
    int overdue;
    private List<Treatment> dueTreatments;
    private static final int nId=1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        due = 0;
        overdue = 0;
        db = new DBHandler(this);
        getDueTreatments();
        //vis push varsler bare dersom der er tillatt i preferences
        if (MainActivity.getPush()) {
            if (due > 0 && overdue > 0) {
                //vis push varsel dersom det er noen behandlinger neste 7 dager
                String title = "Coming and overdue treatments";
                String body = "You have " + due + " due, and " + overdue + " overdue treatments!";
                showNotification(title, body);
            } else if (due > 0) {
                String title = "Coming treatments";
                String body = "You have " + due + " treatment(s) coming this week";
                showNotification(title, body);
            } else if (overdue > 0) {
                String title = "Overdue treatments";
                String body = "You have " + overdue + " overdue treatment(s)!";
                showNotification(title, body);
            }
        } else {
            Log.d("BROADCAST", "Push notifications are not active");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotification(String title, String body) {
        NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notify = new Notification.Builder(getApplicationContext()).setAutoCancel(true).setContentText(body).setContentTitle(title).setSmallIcon(R.drawable.alert).setContentIntent(pIntent).build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
    }

    private void getDueTreatments() {
        List<Treatment> treatments = db.findDueTreatments();
        dueTreatments = filterOut(treatments);
    }

    //fjerner behandlinger som ikke er planlagt for neste 7 dager
    private List<Treatment> filterOut(List<Treatment> treatments) {
        List<Treatment> dTreatments = new ArrayList<>();
        String today = sdf.format(Calendar.getInstance().getTime());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 7);
        String todayWeek = sdf.format(c.getTime());

        for (Treatment t: treatments) {
            if (today.compareTo(t.getNextTreatment()) <= 0 && todayWeek.compareTo(t.getNextTreatment()) >= 0) {
                due++;
                dTreatments.add(t);
            }
            else if (today.compareTo(t.getNextTreatment()) > 0) {
                overdue++;
            }
        }
        return dTreatments;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
