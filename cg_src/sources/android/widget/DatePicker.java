package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/DatePicker.class */
public class DatePicker extends FrameLayout {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/DatePicker$OnDateChangedListener.class */
    public interface OnDateChangedListener {
        void onDateChanged(DatePicker datePicker, int i, int i2, int i3);
    }

    public DatePicker(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
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

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean isEnabled() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
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

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration newConfig) {
        throw new RuntimeException("Stub!");
    }

    public boolean getCalendarViewShown() {
        throw new RuntimeException("Stub!");
    }

    public CalendarView getCalendarView() {
        throw new RuntimeException("Stub!");
    }

    public void setCalendarViewShown(boolean shown) {
        throw new RuntimeException("Stub!");
    }

    public boolean getSpinnersShown() {
        throw new RuntimeException("Stub!");
    }

    public void setSpinnersShown(boolean shown) {
        throw new RuntimeException("Stub!");
    }

    public void updateDate(int year, int month, int dayOfMonth) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable state) {
        throw new RuntimeException("Stub!");
    }

    public void init(int year, int monthOfYear, int dayOfMonth, OnDateChangedListener onDateChangedListener) {
        throw new RuntimeException("Stub!");
    }

    public int getYear() {
        throw new RuntimeException("Stub!");
    }

    public int getMonth() {
        throw new RuntimeException("Stub!");
    }

    public int getDayOfMonth() {
        throw new RuntimeException("Stub!");
    }
}
