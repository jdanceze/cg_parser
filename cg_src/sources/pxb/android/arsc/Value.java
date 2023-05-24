package pxb.android.arsc;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/arsc/Value.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/arsc/Value.class */
public class Value {
    public final int data;
    public String raw;
    public final int type;

    public Value(int type, int data, String raw) {
        this.type = type;
        this.data = data;
        this.raw = raw;
    }

    public String toString() {
        if (this.type == 3) {
            return this.raw;
        }
        return String.format("{t=0x%02x d=0x%08x}", Integer.valueOf(this.type), Integer.valueOf(this.data));
    }
}
