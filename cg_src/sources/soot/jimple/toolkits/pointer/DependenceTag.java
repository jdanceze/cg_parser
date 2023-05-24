package soot.jimple.toolkits.pointer;

import soot.tagkit.Tag;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/DependenceTag.class */
public class DependenceTag implements Tag {
    public static final String NAME = "DependenceTag";
    protected short read = -1;
    protected short write = -1;
    protected boolean callsNative = false;

    public boolean setCallsNative() {
        boolean ret = !this.callsNative;
        this.callsNative = true;
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setRead(short s) {
        this.read = s;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setWrite(short s) {
        this.write = s;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        byte[] ret = new byte[5];
        ret[0] = (byte) ((this.read >> 8) & 255);
        ret[1] = (byte) (this.read & 255);
        ret[2] = (byte) ((this.write >> 8) & 255);
        ret[3] = (byte) (this.write & 255);
        ret[4] = (byte) (this.callsNative ? 1 : 0);
        return ret;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (this.callsNative) {
            buf.append("SECallsNative\n");
        }
        if (this.read >= 0) {
            buf.append("SEReads : ").append((int) this.read).append('\n');
        }
        if (this.write >= 0) {
            buf.append("SEWrites: ").append((int) this.write).append('\n');
        }
        return buf.toString();
    }
}
