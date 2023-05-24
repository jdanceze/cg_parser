package pxb.android.arsc;

import java.util.Map;
import java.util.TreeMap;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/arsc/Config.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/arsc/Config.class */
public class Config {
    public final int entryCount;
    public final byte[] id;
    public Map<Integer, ResEntry> resources = new TreeMap();
    int wChunkSize;
    int wEntryStart;
    int wPosition;

    public Config(byte[] id, int entryCount) {
        this.id = id;
        this.entryCount = entryCount;
    }
}
