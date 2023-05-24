package android.graphics.drawable;

import android.content.res.Resources;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/drawable/StateListDrawable.class */
public class StateListDrawable extends DrawableContainer {
    public StateListDrawable() {
        throw new RuntimeException("Stub!");
    }

    public void addState(int[] stateSet, Drawable drawable) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    public boolean isStateful() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] stateSet) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    public Drawable mutate() {
        throw new RuntimeException("Stub!");
    }
}
