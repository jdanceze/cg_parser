package soot.tagkit;

import soot.util.Switchable;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AnnotationElem.class */
public abstract class AnnotationElem implements Switchable {
    private final char kind;
    private String name;

    public AnnotationElem(char k, String name) {
        this.kind = k;
        this.name = name;
    }

    public String toString() {
        return "Annotation Element: kind: " + this.kind + " name: " + this.name;
    }

    public char getKind() {
        return this.kind;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int hashCode() {
        int result = (31 * 1) + this.kind;
        return (31 * result) + (this.name == null ? 0 : this.name.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AnnotationElem other = (AnnotationElem) obj;
        if (this.kind != other.kind) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
            return true;
        } else if (!this.name.equals(other.name)) {
            return false;
        } else {
            return true;
        }
    }
}
