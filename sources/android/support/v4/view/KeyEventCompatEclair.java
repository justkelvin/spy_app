package android.support.v4.view;

import android.view.KeyEvent;
/* loaded from: classes.dex */
class KeyEventCompatEclair {
    KeyEventCompatEclair() {
    }

    public static void startTracking(KeyEvent event) {
        event.startTracking();
    }

    public static boolean isTracking(KeyEvent event) {
        return event.isTracking();
    }
}
