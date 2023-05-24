package android.renderscript;

import android.content.res.Resources;
import java.io.File;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Font.class */
public class Font extends BaseObj {

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/Font$Style.class */
    public enum Style {
        BOLD,
        BOLD_ITALIC,
        ITALIC,
        NORMAL
    }

    Font() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static Font createFromFile(RenderScript rs, Resources res, String path, float pointSize) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static Font createFromFile(RenderScript rs, Resources res, File path, float pointSize) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static Font createFromAsset(RenderScript rs, Resources res, String path, float pointSize) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static Font createFromResource(RenderScript rs, Resources res, int id, float pointSize) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static Font create(RenderScript rs, Resources res, String familyName, Style fontStyle, float pointSize) {
        throw new RuntimeException("Stub!");
    }
}
