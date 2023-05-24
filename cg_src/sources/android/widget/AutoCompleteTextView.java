package android.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.widget.AdapterView;
import android.widget.Filter;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AutoCompleteTextView.class */
public class AutoCompleteTextView extends EditText implements Filter.FilterListener {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/AutoCompleteTextView$Validator.class */
    public interface Validator {
        boolean isValid(CharSequence charSequence);

        CharSequence fixText(CharSequence charSequence);
    }

    public AutoCompleteTextView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setCompletionHint(CharSequence hint) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getCompletionHint() {
        throw new RuntimeException("Stub!");
    }

    public int getDropDownWidth() {
        throw new RuntimeException("Stub!");
    }

    public void setDropDownWidth(int width) {
        throw new RuntimeException("Stub!");
    }

    public int getDropDownHeight() {
        throw new RuntimeException("Stub!");
    }

    public void setDropDownHeight(int height) {
        throw new RuntimeException("Stub!");
    }

    public int getDropDownAnchor() {
        throw new RuntimeException("Stub!");
    }

    public void setDropDownAnchor(int id) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getDropDownBackground() {
        throw new RuntimeException("Stub!");
    }

    public void setDropDownBackgroundDrawable(Drawable d) {
        throw new RuntimeException("Stub!");
    }

    public void setDropDownBackgroundResource(int id) {
        throw new RuntimeException("Stub!");
    }

    public void setDropDownVerticalOffset(int offset) {
        throw new RuntimeException("Stub!");
    }

    public int getDropDownVerticalOffset() {
        throw new RuntimeException("Stub!");
    }

    public void setDropDownHorizontalOffset(int offset) {
        throw new RuntimeException("Stub!");
    }

    public int getDropDownHorizontalOffset() {
        throw new RuntimeException("Stub!");
    }

    public int getThreshold() {
        throw new RuntimeException("Stub!");
    }

    public void setThreshold(int threshold) {
        throw new RuntimeException("Stub!");
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        throw new RuntimeException("Stub!");
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener l) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public AdapterView.OnItemClickListener getItemClickListener() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public AdapterView.OnItemSelectedListener getItemSelectedListener() {
        throw new RuntimeException("Stub!");
    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        throw new RuntimeException("Stub!");
    }

    public AdapterView.OnItemSelectedListener getOnItemSelectedListener() {
        throw new RuntimeException("Stub!");
    }

    public ListAdapter getAdapter() {
        throw new RuntimeException("Stub!");
    }

    public <T extends ListAdapter & Filterable> void setAdapter(T adapter) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    public boolean enoughToFilter() {
        throw new RuntimeException("Stub!");
    }

    public boolean isPopupShowing() {
        throw new RuntimeException("Stub!");
    }

    protected CharSequence convertSelectionToString(Object selectedItem) {
        throw new RuntimeException("Stub!");
    }

    public void clearListSelection() {
        throw new RuntimeException("Stub!");
    }

    public void setListSelection(int position) {
        throw new RuntimeException("Stub!");
    }

    public int getListSelection() {
        throw new RuntimeException("Stub!");
    }

    protected void performFiltering(CharSequence text, int keyCode) {
        throw new RuntimeException("Stub!");
    }

    public void performCompletion() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView
    public void onCommitCompletion(CompletionInfo completion) {
        throw new RuntimeException("Stub!");
    }

    public boolean isPerformingCompletion() {
        throw new RuntimeException("Stub!");
    }

    protected void replaceText(CharSequence text) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.Filter.FilterListener
    public void onFilterComplete(int count) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onDisplayHint(int hint) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }

    public void dismissDropDown() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.TextView
    protected boolean setFrame(int l, int t, int r, int b) {
        throw new RuntimeException("Stub!");
    }

    public void showDropDown() {
        throw new RuntimeException("Stub!");
    }

    public void setValidator(Validator validator) {
        throw new RuntimeException("Stub!");
    }

    public Validator getValidator() {
        throw new RuntimeException("Stub!");
    }

    public void performValidation() {
        throw new RuntimeException("Stub!");
    }

    protected Filter getFilter() {
        throw new RuntimeException("Stub!");
    }
}
