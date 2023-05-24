package android.view.accessibility;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import java.util.List;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/accessibility/AccessibilityNodeInfo.class */
public class AccessibilityNodeInfo implements Parcelable {
    public static final int ACTION_FOCUS = 1;
    public static final int ACTION_CLEAR_FOCUS = 2;
    public static final int ACTION_SELECT = 4;
    public static final int ACTION_CLEAR_SELECTION = 8;
    public static final int ACTION_CLICK = 16;
    public static final int ACTION_LONG_CLICK = 32;
    public static final int ACTION_ACCESSIBILITY_FOCUS = 64;
    public static final int ACTION_CLEAR_ACCESSIBILITY_FOCUS = 128;
    public static final int ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 256;
    public static final int ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 512;
    public static final int ACTION_NEXT_HTML_ELEMENT = 1024;
    public static final int ACTION_PREVIOUS_HTML_ELEMENT = 2048;
    public static final int ACTION_SCROLL_FORWARD = 4096;
    public static final int ACTION_SCROLL_BACKWARD = 8192;
    public static final String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";
    public static final String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";
    public static final int FOCUS_INPUT = 1;
    public static final int FOCUS_ACCESSIBILITY = 2;
    public static final int MOVEMENT_GRANULARITY_CHARACTER = 1;
    public static final int MOVEMENT_GRANULARITY_WORD = 2;
    public static final int MOVEMENT_GRANULARITY_LINE = 4;
    public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 8;
    public static final int MOVEMENT_GRANULARITY_PAGE = 16;
    public static final Parcelable.Creator<AccessibilityNodeInfo> CREATOR = null;

    AccessibilityNodeInfo() {
        throw new RuntimeException("Stub!");
    }

    public void setSource(View source) {
        throw new RuntimeException("Stub!");
    }

    public void setSource(View root, int virtualDescendantId) {
        throw new RuntimeException("Stub!");
    }

    public AccessibilityNodeInfo findFocus(int focus) {
        throw new RuntimeException("Stub!");
    }

    public AccessibilityNodeInfo focusSearch(int direction) {
        throw new RuntimeException("Stub!");
    }

    public int getWindowId() {
        throw new RuntimeException("Stub!");
    }

    public int getChildCount() {
        throw new RuntimeException("Stub!");
    }

    public AccessibilityNodeInfo getChild(int index) {
        throw new RuntimeException("Stub!");
    }

    public void addChild(View child) {
        throw new RuntimeException("Stub!");
    }

    public void addChild(View root, int virtualDescendantId) {
        throw new RuntimeException("Stub!");
    }

    public int getActions() {
        throw new RuntimeException("Stub!");
    }

    public void addAction(int action) {
        throw new RuntimeException("Stub!");
    }

    public void setMovementGranularities(int granularities) {
        throw new RuntimeException("Stub!");
    }

    public int getMovementGranularities() {
        throw new RuntimeException("Stub!");
    }

    public boolean performAction(int action) {
        throw new RuntimeException("Stub!");
    }

    public boolean performAction(int action, Bundle arguments) {
        throw new RuntimeException("Stub!");
    }

    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String text) {
        throw new RuntimeException("Stub!");
    }

    public AccessibilityNodeInfo getParent() {
        throw new RuntimeException("Stub!");
    }

    public void setParent(View parent) {
        throw new RuntimeException("Stub!");
    }

    public void setParent(View root, int virtualDescendantId) {
        throw new RuntimeException("Stub!");
    }

    public void getBoundsInParent(Rect outBounds) {
        throw new RuntimeException("Stub!");
    }

    public void setBoundsInParent(Rect bounds) {
        throw new RuntimeException("Stub!");
    }

    public void getBoundsInScreen(Rect outBounds) {
        throw new RuntimeException("Stub!");
    }

    public void setBoundsInScreen(Rect bounds) {
        throw new RuntimeException("Stub!");
    }

    public boolean isCheckable() {
        throw new RuntimeException("Stub!");
    }

    public void setCheckable(boolean checkable) {
        throw new RuntimeException("Stub!");
    }

    public boolean isChecked() {
        throw new RuntimeException("Stub!");
    }

    public void setChecked(boolean checked) {
        throw new RuntimeException("Stub!");
    }

    public boolean isFocusable() {
        throw new RuntimeException("Stub!");
    }

    public void setFocusable(boolean focusable) {
        throw new RuntimeException("Stub!");
    }

    public boolean isFocused() {
        throw new RuntimeException("Stub!");
    }

    public void setFocused(boolean focused) {
        throw new RuntimeException("Stub!");
    }

    public boolean isVisibleToUser() {
        throw new RuntimeException("Stub!");
    }

    public void setVisibleToUser(boolean visibleToUser) {
        throw new RuntimeException("Stub!");
    }

    public boolean isAccessibilityFocused() {
        throw new RuntimeException("Stub!");
    }

    public void setAccessibilityFocused(boolean focused) {
        throw new RuntimeException("Stub!");
    }

    public boolean isSelected() {
        throw new RuntimeException("Stub!");
    }

    public void setSelected(boolean selected) {
        throw new RuntimeException("Stub!");
    }

    public boolean isClickable() {
        throw new RuntimeException("Stub!");
    }

    public void setClickable(boolean clickable) {
        throw new RuntimeException("Stub!");
    }

    public boolean isLongClickable() {
        throw new RuntimeException("Stub!");
    }

    public void setLongClickable(boolean longClickable) {
        throw new RuntimeException("Stub!");
    }

    public boolean isEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean isPassword() {
        throw new RuntimeException("Stub!");
    }

    public void setPassword(boolean password) {
        throw new RuntimeException("Stub!");
    }

    public boolean isScrollable() {
        throw new RuntimeException("Stub!");
    }

    public void setScrollable(boolean scrollable) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getPackageName() {
        throw new RuntimeException("Stub!");
    }

    public void setPackageName(CharSequence packageName) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getClassName() {
        throw new RuntimeException("Stub!");
    }

    public void setClassName(CharSequence className) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getText() {
        throw new RuntimeException("Stub!");
    }

    public void setText(CharSequence text) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getContentDescription() {
        throw new RuntimeException("Stub!");
    }

    public void setContentDescription(CharSequence contentDescription) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        throw new RuntimeException("Stub!");
    }

    public static AccessibilityNodeInfo obtain(View source) {
        throw new RuntimeException("Stub!");
    }

    public static AccessibilityNodeInfo obtain(View root, int virtualDescendantId) {
        throw new RuntimeException("Stub!");
    }

    public static AccessibilityNodeInfo obtain() {
        throw new RuntimeException("Stub!");
    }

    public static AccessibilityNodeInfo obtain(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }

    public void recycle() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object object) {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
