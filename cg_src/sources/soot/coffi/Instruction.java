package soot.coffi;

import android.text.Spanned;
/* loaded from: gencallgraphv3.jar:soot/coffi/Instruction.class */
abstract class Instruction implements Cloneable {
    public static final String argsep = " ";
    public static final String LOCALPREFIX = "local_";
    public byte code;
    public int label;
    public String name;
    public Instruction prev;
    public boolean labelled;
    public Instruction[] succs;
    int originalIndex;
    public Instruction next = null;
    public boolean branches = false;
    public boolean calls = false;
    public boolean returns = false;

    public abstract int parse(byte[] bArr, int i);

    public abstract int compile(byte[] bArr, int i);

    public Instruction(byte c) {
        this.code = c;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        return String.valueOf(this.label) + ": " + this.name + "[" + this.originalIndex + "]";
    }

    public void offsetToPointer(ByteCode bc) {
    }

    public int nextOffset(int curr) {
        return curr + 1;
    }

    public Instruction[] branchpoints(Instruction next) {
        return null;
    }

    public void markCPRefs(boolean[] refs) {
    }

    public void redirectCPRefs(short[] redirect) {
    }

    public int hashCode() {
        return new Integer(this.label).hashCode();
    }

    public boolean equals(Instruction i) {
        return this == i;
    }

    public static short getShort(byte[] bc, int index) {
        short s = (short) (((bc[index] << 8) & 65280) | (bc[index + 1] & 255));
        return s;
    }

    public static int getInt(byte[] bc, int index) {
        int bhh = (bc[index] << 24) & (-16777216);
        int bhl = (bc[index + 1] << 16) & Spanned.SPAN_PRIORITY;
        int blh = (bc[index + 2] << 8) & 65280;
        int bll = bc[index + 3] & 255;
        int i = bhh | bhl | blh | bll;
        return i;
    }

    public static int shortToBytes(short s, byte[] bc, int index) {
        int index2 = index + 1;
        bc[index] = (byte) ((s >> 8) & 255);
        int index3 = index2 + 1;
        bc[index2] = (byte) (s & 255);
        return index3;
    }

    public static int intToBytes(int s, byte[] bc, int index) {
        int index2 = index + 1;
        bc[index] = (byte) ((s >> 24) & 255);
        int index3 = index2 + 1;
        bc[index2] = (byte) ((s >> 16) & 255);
        int index4 = index3 + 1;
        bc[index3] = (byte) ((s >> 8) & 255);
        int index5 = index4 + 1;
        bc[index4] = (byte) (s & 255);
        return index5;
    }

    public String toString(cp_info[] constant_pool) {
        int i = this.code & 255;
        if (this.name == null) {
            this.name = "null???=" + Integer.toString(i);
        }
        return this.name;
    }
}
