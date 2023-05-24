package org.xmlpull.mxp1;

import java.io.Reader;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/mxp1/MXParserCachingStrings.class */
public class MXParserCachingStrings extends MXParser implements Cloneable {
    protected static final boolean CACHE_STATISTICS = false;
    protected static final boolean TRACE_SIZING = false;
    protected static final int INITIAL_CAPACITY = 13;
    protected int cacheStatCalls;
    protected int cacheStatWalks;
    protected int cacheStatResets;
    protected int cacheStatRehash;
    protected static final int CACHE_LOAD = 77;
    protected int cacheEntriesCount;
    protected int cacheEntriesThreshold;
    protected char[][] keys;
    protected String[] values;

    public Object clone() throws CloneNotSupportedException {
        if (this.reader != null && !(this.reader instanceof Cloneable)) {
            throw new CloneNotSupportedException("reader used in parser must implement Cloneable!");
        }
        MXParserCachingStrings cloned = (MXParserCachingStrings) super.clone();
        if (this.reader != null) {
            try {
                Object o = this.reader.getClass().getMethod("clone", null).invoke(this.reader, null);
                cloned.reader = (Reader) o;
            } catch (Exception e) {
                CloneNotSupportedException ee = new CloneNotSupportedException(new StringBuffer().append("failed to call clone() on reader ").append(this.reader).append(":").append(e).toString());
                ee.initCause(e);
                throw ee;
            }
        }
        if (this.keys != null) {
            cloned.keys = (char[][]) this.keys.clone();
        }
        if (this.values != null) {
            cloned.values = (String[]) this.values.clone();
        }
        if (this.elRawName != null) {
            cloned.elRawName = cloneCCArr(this.elRawName);
        }
        if (this.elRawNameEnd != null) {
            cloned.elRawNameEnd = (int[]) this.elRawNameEnd.clone();
        }
        if (this.elRawNameLine != null) {
            cloned.elRawNameLine = (int[]) this.elRawNameLine.clone();
        }
        if (this.elName != null) {
            cloned.elName = (String[]) this.elName.clone();
        }
        if (this.elPrefix != null) {
            cloned.elPrefix = (String[]) this.elPrefix.clone();
        }
        if (this.elUri != null) {
            cloned.elUri = (String[]) this.elUri.clone();
        }
        if (this.elNamespaceCount != null) {
            cloned.elNamespaceCount = (int[]) this.elNamespaceCount.clone();
        }
        if (this.attributeName != null) {
            cloned.attributeName = (String[]) this.attributeName.clone();
        }
        if (this.attributeNameHash != null) {
            cloned.attributeNameHash = (int[]) this.attributeNameHash.clone();
        }
        if (this.attributePrefix != null) {
            cloned.attributePrefix = (String[]) this.attributePrefix.clone();
        }
        if (this.attributeUri != null) {
            cloned.attributeUri = (String[]) this.attributeUri.clone();
        }
        if (this.attributeValue != null) {
            cloned.attributeValue = (String[]) this.attributeValue.clone();
        }
        if (this.namespacePrefix != null) {
            cloned.namespacePrefix = (String[]) this.namespacePrefix.clone();
        }
        if (this.namespacePrefixHash != null) {
            cloned.namespacePrefixHash = (int[]) this.namespacePrefixHash.clone();
        }
        if (this.namespaceUri != null) {
            cloned.namespaceUri = (String[]) this.namespaceUri.clone();
        }
        if (this.entityName != null) {
            cloned.entityName = (String[]) this.entityName.clone();
        }
        if (this.entityNameBuf != null) {
            cloned.entityNameBuf = cloneCCArr(this.entityNameBuf);
        }
        if (this.entityNameHash != null) {
            cloned.entityNameHash = (int[]) this.entityNameHash.clone();
        }
        if (this.entityReplacementBuf != null) {
            cloned.entityReplacementBuf = cloneCCArr(this.entityReplacementBuf);
        }
        if (this.entityReplacement != null) {
            cloned.entityReplacement = (String[]) this.entityReplacement.clone();
        }
        if (this.buf != null) {
            cloned.buf = (char[]) this.buf.clone();
        }
        if (this.pc != null) {
            cloned.pc = (char[]) this.pc.clone();
        }
        if (this.charRefOneCharBuf != null) {
            cloned.charRefOneCharBuf = (char[]) this.charRefOneCharBuf.clone();
        }
        return cloned;
    }

