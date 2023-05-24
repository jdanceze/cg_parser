package pxb.android.axml;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import pxb.android.ResConst;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:axml-2.1.0-SNAPSHOT.jar:pxb/android/axml/AxmlParser.class
 */
/* loaded from: gencallgraphv3.jar:axml-2.1.3.jar:pxb/android/axml/AxmlParser.class */
public class AxmlParser implements ResConst {
    public static final int END_FILE = 7;
    public static final int END_NS = 5;
    public static final int END_TAG = 3;
    public static final int START_FILE = 1;
    public static final int START_NS = 4;
    public static final int START_TAG = 2;
    public static final int TEXT = 6;
    private int attributeCount;
    private IntBuffer attrs;
    private int classAttribute;
    private int fileSize;
    private int idAttribute;
    private ByteBuffer in;
    private int lineNumber;
    private int nameIdx;
    private int nsIdx;
    private int prefixIdx;
    private int[] resourceIds;
    private String[] strings;
    private int styleAttribute;
    private int textIdx;

    public AxmlParser(byte[] data) {
        this(ByteBuffer.wrap(data));
    }

    public AxmlParser(ByteBuffer in) {
        this.fileSize = -1;
        this.in = in.order(ByteOrder.LITTLE_ENDIAN);
    }

    public int getAttrCount() {
        return this.attributeCount;
    }

    public int getAttributeCount() {
        return this.attributeCount;
    }

    public String getAttrName(int i) {
        int idx = this.attrs.get((i * 5) + 1);
        if (idx >= 0 && idx < this.strings.length) {
            return this.strings[idx];
        }
        return null;
    }

    public String getAttrNs(int i) {
        int idx = this.attrs.get((i * 5) + 0);
        if (idx >= 0 && idx < this.strings.length) {
            return this.strings[idx];
        }
        return null;
    }

    String getAttrRawString(int i) {
        int idx = this.attrs.get((i * 5) + 2);
        if (idx >= 0 && idx < this.strings.length) {
            return this.strings[idx];
        }
        return null;
    }

    public int getAttrResId(int i) {
        int idx;
        if (this.resourceIds != null && (idx = this.attrs.get((i * 5) + 1)) >= 0 && idx < this.resourceIds.length) {
            return this.resourceIds[idx];
        }
        return -1;
    }

    public int getAttrType(int i) {
        return this.attrs.get((i * 5) + 3) >> 24;
    }

    public Object getAttrValue(int i) {
        int v = this.attrs.get((i * 5) + 4);
        if (i == this.idAttribute) {
            return ValueWrapper.wrapId(v, getAttrRawString(i));
        }
        if (i == this.styleAttribute) {
            return ValueWrapper.wrapStyle(v, getAttrRawString(i));
        }
        if (i == this.classAttribute) {
            return ValueWrapper.wrapClass(v, getAttrRawString(i));
        }
        switch (getAttrType(i)) {
            case 3:
                return this.strings[v];
            case 18:
                return Boolean.valueOf(v != 0);
            default:
                return Integer.valueOf(v);
        }
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public String getName() {
        if (this.nameIdx >= 0 && this.nameIdx < this.strings.length) {
            return this.strings[this.nameIdx];
        }
        return null;
    }

    public String getNamespacePrefix() {
        if (this.prefixIdx >= 0 && this.prefixIdx < this.strings.length) {
            return this.strings[this.prefixIdx];
        }
        return null;
    }

    public String getNamespaceUri() {
        if (this.nsIdx < 0 && this.nsIdx >= this.strings.length) {
            return this.strings[this.nsIdx];
        }
        return null;
    }

    public String getText() {
        if (this.textIdx >= 0 && this.textIdx < this.strings.length) {
            return this.strings[this.textIdx];
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0219, code lost:
        r5.in.position(r7 + r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0226, code lost:
        return r6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int next() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 565
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: pxb.android.axml.AxmlParser.next():int");
    }
}
