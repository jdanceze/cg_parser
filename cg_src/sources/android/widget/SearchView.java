package android.widget;

import android.app.SearchableInfo;
import android.content.Context;
import android.util.AttributeSet;
import android.view.CollapsibleActionView;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SearchView.class */
public class SearchView extends LinearLayout implements CollapsibleActionView {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SearchView$OnCloseListener.class */
    public interface OnCloseListener {
        boolean onClose();
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SearchView$OnQueryTextListener.class */
    public interface OnQueryTextListener {
        boolean onQueryTextSubmit(String str);

        boolean onQueryTextChange(String str);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/SearchView$OnSuggestionListener.class */
    public interface OnSuggestionListener {
        boolean onSuggestionSelect(int i);

        boolean onSuggestionClick(int i);
    }

    public SearchView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setSearchableInfo(SearchableInfo searchable) {
        throw new RuntimeException("Stub!");
    }

    public void setImeOptions(int imeOptions) {
        throw new RuntimeException("Stub!");
    }

    public int getImeOptions() {
        throw new RuntimeException("Stub!");
    }

    public void setInputType(int inputType) {
        throw new RuntimeException("Stub!");
    }

    public int getInputType() {
        throw new RuntimeException("Stub!");
    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnCloseListener(OnCloseListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnQueryTextFocusChangeListener(View.OnFocusChangeListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnSuggestionListener(OnSuggestionListener listener) {
        throw new RuntimeException("Stub!");
    }

    public void setOnSearchClickListener(View.OnClickListener listener) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getQuery() {
        throw new RuntimeException("Stub!");
    }

    public void setQuery(CharSequence query, boolean submit) {
        throw new RuntimeException("Stub!");
    }

    public void setQueryHint(CharSequence hint) {
        throw new RuntimeException("Stub!");
    }

    public CharSequence getQueryHint() {
        throw new RuntimeException("Stub!");
    }

    public void setIconifiedByDefault(boolean iconified) {
        throw new RuntimeException("Stub!");
    }

    public boolean isIconfiedByDefault() {
        throw new RuntimeException("Stub!");
    }

    public void setIconified(boolean iconify) {
        throw new RuntimeException("Stub!");
    }

    public boolean isIconified() {
        throw new RuntimeException("Stub!");
    }

    public void setSubmitButtonEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean isSubmitButtonEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setQueryRefinementEnabled(boolean enable) {
        throw new RuntimeException("Stub!");
    }

    public boolean isQueryRefinementEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setSuggestionsAdapter(CursorAdapter adapter) {
        throw new RuntimeException("Stub!");
    }

    public CursorAdapter getSuggestionsAdapter() {
        throw new RuntimeException("Stub!");
    }

    public void setMaxWidth(int maxpixels) {
        throw new RuntimeException("Stub!");
    }

    public int getMaxWidth() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.CollapsibleActionView
    public void onActionViewCollapsed() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.view.CollapsibleActionView
    public void onActionViewExpanded() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
