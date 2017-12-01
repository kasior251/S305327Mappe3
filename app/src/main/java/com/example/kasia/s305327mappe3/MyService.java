package com.example.kasia.s305327mappe3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Kasia on 30.11.2017.
 */

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("BROADCAST", "In MyService");
        Calendar calendar = Calendar.getInstance();
        Intent i = new Intent(this,CheckDueTreatments.class );
        PendingIntent pIntent = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, pIntent);

        return super.onStartCommand(intent, flags, startId);
    }
}
