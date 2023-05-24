package android.text;

import android.graphics.drawable.Drawable;
import org.xml.sax.XMLReader;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/Html.class */
public class Html {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/Html$ImageGetter.class */
    public interface ImageGetter {
        Drawable getDrawable(String str);
    }

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/Html$TagHandler.class */
    public interface TagHandler {
        void handleTag(boolean z, String str, Editable editable, XMLReader xMLReader);
    }

    Html() {
        throw new RuntimeException("Stub!");
    }

    public static Spanned fromHtml(String source) {
        throw new RuntimeException("Stub!");
    }

    public static Spanned fromHtml(String source, ImageGetter imageGetter, TagHandler tagHandler) {
        throw new RuntimeException("Stub!");
    }

    public static String toHtml(Spanned text) {
        throw new RuntimeException("Stub!");
    }

    public static String escapeHtml(CharSequence text) {
        throw new RuntimeException("Stub!");
    }
}
