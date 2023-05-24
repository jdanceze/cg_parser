package soot.tagkit;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AnnotationBooleanElem.class */
public class AnnotationBooleanElem extends AnnotationElem {
    private final boolean value;

    public AnnotationBooleanElem(boolean v, String name) {
        this(v, 'Z', name);
    }

    public AnnotationBooleanElem(boolean v, char kind, String name) {
        super(kind, name);
        this.value = v;
    }

    @Override // soot.tagkit.AnnotationElem
    public String toString() {
        return String.valueOf(super.toString()) + " value: " + this.value;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((IAnnotationElemTypeSwitch) sw).caseAnnotationBooleanElem(this);
    }

    @Override // soot.tagkit.AnnotationElem
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.value ? 1231 : 1237);
    }

    @Override // soot.tagkit.AnnotationElem
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        AnnotationBooleanElem other = (AnnotationBooleanElem) obj;
        return this.value == other.value;
    }
}
