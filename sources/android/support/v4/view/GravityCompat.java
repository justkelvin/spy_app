package android.support.v4.view;

import android.graphics.Rect;
import android.os.Build;
import android.view.Gravity;
/* loaded from: classes.dex */
public class GravityCompat {
    public static final int END = 8388613;
    static final GravityCompatImpl IMPL;
    public static final int RELATIVE_HORIZONTAL_GRAVITY_MASK = 8388615;
    public static final int RELATIVE_LAYOUT_DIRECTION = 8388608;
    public static final int START = 8388611;

    /* loaded from: classes.dex */
    interface GravityCompatImpl {
        void apply(int i, int i2, int i3, Rect rect, int i4, int i5, Rect rect2, int i6);

        void apply(int i, int i2, int i3, Rect rect, Rect rect2, int i4);

        void applyDisplay(int i, Rect rect, Rect rect2, int i2);

        int getAbsoluteGravity(int i, int i2);
    }

    /* loaded from: classes.dex */
    static class GravityCompatImplBase implements GravityCompatImpl {
        GravityCompatImplBase() {
        }

        @Override // android.support.v4.view.GravityCompat.GravityCompatImpl
        public int getAbsoluteGravity(int gravity, int layoutDirection) {
            return (-8388609) & gravity;
        }

        @Override // android.support.v4.view.GravityCompat.GravityCompatImpl
        public void apply(int gravity, int w, int h, Rect container, Rect outRect, int layoutDirection) {
            Gravity.apply(gravity, w, h, container, outRect);
        }

        @Override // android.support.v4.view.GravityCompat.GravityCompatImpl
        public void apply(int gravity, int w, int h, Rect container, int xAdj, int yAdj, Rect outRect, int layoutDirection) {
            Gravity.apply(gravity, w, h, container, xAdj, yAdj, outRect);
        }

        @Override // android.support.v4.view.GravityCompat.GravityCompatImpl
        public void applyDisplay(int gravity, Rect display, Rect inoutObj, int layoutDirection) {
            Gravity.applyDisplay(gravity, display, inoutObj);
        }
    }

    /* loaded from: classes.dex */
    static class GravityCompatImplJellybeanMr1 implements GravityCompatImpl {
        GravityCompatImplJellybeanMr1() {
        }

        @Override // android.support.v4.view.GravityCompat.GravityCompatImpl
        public int getAbsoluteGravity(int gravity, int layoutDirection) {
            return GravityCompatJellybeanMr1.getAbsoluteGravity(gravity, layoutDirection);
        }

        @Override // android.support.v4.view.GravityCompat.GravityCompatImpl
        public void apply(int gravity, int w, int h, Rect container, Rect outRect, int layoutDirection) {
            GravityCompatJellybeanMr1.apply(gravity, w, h, container, outRect, layoutDirection);
        }

        @Override // android.support.v4.view.GravityCompat.GravityCompatImpl
        public void apply(int gravity, int w, int h, Rect container, int xAdj, int yAdj, Rect outRect, int layoutDirection) {
            GravityCompatJellybeanMr1.apply(gravity, w, h, container, xAdj, yAdj, outRect, layoutDirection);
        }

        @Override // android.support.v4.view.GravityCompat.GravityCompatImpl
        public void applyDisplay(int gravity, Rect display, Rect inoutObj, int layoutDirection) {
            GravityCompatJellybeanMr1.applyDisplay(gravity, display, inoutObj, layoutDirection);
        }
    }

    static {
        int version = Build.VERSION.SDK_INT;
        if (version >= 17) {
            IMPL = new GravityCompatImplJellybeanMr1();
        } else {
            IMPL = new GravityCompatImplBase();
        }
    }

    public static void apply(int gravity, int w, int h, Rect container, Rect outRect, int layoutDirection) {
        IMPL.apply(gravity, w, h, container, outRect, layoutDirection);
    }

    public static void apply(int gravity, int w, int h, Rect container, int xAdj, int yAdj, Rect outRect, int layoutDirection) {
        IMPL.apply(gravity, w, h, container, xAdj, yAdj, outRect, layoutDirection);
    }

    public static void applyDisplay(int gravity, Rect display, Rect inoutObj, int layoutDirection) {
        IMPL.applyDisplay(gravity, display, inoutObj, layoutDirection);
    }

    public static int getAbsoluteGravity(int gravity, int layoutDirection) {
        return IMPL.getAbsoluteGravity(gravity, layoutDirection);
    }
}
