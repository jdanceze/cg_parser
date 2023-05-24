package android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/ListPopupWindow.class */
public class ListPopupWindow {
    public static final int POSITION_PROMPT_ABOVE = 0;
    public static final int POSITION_PROMPT_BELOW = 1;
    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;
    public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
    public static final int INPUT_METHOD_NEEDED = 1;
    public static final int INPUT_METHOD_NOT_NEEDED = 2;

    public ListPopupWindow(Context context) {
        throw new RuntimeException("Stub!");
    }

    public ListPopupWindow(Context context, AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    public ListPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        throw new RuntimeException("Stub!");
    }

    public ListPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        throw new RuntimeException("Stub!");
    }

    public void setAdapter(ListAdapter adapter) {
        throw new RuntimeException("Stub!");
    }

    public void setPromptPosition(int position) {
        throw new RuntimeException("Stub!");
    }

    public int getPromptPosition() {
        throw new RuntimeException("Stub!");
    }

    public void setModal(boolean modal) {
        throw new RuntimeException("Stub!");
    }

    public boolean isModal() {
        throw new RuntimeException("Stub!");
    }

    public void setSoftInputMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    public int getSoftInputMode() {
        throw new RuntimeException("Stub!");
    }

    public void setListSelector(Drawable selector) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getBackground() {
        throw new RuntimeException("Stub!");
    }

    public void setBackgroundDrawable(Drawable d) {
        throw new RuntimeException("Stub!");
    }

    public void setAnimationStyle(int animationStyle) {
        throw new RuntimeException("Stub!");
    }

    public int getAnimationStyle() {
        throw new RuntimeException("Stub!");
    }

    public View getAnchorView() {
        throw new RuntimeException("Stub!");
    }

    public void setAnchorView(View anchor) {
        throw new RuntimeException("Stub!");
    }

    public int getHorizontalOffset() {
        throw new RuntimeException("Stub!");
    }

    public void setHorizontalOffset(int offset) {
        throw new RuntimeException("Stub!");
    }

    public int getVerticalOffset() {
        throw new RuntimeException("Stub!");
    }

    public void setVerticalOffset(int offset) {
        throw new RuntimeException("Stub!");
    }

    public int getWidth() {
        throw new RuntimeException("Stub!");
    }

    public void setWidth(int width) {
        throw new RuntimeException("Stub!");
    }

    public void setContentWidth(int width) {
        throw new RuntimeException("Stub!");
    }

    public int getHeight() {
        throw new RuntimeException("Stub!");
    }

    public void setHeight(int height) {
        throw new RuntimeException("Stub!");
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener clickListener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener selectedListener) {
        throw new RuntimeException("Stub!");
    }

    public void setPromptView(View prompt) {
        throw new RuntimeException("Stub!");
    }

    public void postShow() {
        throw new RuntimeException("Stub!");
    }

    public void show() {
        throw new RuntimeException("Stub!");
    }

    public void dismiss() {
        throw new RuntimeException("Stub!");
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setInputMethodMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    public int getInputMethodMode() {
        throw new RuntimeException("Stub!");
    }

    public void setSelection(int position) {
        throw new RuntimeException("Stub!");
    }

    public void clearListSelection() {
        throw new RuntimeException("Stub!");
    }

    public boolean isShowing() {
        throw new RuntimeException("Stub!");
    }

    public boolean isInputMethodNotNeeded() {
        throw new RuntimeException("Stub!");
    }

    public boolean performItemClick(int position) {
        throw new RuntimeException("Stub!");
    }

    public Object getSelectedItem() {
        throw new RuntimeException("Stub!");
    }

    public int getSelectedItemPosition() {
        throw new RuntimeException("Stub!");
    }

    public long getSelectedItemId() {
        throw new RuntimeException("Stub!");
    }

    public View getSelectedView() {
        throw new RuntimeException("Stub!");
    }

    public ListView getListView() {
        throw new RuntimeException("Stub!");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }
}
