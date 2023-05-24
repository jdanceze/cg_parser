package soot.tagkit;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AnnotationStringElem.class */
public class AnnotationStringElem extends AnnotationElem {
    private final String value;

    public AnnotationStringElem(String s, String name) {
        this(s, 's', name);
    }

    public AnnotationStringElem(String s, char kind, String name) {
        super(kind, name);
        this.value = s;
    }

    @Override // soot.tagkit.AnnotationElem
    public String toString() {
        return String.valueOf(super.toString()) + " value: " + this.value;
    }

    public String getValue() {
        return this.value;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((IAnnotationElemTypeSwitch) sw).caseAnnotationStringElem(this);
    }

    @Override // soot.tagkit.AnnotationElem
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.value == null ? 0 : this.value.hashCode());
    }

    @Override // soot.tagkit.AnnotationElem
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        AnnotationStringElem other = (AnnotationStringElem) obj;
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
            return true;
        } else if (!this.value.equals(other.value)) {
            return false;
        } else {
            return true;
        }
    }
}
