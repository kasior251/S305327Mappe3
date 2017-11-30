package com.example.kasia.s305327mappe3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Kasia on 30.11.2017.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BROADCAST", "In broadcast receiver");
        Intent i = new Intent(context, MyService.class);
        context.startService(i);
    }
}
