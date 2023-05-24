package soot.tagkit;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AnnotationIntElem.class */
public class AnnotationIntElem extends AnnotationElem {
    private final int value;

    public AnnotationIntElem(int v, char kind, String name) {
        super(kind, name);
        this.value = v;
    }

    public AnnotationIntElem(int v, String name) {
        this(v, 'I', name);
    }

    public AnnotationIntElem(Byte v, String name) {
        this(v.byteValue(), 'B', name);
    }

    public AnnotationIntElem(Character v, String name) {
        this(v.charValue(), 'C', name);
    }

    public AnnotationIntElem(Short v, String name) {
        this(v.shortValue(), 'S', name);
    }

    @Override // soot.tagkit.AnnotationElem
    public String toString() {
        return String.valueOf(super.toString()) + " value: " + this.value;
    }

    public int getValue() {
        return this.value;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((IAnnotationElemTypeSwitch) sw).caseAnnotationIntElem(this);
    }

    @Override // soot.tagkit.AnnotationElem
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + this.value;
    }

    @Override // soot.tagkit.AnnotationElem
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        AnnotationIntElem other = (AnnotationIntElem) obj;
        return this.value == other.value;
    }
}
