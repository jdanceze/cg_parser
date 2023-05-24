package soot.jimple.spark.fieldrw;

import java.util.Set;
import soot.SootField;
import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/fieldrw/FieldRWTag.class */
public abstract class FieldRWTag implements Tag {
    private final String fieldNames;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldRWTag(Set<SootField> fields) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (SootField field : fields) {
            if (first) {
                first = false;
            } else {
                sb.append('%');
            }
            sb.append(field.getDeclaringClass().getName());
            sb.append(':');
            sb.append(field.getName());
        }
        this.fieldNames = sb.toString();
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        byte[] bytes = this.fieldNames.getBytes();
        byte[] ret = new byte[bytes.length + 2];
        ret[0] = (byte) (bytes.length / 256);
        ret[1] = (byte) (bytes.length % 256);
        System.arraycopy(bytes, 0, ret, 2, bytes.length);
        return ret;
    }

    public String toString() {
        return String.valueOf(getName()) + this.fieldNames;
    }
}
