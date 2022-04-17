package com.bigeye.crasher;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
/* loaded from: classes.dex */
public class ReadContacts {
    public final String APP_TAG = getClass().getName();
    private Context context;

    public ReadContacts(Context _context) {
        this.context = _context;
    }

    public String getContactName(String phoneNo) {
        String contactName = "";
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
        Cursor c = this.context.getContentResolver().query(lookupUri, null, null, null, null);
        if (c.getCount() > 0) {
            Log.i(this.APP_TAG, "Rec:" + c.getCount());
        } else {
            Log.i(this.APP_TAG, "No ContactName Found");
        }
        while (c.moveToNext()) {
            contactName = c.getString(c.getColumnIndex("display_name"));
            Log.i(this.APP_TAG, "Contact Name: " + contactName);
        }
        return contactName;
    }
}
