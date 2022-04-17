package android.support.v4.app;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
/* loaded from: classes.dex */
public class ActionBarDrawerToggle implements DrawerLayout.DrawerListener {
    private static final int ID_HOME = 16908332;
    private static final ActionBarDrawerToggleImpl IMPL;
    private final Activity mActivity;
    private final int mCloseDrawerContentDescRes;
    private Drawable mDrawerImage;
    private final int mDrawerImageResource;
    private boolean mDrawerIndicatorEnabled = true;
    private final DrawerLayout mDrawerLayout;
    private final int mOpenDrawerContentDescRes;
    private Object mSetIndicatorInfo;
    private SlideDrawable mSlider;
    private Drawable mThemeImage;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface ActionBarDrawerToggleImpl {
        Drawable getThemeUpIndicator(Activity activity);

        Object setActionBarDescription(Object obj, Activity activity, int i);

        Object setActionBarUpIndicator(Object obj, Activity activity, Drawable drawable, int i);
    }

    /* loaded from: classes.dex */
    private static class ActionBarDrawerToggleImplBase implements ActionBarDrawerToggleImpl {
        private ActionBarDrawerToggleImplBase() {
        }

        @Override // android.support.v4.app.ActionBarDrawerToggle.ActionBarDrawerToggleImpl
        public Drawable getThemeUpIndicator(Activity activity) {
            return null;
        }

        @Override // android.support.v4.app.ActionBarDrawerToggle.ActionBarDrawerToggleImpl
        public Object setActionBarUpIndicator(Object info, Activity activity, Drawable themeImage, int contentDescRes) {
            return info;
        }

        @Override // android.support.v4.app.ActionBarDrawerToggle.ActionBarDrawerToggleImpl
        public Object setActionBarDescription(Object info, Activity activity, int contentDescRes) {
            return info;
        }
    }

    /* loaded from: classes.dex */
    private static class ActionBarDrawerToggleImplHC implements ActionBarDrawerToggleImpl {
        private ActionBarDrawerToggleImplHC() {
        }

        @Override // android.support.v4.app.ActionBarDrawerToggle.ActionBarDrawerToggleImpl
        public Drawable getThemeUpIndicator(Activity activity) {
            return ActionBarDrawerToggleHoneycomb.getThemeUpIndicator(activity);
        }

        @Override // android.support.v4.app.ActionBarDrawerToggle.ActionBarDrawerToggleImpl
        public Object setActionBarUpIndicator(Object info, Activity activity, Drawable themeImage, int contentDescRes) {
            return ActionBarDrawerToggleHoneycomb.setActionBarUpIndicator(info, activity, themeImage, contentDescRes);
        }

        @Override // android.support.v4.app.ActionBarDrawerToggle.ActionBarDrawerToggleImpl
        public Object setActionBarDescription(Object info, Activity activity, int contentDescRes) {
            return ActionBarDrawerToggleHoneycomb.setActionBarDescription(info, activity, contentDescRes);
        }
    }

    static {
        int version = Build.VERSION.SDK_INT;
        if (version >= 11) {
            IMPL = new ActionBarDrawerToggleImplHC();
        } else {
            IMPL = new ActionBarDrawerToggleImplBase();
        }
    }

