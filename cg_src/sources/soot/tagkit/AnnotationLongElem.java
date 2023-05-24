package soot.tagkit;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AnnotationLongElem.class */
public class AnnotationLongElem extends AnnotationElem {
    private final long value;

    public AnnotationLongElem(long v, String name) {
        this(v, 'J', name);
    }

    public AnnotationLongElem(long v, char kind, String name) {
        super(kind, name);
        this.value = v;
    }

    @Override // soot.tagkit.AnnotationElem
    public String toString() {
        return String.valueOf(super.toString()) + " value: " + this.value;
    }

    public long getValue() {
        return this.value;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((IAnnotationElemTypeSwitch) sw).caseAnnotationLongElem(this);
    }

    @Override // soot.tagkit.AnnotationElem
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + ((int) (this.value ^ (this.value >>> 32)));
    }

    @Override // soot.tagkit.AnnotationElem
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        AnnotationLongElem other = (AnnotationLongElem) obj;
        return this.value == other.value;
    }
}
