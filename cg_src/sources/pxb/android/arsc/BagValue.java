package pxb.android.arsc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/arsc/BagValue.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/arsc/BagValue.class */
public class BagValue {
    public List<Map.Entry<Integer, Value>> map = new ArrayList();
    public final int parent;

    public BagValue(int parent) {
        this.parent = parent;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof BagValue)) {
            return false;
        }
        BagValue other = (BagValue) obj;
        if (this.map == null) {
            if (other.map != null) {
                return false;
            }
        } else if (!this.map.equals(other.map)) {
            return false;
        }
        if (this.parent != other.parent) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.map == null ? 0 : this.map.hashCode());
        return (31 * result) + this.parent;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("{bag%08x", Integer.valueOf(this.parent)));
        for (Map.Entry<Integer, Value> e : this.map) {
            sb.append(",").append(String.format("0x%08x", e.getKey()));
            sb.append("=");
            sb.append(e.getValue());
        }
        return sb.append("}").toString();
    }
}
