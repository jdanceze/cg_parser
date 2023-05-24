package soot.tagkit;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AnnotationClassElem.class */
public class AnnotationClassElem extends AnnotationElem {
    private final String desc;

    public AnnotationClassElem(String typeName, String elementName) {
        this(typeName, 'c', elementName);
    }

    public AnnotationClassElem(String s, char kind, String name) {
        super(kind, name);
        this.desc = s;
    }

    @Override // soot.tagkit.AnnotationElem
    public String toString() {
        return String.valueOf(super.toString()) + " decription: " + this.desc;
    }

    public String getDesc() {
        return this.desc;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((IAnnotationElemTypeSwitch) sw).caseAnnotationClassElem(this);
    }

    @Override // soot.tagkit.AnnotationElem
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.desc == null ? 0 : this.desc.hashCode());
    }

    @Override // soot.tagkit.AnnotationElem
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        AnnotationClassElem other = (AnnotationClassElem) obj;
        if (this.desc == null) {
            if (other.desc != null) {
                return false;
            }
            return true;
        } else if (!this.desc.equals(other.desc)) {
            return false;
        } else {
            return true;
        }
    }
}