    public ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        this.mActivity = activity;
        this.mDrawerLayout = drawerLayout;
        this.mDrawerImageResource = drawerImageRes;
        this.mOpenDrawerContentDescRes = openDrawerContentDescRes;
        this.mCloseDrawerContentDescRes = closeDrawerContentDescRes;
        this.mThemeImage = IMPL.getThemeUpIndicator(activity);
        this.mDrawerImage = activity.getResources().getDrawable(drawerImageRes);
        this.mSlider = new SlideDrawable(this.mDrawerImage);
        this.mSlider.setOffsetBy(0.33333334f);
    }

    public void syncState() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mSlider.setOffset(1.0f);
        } else {
            this.mSlider.setOffset(0.0f);
        }
        if (this.mDrawerIndicatorEnabled) {
            this.mSetIndicatorInfo = IMPL.setActionBarUpIndicator(this.mSetIndicatorInfo, this.mActivity, this.mSlider, this.mDrawerLayout.isDrawerOpen(GravityCompat.START) ? this.mOpenDrawerContentDescRes : this.mCloseDrawerContentDescRes);
        }
    }

    public void setDrawerIndicatorEnabled(boolean enable) {
        if (enable != this.mDrawerIndicatorEnabled) {
            if (enable) {
                this.mSetIndicatorInfo = IMPL.setActionBarUpIndicator(this.mSetIndicatorInfo, this.mActivity, this.mSlider, this.mDrawerLayout.isDrawerOpen(GravityCompat.START) ? this.mOpenDrawerContentDescRes : this.mCloseDrawerContentDescRes);
            } else {
                this.mSetIndicatorInfo = IMPL.setActionBarUpIndicator(this.mSetIndicatorInfo, this.mActivity, this.mThemeImage, 0);
            }
            this.mDrawerIndicatorEnabled = enable;
        }
    }

    public boolean isDrawerIndicatorEnabled() {
        return this.mDrawerIndicatorEnabled;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        this.mThemeImage = IMPL.getThemeUpIndicator(this.mActivity);
        this.mDrawerImage = this.mActivity.getResources().getDrawable(this.mDrawerImageResource);
        syncState();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == null || item.getItemId() != ID_HOME || !this.mDrawerIndicatorEnabled) {
            return false;
        }
        if (this.mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
        this.mDrawerLayout.openDrawer(GravityCompat.START);
        return false;
    }

    @Override // android.support.v4.widget.DrawerLayout.DrawerListener
    public void onDrawerSlide(View drawerView, float slideOffset) {
        float glyphOffset;
        float glyphOffset2 = this.mSlider.getOffset();
        if (slideOffset > 0.5f) {
            glyphOffset = Math.max(glyphOffset2, Math.max(0.0f, slideOffset - 0.5f) * 2.0f);
        } else {
            glyphOffset = Math.min(glyphOffset2, slideOffset * 2.0f);
        }
        this.mSlider.setOffset(glyphOffset);
    }

    @Override // android.support.v4.widget.DrawerLayout.DrawerListener
    public void onDrawerOpened(View drawerView) {
        this.mSlider.setOffset(1.0f);
        if (this.mDrawerIndicatorEnabled) {
            this.mSetIndicatorInfo = IMPL.setActionBarDescription(this.mSetIndicatorInfo, this.mActivity, this.mOpenDrawerContentDescRes);
        }
    }

    @Override // android.support.v4.widget.DrawerLayout.DrawerListener
    public void onDrawerClosed(View drawerView) {
        this.mSlider.setOffset(0.0f);
        if (this.mDrawerIndicatorEnabled) {
            this.mSetIndicatorInfo = IMPL.setActionBarDescription(this.mSetIndicatorInfo, this.mActivity, this.mCloseDrawerContentDescRes);
        }
    }

    @Override // android.support.v4.widget.DrawerLayout.DrawerListener
    public void onDrawerStateChanged(int newState) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SlideDrawable extends Drawable implements Drawable.Callback {
        private float mOffset;
        private float mOffsetBy;
        private final Rect mTmpRect = new Rect();
        private Drawable mWrapped;

        public SlideDrawable(Drawable wrapped) {
            this.mWrapped = wrapped;
        }

        public void setOffset(float offset) {
            this.mOffset = offset;
            invalidateSelf();
        }

        public float getOffset() {
            return this.mOffset;
        }

        public void setOffsetBy(float offsetBy) {
            this.mOffsetBy = offsetBy;
            invalidateSelf();
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            this.mWrapped.copyBounds(this.mTmpRect);
            canvas.save();
            canvas.translate(this.mOffsetBy * this.mTmpRect.width() * (-this.mOffset), 0.0f);
            this.mWrapped.draw(canvas);
            canvas.restore();
        }

        @Override // android.graphics.drawable.Drawable
        public void setChangingConfigurations(int configs) {
            this.mWrapped.setChangingConfigurations(configs);
        }

        @Override // android.graphics.drawable.Drawable
        public int getChangingConfigurations() {
            return this.mWrapped.getChangingConfigurations();
        }

        @Override // android.graphics.drawable.Drawable
        public void setDither(boolean dither) {
            this.mWrapped.setDither(dither);
        }

        @Override // android.graphics.drawable.Drawable
        public void setFilterBitmap(boolean filter) {
            this.mWrapped.setFilterBitmap(filter);
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int alpha) {
            this.mWrapped.setAlpha(alpha);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter cf) {
            this.mWrapped.setColorFilter(cf);
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(int color, PorterDuff.Mode mode) {
            this.mWrapped.setColorFilter(color, mode);
        }

        @Override // android.graphics.drawable.Drawable
        public void clearColorFilter() {
            this.mWrapped.clearColorFilter();
        }

        @Override // android.graphics.drawable.Drawable
        public boolean isStateful() {
            return this.mWrapped.isStateful();
        }

        @Override // android.graphics.drawable.Drawable
        public boolean setState(int[] stateSet) {
            return this.mWrapped.setState(stateSet);
        }

        @Override // android.graphics.drawable.Drawable
        public int[] getState() {
            return this.mWrapped.getState();
        }

        @Override // android.graphics.drawable.Drawable
        public Drawable getCurrent() {
            return this.mWrapped.getCurrent();
        }

        @Override // android.graphics.drawable.Drawable
        public boolean setVisible(boolean visible, boolean restart) {
            return super.setVisible(visible, restart);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return this.mWrapped.getOpacity();
        }

        @Override // android.graphics.drawable.Drawable
        public Region getTransparentRegion() {
            return this.mWrapped.getTransparentRegion();
        }

        @Override // android.graphics.drawable.Drawable
        protected boolean onStateChange(int[] state) {
            this.mWrapped.setState(state);
            return super.onStateChange(state);
        }

        @Override // android.graphics.drawable.Drawable
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            this.mWrapped.setBounds(bounds);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return this.mWrapped.getIntrinsicWidth();
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return this.mWrapped.getIntrinsicHeight();
        }

        @Override // android.graphics.drawable.Drawable
        public int getMinimumWidth() {
            return this.mWrapped.getMinimumWidth();
        }

        @Override // android.graphics.drawable.Drawable
        public int getMinimumHeight() {
            return this.mWrapped.getMinimumHeight();
        }

        @Override // android.graphics.drawable.Drawable
        public boolean getPadding(Rect padding) {
            return this.mWrapped.getPadding(padding);
        }

        @Override // android.graphics.drawable.Drawable
        public Drawable.ConstantState getConstantState() {
            return super.getConstantState();
        }

        @Override // android.graphics.drawable.Drawable.Callback
        public void invalidateDrawable(Drawable who) {
            if (who == this.mWrapped) {
                invalidateSelf();
            }
        }

        @Override // android.graphics.drawable.Drawable.Callback
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            if (who == this.mWrapped) {
                scheduleSelf(what, when);
            }
        }

        @Override // android.graphics.drawable.Drawable.Callback
        public void unscheduleDrawable(Drawable who, Runnable what) {
            if (who == this.mWrapped) {
                unscheduleSelf(what);
            }
        }
    }
}
