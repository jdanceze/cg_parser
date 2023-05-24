package pxb.android;

import java.nio.ByteBuffer;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/StringItem.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/StringItem.class */
public class StringItem implements Item {
    public String data;
    public int dataOffset;
    public int index;

    public StringItem() {
    }

    public StringItem(String data) {
        this.data = data;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        StringItem other = (StringItem) obj;
        if (this.data == null) {
            if (other.data != null) {
                return false;
            }
            return true;
        } else if (!this.data.equals(other.data)) {
            return false;
        } else {
            return true;
        }
    }

    public int hashCode() {
        int result = (31 * 1) + (this.data == null ? 0 : this.data.hashCode());
        return result;
    }

    public String toString() {
        return String.format("S%04d %s", Integer.valueOf(this.index), this.data);
    }

    @Override // pxb.android.Item
    public void writeout(ByteBuffer out) {
        out.putInt(this.index);
    }
}
