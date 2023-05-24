package android.text;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/TextWatcher.class */
public interface TextWatcher extends NoCopySpan {
    void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3);

    void onTextChanged(CharSequence charSequence, int i, int i2, int i3);

    void afterTextChanged(Editable editable);
}
