package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/CalendarView.class */
public class CalendarView extends FrameLayout {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/CalendarView$OnDateChangeListener.class */
    public interface OnDateChangeListener {
        void onSelectedDayChange(CalendarView calendarView, int i, int i2, int i3);
    }

    public CalendarView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setShownWeekCount(int count) {
        throw new RuntimeException("Stub!");
    }

    public int getShownWeekCount() {
        throw new RuntimeException("Stub!");
    }

    public void setSelectedWeekBackgroundColor(int color) {
        throw new RuntimeException("Stub!");
    }

    public int getSelectedWeekBackgroundColor() {
        throw new RuntimeException("Stub!");
    }

    public void setFocusedMonthDateColor(int color) {
        throw new RuntimeException("Stub!");
    }

    public int getFocusedMonthDateColor() {
        throw new RuntimeException("Stub!");
    }

    public void setUnfocusedMonthDateColor(int color) {
        throw new RuntimeException("Stub!");
    }

    public int getUnfocusedMonthDateColor() {
        throw new RuntimeException("Stub!");
    }

    public void setWeekNumberColor(int color) {
        throw new RuntimeException("Stub!");
    }

    public int getWeekNumberColor() {
        throw new RuntimeException("Stub!");
    }

    public void setWeekSeparatorLineColor(int color) {
        throw new RuntimeException("Stub!");
    }

    public int getWeekSeparatorLineColor() {
        throw new RuntimeException("Stub!");
    }

    public void setSelectedDateVerticalBar(int resourceId) {
        throw new RuntimeException("Stub!");
    }

    public void setSelectedDateVerticalBar(Drawable drawable) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getSelectedDateVerticalBar() {
        throw new RuntimeException("Stub!");
    }

    public void setWeekDayTextAppearance(int resourceId) {
        throw new RuntimeException("Stub!");
    }

    public int getWeekDayTextAppearance() {
        throw new RuntimeException("Stub!");
    }

    public void setDateTextAppearance(int resourceId) {
        throw new RuntimeException("Stub!");
    }

    public int getDateTextAppearance() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean isEnabled() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration newConfig) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }

    public long getMinDate() {
        throw new RuntimeException("Stub!");
    }

    public void setMinDate(long minDate) {
        throw new RuntimeException("Stub!");
    }

    public long getMaxDate() {
        throw new RuntimeException("Stub!");
    }

    public void setMaxDate(long maxDate) {
        throw new RuntimeException("Stub!");
    }

    public void setShowWeekNumber(boolean showWeekNumber) {
        throw new RuntimeException("Stub!");
    }

    public boolean getShowWeekNumber() {
        throw new RuntimeException("Stub!");
    }

    public int getFirstDayOfWeek() {
        throw new RuntimeException("Stub!");
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        throw new RuntimeException("Stub!");
    }

    public void setOnDateChangeListener(OnDateChangeListener listener) {
        throw new RuntimeException("Stub!");
    }

    public long getDate() {
        throw new RuntimeException("Stub!");
    }

    public void setDate(long date) {
        throw new RuntimeException("Stub!");
    }

    public void setDate(long date, boolean animate, boolean center) {
        throw new RuntimeException("Stub!");
    }
}
