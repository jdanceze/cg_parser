package soot.tagkit;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AnnotationFloatElem.class */
public class AnnotationFloatElem extends AnnotationElem {
    private final float value;

    public AnnotationFloatElem(float v, String name) {
        this(v, 'F', name);
    }

    public AnnotationFloatElem(float v, char kind, String name) {
        super(kind, name);
        this.value = v;
    }

    @Override // soot.tagkit.AnnotationElem
    public String toString() {
        return String.valueOf(super.toString()) + " value: " + this.value;
    }

    public float getValue() {
        return this.value;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((IAnnotationElemTypeSwitch) sw).caseAnnotationFloatElem(this);
    }

    @Override // soot.tagkit.AnnotationElem
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + Float.floatToIntBits(this.value);
    }

    @Override // soot.tagkit.AnnotationElem
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        AnnotationFloatElem other = (AnnotationFloatElem) obj;
        return Float.floatToIntBits(this.value) == Float.floatToIntBits(other.value);
    }
}
