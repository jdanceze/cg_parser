package soot.tagkit;

import java.util.Arrays;
import soot.jimple.Constant;
/* loaded from: gencallgraphv3.jar:soot/tagkit/ConstantValueTag.class */
public abstract class ConstantValueTag implements Tag {
    protected final byte[] bytes;

    public abstract Constant getConstant();

    public abstract String toString();

    /* JADX INFO: Access modifiers changed from: protected */
    public ConstantValueTag(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        return this.bytes;
    }

    public int hashCode() {
        int result = (31 * 1) + Arrays.hashCode(this.bytes);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ConstantValueTag other = (ConstantValueTag) obj;
        return Arrays.equals(this.bytes, other.bytes);
    }
}
