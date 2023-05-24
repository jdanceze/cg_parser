package android.content;

import android.net.Uri;
import java.util.ArrayList;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/Entity.class */
public final class Entity {

    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/content/Entity$NamedContentValues.class */
    public static class NamedContentValues {
        public final Uri uri;
        public final ContentValues values;

        public NamedContentValues(Uri uri, ContentValues values) {
            throw new RuntimeException("Stub!");
        }
    }

    public Entity(ContentValues values) {
        throw new RuntimeException("Stub!");
    }

    public ContentValues getEntityValues() {
        throw new RuntimeException("Stub!");
    }

    public ArrayList<NamedContentValues> getSubValues() {
        throw new RuntimeException("Stub!");
    }

    public void addSubValue(Uri uri, ContentValues values) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
