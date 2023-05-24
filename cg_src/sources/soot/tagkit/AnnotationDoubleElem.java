package soot.tagkit;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AnnotationDoubleElem.class */
public class AnnotationDoubleElem extends AnnotationElem {
    private final double value;

    public AnnotationDoubleElem(double v, String name) {
        this(v, 'D', name);
    }

    public AnnotationDoubleElem(double v, char kind, String name) {
        super(kind, name);
        this.value = v;
    }

    @Override // soot.tagkit.AnnotationElem
    public String toString() {
        return String.valueOf(super.toString()) + " value: " + this.value;
    }

    public double getValue() {
        return this.value;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((IAnnotationElemTypeSwitch) sw).caseAnnotationDoubleElem(this);
    }

    @Override // soot.tagkit.AnnotationElem
    public int hashCode() {
        int result = super.hashCode();
        long temp = Double.doubleToLongBits(this.value);
        return (31 * result) + ((int) (temp ^ (temp >>> 32)));
    }

    @Override // soot.tagkit.AnnotationElem
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        AnnotationDoubleElem other = (AnnotationDoubleElem) obj;
        return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(other.value);
    }
}
