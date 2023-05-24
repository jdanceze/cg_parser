package soot.tagkit;

import java.util.ArrayList;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AnnotationArrayElem.class */
public class AnnotationArrayElem extends AnnotationElem {
    private final ArrayList<AnnotationElem> values;

    public AnnotationArrayElem(ArrayList<AnnotationElem> types, String elemName) {
        this(types, '[', elemName);
    }

    public AnnotationArrayElem(ArrayList<AnnotationElem> t, char kind, String name) {
        super(kind, name);
        this.values = t;
    }

    @Override // soot.tagkit.AnnotationElem
    public String toString() {
        return String.valueOf(super.toString()) + " values: " + this.values.toString();
    }

    public ArrayList<AnnotationElem> getValues() {
        return this.values;
    }

    public int getNumValues() {
        if (this.values == null) {
            return 0;
        }
        return this.values.size();
    }

    public AnnotationElem getValueAt(int i) {
        return this.values.get(i);
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((IAnnotationElemTypeSwitch) sw).caseAnnotationArrayElem(this);
    }

    @Override // soot.tagkit.AnnotationElem
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.values == null ? 0 : this.values.hashCode());
    }

    @Override // soot.tagkit.AnnotationElem
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        AnnotationArrayElem other = (AnnotationArrayElem) obj;
        if (this.values == null) {
            if (other.values != null) {
                return false;
            }
            return true;
        } else if (!this.values.equals(other.values)) {
            return false;
        } else {
            return true;
        }
    }
}