    private char[][] cloneCCArr(char[][] ccarr) {
        char[][] cca = (char[][]) ccarr.clone();
        for (int i = 0; i < cca.length; i++) {
            if (cca[i] != null) {
                cca[i] = (char[]) cca[i].clone();
            }
        }
        return cca;
    }

    public MXParserCachingStrings() {
        this.allStringsInterned = true;
        initStringCache();
    }

    @Override // org.xmlpull.mxp1.MXParser, org.xmlpull.v1.XmlPullParser
    public void setFeature(String name, boolean state) throws XmlPullParserException {
        if ("http://xmlpull.org/v1/doc/features.html#names-interned".equals(name)) {
            if (this.eventType != 0) {
                throw new XmlPullParserException("interning names feature can only be changed before parsing", this, null);
            }
            this.allStringsInterned = state;
            if (state || this.keys == null) {
                return;
            }
            resetStringCache();
            return;
        }
        super.setFeature(name, state);
    }

    @Override // org.xmlpull.mxp1.MXParser, org.xmlpull.v1.XmlPullParser
    public boolean getFeature(String name) {
        if ("http://xmlpull.org/v1/doc/features.html#names-interned".equals(name)) {
            return this.allStringsInterned;
        }
        return super.getFeature(name);
    }

    public void finalize() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.xmlpull.mxp1.MXParser
    public String newString(char[] cbuf, int off, int len) {
        if (this.allStringsInterned) {
            return newStringIntern(cbuf, off, len);
        }
        return super.newString(cbuf, off, len);
    }

    @Override // org.xmlpull.mxp1.MXParser
    protected String newStringIntern(char[] cbuf, int off, int len) {
        char[] k;
        if (this.cacheEntriesCount >= this.cacheEntriesThreshold) {
            rehash();
        }
        int offset = MXParser.fastHash(cbuf, off, len) % this.keys.length;
        while (true) {
            k = this.keys[offset];
            if (k == null || keysAreEqual(k, 0, k.length, cbuf, off, len)) {
                break;
            }
            offset = (offset + 1) % this.keys.length;
        }
        if (k != null) {
            return this.values[offset];
        }
        char[] k2 = new char[len];
        System.arraycopy(cbuf, off, k2, 0, len);
        String v = new String(k2).intern();
        this.keys[offset] = k2;
        this.values[offset] = v;
        this.cacheEntriesCount++;
        return v;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [char[], char[][]] */
    protected void initStringCache() {
        if (this.keys == null) {
            this.cacheEntriesThreshold = 10;
            if (this.cacheEntriesThreshold >= 13) {
                throw new RuntimeException("internal error: threshold must be less than capacity: 13");
            }
            this.keys = new char[13];
            this.values = new String[13];
            this.cacheEntriesCount = 0;
        }
    }

    @Override // org.xmlpull.mxp1.MXParser
    protected void resetStringCache() {
        initStringCache();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v7, types: [char[], char[][]] */
    private void rehash() {
        int newSize = (2 * this.keys.length) + 1;
        this.cacheEntriesThreshold = (newSize * 77) / 100;
        if (this.cacheEntriesThreshold >= newSize) {
            throw new RuntimeException(new StringBuffer().append("internal error: threshold must be less than capacity: ").append(newSize).toString());
        }
        ?? r0 = new char[newSize];
        String[] newValues = new String[newSize];
        for (int i = 0; i < this.keys.length; i++) {
            char[] k = this.keys[i];
            this.keys[i] = null;
            String v = this.values[i];
            this.values[i] = null;
            if (k != null) {
                int newOffset = MXParser.fastHash(k, 0, k.length) % newSize;
                while (true) {
                    char[] newk = r0[newOffset];
                    if (newk != 0) {
                        if (keysAreEqual(newk, 0, newk.length, k, 0, k.length)) {
                            throw new RuntimeException(new StringBuffer().append("internal cache error: duplicated keys: ").append(new String(newk)).append(" and ").append(new String(k)).toString());
                        }
                        newOffset = (newOffset + 1) % newSize;
                    } else {
                        r0[newOffset] = k;
                        newValues[newOffset] = v;
                        break;
                    }
                }
            }
        }
        this.keys = r0;
        this.values = newValues;
    }

    private static final boolean keysAreEqual(char[] a, int astart, int alength, char[] b, int bstart, int blength) {
        if (alength != blength) {
            return false;
        }
        for (int i = 0; i < alength; i++) {
            if (a[astart + i] != b[bstart + i]) {
                return false;
            }
        }
        return true;
    }
}
