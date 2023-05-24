package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/MultiAutoCompleteTextView.class */
public class MultiAutoCompleteTextView extends AutoCompleteTextView {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/MultiAutoCompleteTextView$Tokenizer.class */
    public interface Tokenizer {
        int findTokenStart(CharSequence charSequence, int i);

        int findTokenEnd(CharSequence charSequence, int i);

        CharSequence terminateToken(CharSequence charSequence);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/widget/MultiAutoCompleteTextView$CommaTokenizer.class */
    public static class CommaTokenizer implements Tokenizer {
        public CommaTokenizer() {
            throw new RuntimeException("Stub!");
        }

        @Override // android.widget.MultiAutoCompleteTextView.Tokenizer
        public int findTokenStart(CharSequence text, int cursor) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.widget.MultiAutoCompleteTextView.Tokenizer
        public int findTokenEnd(CharSequence text, int cursor) {
            throw new RuntimeException("Stub!");
        }

        @Override // android.widget.MultiAutoCompleteTextView.Tokenizer
        public CharSequence terminateToken(CharSequence text) {
            throw new RuntimeException("Stub!");
        }
    }

    public MultiAutoCompleteTextView(Context context) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public MultiAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public MultiAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(null, null, 0);
        throw new RuntimeException("Stub!");
    }

    public void setTokenizer(Tokenizer t) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AutoCompleteTextView
    protected void performFiltering(CharSequence text, int keyCode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AutoCompleteTextView
    public boolean enoughToFilter() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AutoCompleteTextView
    public void performValidation() {
        throw new RuntimeException("Stub!");
    }

    protected void performFiltering(CharSequence text, int start, int end, int keyCode) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.AutoCompleteTextView
    protected void replaceText(CharSequence text) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.EditText, android.widget.TextView, android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.widget.EditText, android.widget.TextView, android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        throw new RuntimeException("Stub!");
    }
}
