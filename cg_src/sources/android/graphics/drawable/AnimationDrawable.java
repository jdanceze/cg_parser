package android.graphics.drawable;

import android.content.res.Resources;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/graphics/drawable/AnimationDrawable.class */
public class AnimationDrawable extends DrawableContainer implements Runnable, Animatable {
    public AnimationDrawable() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    public boolean setVisible(boolean visible, boolean restart) {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Animatable
    public void start() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Animatable
    public void stop() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Animatable
    public boolean isRunning() {
        throw new RuntimeException("Stub!");
    }

    @Override // java.lang.Runnable
    public void run() {
        throw new RuntimeException("Stub!");
    }

    @Override // android.graphics.drawable.Drawable
    public void unscheduleSelf(Runnable what) {
        throw new RuntimeException("Stub!");
    }

    public int getNumberOfFrames() {
        throw new RuntimeException("Stub!");
    }

    public Drawable getFrame(int index) {
        throw new RuntimeException("Stub!");
    }

    public int getDuration(int i) {
        throw new RuntimeException("Stub!");
    }

    public boolean isOneShot() {
        throw new RuntimeException("Stub!");
    }

    public void setOneShot(boolean oneShot) {
        throw new RuntimeException("Stub!");
    }

    public void addFrame(Drawable frame, int duration) {
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
