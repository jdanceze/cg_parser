package soot.tagkit;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AnnotationEnumElem.class */
public class AnnotationEnumElem extends AnnotationElem {
    private String typeName;
    private String constantName;

    public AnnotationEnumElem(String typeName, String constantName, String elementName) {
        this(typeName, constantName, 'e', elementName);
    }

    public AnnotationEnumElem(String t, String c, char kind, String name) {
        super(kind, name);
        this.typeName = t;
        this.constantName = c;
    }

    @Override // soot.tagkit.AnnotationElem
    public String toString() {
        return String.valueOf(super.toString()) + " type name: " + this.typeName + " constant name: " + this.constantName;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String newValue) {
        this.typeName = newValue;
    }

    public String getConstantName() {
        return this.constantName;
    }

    public void setConstantName(String newValue) {
        this.constantName = newValue;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((IAnnotationElemTypeSwitch) sw).caseAnnotationEnumElem(this);
    }

    @Override // soot.tagkit.AnnotationElem
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * result) + (this.constantName == null ? 0 : this.constantName.hashCode()))) + (this.typeName == null ? 0 : this.typeName.hashCode());
    }

    @Override // soot.tagkit.AnnotationElem
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        AnnotationEnumElem other = (AnnotationEnumElem) obj;
        if (this.constantName == null) {
            if (other.constantName != null) {
                return false;
            }
        } else if (!this.constantName.equals(other.constantName)) {
            return false;
        }
        if (this.typeName == null) {
            if (other.typeName != null) {
                return false;
            }
            return true;
        } else if (!this.typeName.equals(other.typeName)) {
            return false;
        } else {
            return true;
        }
    }
}
