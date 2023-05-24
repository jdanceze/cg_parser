package android.renderscript;

import android.content.res.AssetManager;
import android.content.res.Resources;
import java.io.File;
@Deprecated
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/FileA3D.class */
public class FileA3D extends BaseObj {

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/FileA3D$EntryType.class */
    public enum EntryType {
        MESH,
        UNKNOWN
    }

    @Deprecated
    /* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/renderscript/FileA3D$IndexEntry.class */
    public static class IndexEntry {
        IndexEntry() {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public String getName() {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public EntryType getEntryType() {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public BaseObj getObject() {
            throw new RuntimeException("Stub!");
        }

        @Deprecated
        public Mesh getMesh() {
            throw new RuntimeException("Stub!");
        }
    }

    FileA3D() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public int getIndexEntryCount() {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public IndexEntry getIndexEntry(int index) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static FileA3D createFromAsset(RenderScript rs, AssetManager mgr, String path) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static FileA3D createFromFile(RenderScript rs, String path) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static FileA3D createFromFile(RenderScript rs, File path) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public static FileA3D createFromResource(RenderScript rs, Resources res, int id) {
        throw new RuntimeException("Stub!");
    }
}
