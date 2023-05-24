package com.sun.xml.bind.v2.runtime.output;

import com.sun.istack.FinalArrayList;
import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.bind.v2.runtime.Name;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/C14nXmlOutput.class */
public class C14nXmlOutput extends UTF8XmlOutput {
    private StaticAttribute[] staticAttributes;
    private int len;
    private int[] nsBuf;
    private final FinalArrayList<DynamicAttribute> otherAttributes;
    private final boolean namedAttributesAreOrdered;

    public C14nXmlOutput(OutputStream out, Encoded[] localNames, boolean namedAttributesAreOrdered, CharacterEscapeHandler escapeHandler) {
        super(out, localNames, escapeHandler);
        this.staticAttributes = new StaticAttribute[8];
        this.len = 0;
        this.nsBuf = new int[8];
        this.otherAttributes = new FinalArrayList<>();
        this.namedAttributesAreOrdered = namedAttributesAreOrdered;
        for (int i = 0; i < this.staticAttributes.length; i++) {
            this.staticAttributes[i] = new StaticAttribute();
        }
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/C14nXmlOutput$StaticAttribute.class */
    final class StaticAttribute implements Comparable<StaticAttribute> {
        Name name;
        String value;

        StaticAttribute() {
        }

        public void set(Name name, String value) {
            this.name = name;
            this.value = value;
        }

        void write() throws IOException {
            C14nXmlOutput.super.attribute(this.name, this.value);
        }

        DynamicAttribute toDynamicAttribute() {
            int prefix;
            int nsUriIndex = this.name.nsUriIndex;
            if (nsUriIndex == -1) {
                prefix = -1;
            } else {
                prefix = C14nXmlOutput.this.nsUriIndex2prefixIndex[nsUriIndex];
            }
            return new DynamicAttribute(prefix, this.name.localName, this.value);
        }

        @Override // java.lang.Comparable
        public int compareTo(StaticAttribute that) {
            return this.name.compareTo(that.name);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/C14nXmlOutput$DynamicAttribute.class */
    public final class DynamicAttribute implements Comparable<DynamicAttribute> {
        final int prefix;
        final String localName;
        final String value;

        public DynamicAttribute(int prefix, String localName, String value) {
            this.prefix = prefix;
            this.localName = localName;
            this.value = value;
        }

        private String getURI() {
            return this.prefix == -1 ? "" : C14nXmlOutput.this.nsContext.getNamespaceURI(this.prefix);
        }

        @Override // java.lang.Comparable
        public int compareTo(DynamicAttribute that) {
            int r = getURI().compareTo(that.getURI());
            return r != 0 ? r : this.localName.compareTo(that.localName);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void attribute(Name name, String value) throws IOException {
        if (this.staticAttributes.length == this.len) {
            int newLen = this.len * 2;
            StaticAttribute[] newbuf = new StaticAttribute[newLen];
            System.arraycopy(this.staticAttributes, 0, newbuf, 0, this.len);
            for (int i = this.len; i < newLen; i++) {
                this.staticAttributes[i] = new StaticAttribute();
            }
            this.staticAttributes = newbuf;
        }
        StaticAttribute[] staticAttributeArr = this.staticAttributes;
        int i2 = this.len;
        this.len = i2 + 1;
        staticAttributeArr[i2].set(name, value);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void attribute(int prefix, String localName, String value) throws IOException {
        this.otherAttributes.add(new DynamicAttribute(prefix, localName, value));
    }

    @Override // com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput, com.sun.xml.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void endStartTag() throws IOException {
        if (this.otherAttributes.isEmpty()) {
            if (this.len != 0) {
                if (!this.namedAttributesAreOrdered) {
                    Arrays.sort(this.staticAttributes, 0, this.len);
                }
                for (int i = 0; i < this.len; i++) {
                    this.staticAttributes[i].write();
                }
                this.len = 0;
            }
        } else {
            for (int i2 = 0; i2 < this.len; i2++) {
                this.otherAttributes.add(this.staticAttributes[i2].toDynamicAttribute());
            }
            this.len = 0;
            Collections.sort(this.otherAttributes);
            int size = this.otherAttributes.size();
            for (int i3 = 0; i3 < size; i3++) {
                DynamicAttribute a = this.otherAttributes.get(i3);
                super.attribute(a.prefix, a.localName, a.value);
            }
            this.otherAttributes.clear();
        }
        super.endStartTag();
    }

    @Override // com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput
    protected void writeNsDecls(int base) throws IOException {
        int count = this.nsContext.getCurrent().count();
        if (count == 0) {
            return;
        }
        if (count > this.nsBuf.length) {
            this.nsBuf = new int[count];
        }
        for (int i = count - 1; i >= 0; i--) {
            this.nsBuf[i] = base + i;
        }
        for (int i2 = 0; i2 < count; i2++) {
            for (int j = i2 + 1; j < count; j++) {
                String p = this.nsContext.getPrefix(this.nsBuf[i2]);
                String q = this.nsContext.getPrefix(this.nsBuf[j]);
                if (p.compareTo(q) > 0) {
                    int t = this.nsBuf[j];
                    this.nsBuf[j] = this.nsBuf[i2];
                    this.nsBuf[i2] = t;
                }
            }
        }
        for (int i3 = 0; i3 < count; i3++) {
            writeNsDecl(this.nsBuf[i3]);
        }
    }
}
