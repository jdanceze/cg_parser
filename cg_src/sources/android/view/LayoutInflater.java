package android.view;

import android.content.Context;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/LayoutInflater.class */
public abstract class LayoutInflater {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/LayoutInflater$Factory.class */
    public interface Factory {
        View onCreateView(String str, Context context, AttributeSet attributeSet);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/LayoutInflater$Factory2.class */
    public interface Factory2 extends Factory {
        View onCreateView(View view, String str, Context context, AttributeSet attributeSet);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/view/LayoutInflater$Filter.class */
    public interface Filter {
        boolean onLoadClass(Class cls);
    }

    public abstract LayoutInflater cloneInContext(Context context);

    protected LayoutInflater(Context context) {
        throw new RuntimeException("Stub!");
    }

    protected LayoutInflater(LayoutInflater original, Context newContext) {
        throw new RuntimeException("Stub!");
    }

    public static LayoutInflater from(Context context) {
        throw new RuntimeException("Stub!");
    }

    public Context getContext() {
        throw new RuntimeException("Stub!");
    }

    public final Factory getFactory() {
        throw new RuntimeException("Stub!");
    }

    public final Factory2 getFactory2() {
        throw new RuntimeException("Stub!");
    }

    public void setFactory(Factory factory) {
        throw new RuntimeException("Stub!");
    }

    public void setFactory2(Factory2 factory) {
        throw new RuntimeException("Stub!");
    }

    public Filter getFilter() {
        throw new RuntimeException("Stub!");
    }

    public void setFilter(Filter filter) {
        throw new RuntimeException("Stub!");
    }

    public View inflate(int resource, ViewGroup root) {
        throw new RuntimeException("Stub!");
    }

    public View inflate(XmlPullParser parser, ViewGroup root) {
        throw new RuntimeException("Stub!");
    }

    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        throw new RuntimeException("Stub!");
    }

    public View inflate(XmlPullParser parser, ViewGroup root, boolean attachToRoot) {
        throw new RuntimeException("Stub!");
    }

    public final View createView(String name, String prefix, AttributeSet attrs) throws ClassNotFoundException, InflateException {
        throw new RuntimeException("Stub!");
    }

    protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        throw new RuntimeException("Stub!");
    }

    protected View onCreateView(View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
        throw new RuntimeException("Stub!");
    }
}
