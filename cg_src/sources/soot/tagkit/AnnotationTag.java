package soot.tagkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AnnotationTag.class */
public class AnnotationTag implements Tag {
    public static final String NAME = "AnnotationTag";
    private final String type;
    private List<AnnotationElem> elems;

    public AnnotationTag(String type) {
        this.type = type;
        this.elems = null;
    }

    public AnnotationTag(String type, Collection<AnnotationElem> elements) {
        this.type = type;
        if (elements == null || elements.isEmpty()) {
            this.elems = null;
        } else if (elements instanceof List) {
            this.elems = (List) elements;
        } else {
            this.elems = new ArrayList(elements);
        }
    }

    @Deprecated
    public AnnotationTag(String type, int numElem) {
        this.type = type;
        this.elems = new ArrayList(numElem);
    }

    public String toString() {
        if (this.elems != null) {
            StringBuilder sb = new StringBuilder("Annotation: type: ");
            sb.append(this.type).append(" num elems: ").append(this.elems.size()).append(" elems: ");
            for (AnnotationElem next : this.elems) {
                sb.append('\n').append(next);
            }
            sb.append('\n');
            return sb.toString();
        }
        return "Annotation type: " + this.type + " without elements";
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    public String getInfo() {
        return "Annotation";
    }

    public String getType() {
        return this.type;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("AnnotationTag has no value for bytecode");
    }

    public void addElem(AnnotationElem elem) {
        if (this.elems == null) {
            this.elems = new ArrayList();
        }
        this.elems.add(elem);
    }

    public void setElems(List<AnnotationElem> list) {
        this.elems = list;
    }

    public Collection<AnnotationElem> getElems() {
        return this.elems == null ? Collections.emptyList() : Collections.unmodifiableCollection(this.elems);
    }

    public int hashCode() {
        int result = (31 * 1) + (this.elems == null ? 0 : this.elems.hashCode());
        return (31 * result) + (this.type == null ? 0 : this.type.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AnnotationTag other = (AnnotationTag) obj;
        if (this.elems == null) {
            if (other.elems != null) {
                return false;
            }
        } else if (!this.elems.equals(other.elems)) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
            return true;
        } else if (!this.type.equals(other.type)) {
            return false;
        } else {
            return true;
        }
    }
}
