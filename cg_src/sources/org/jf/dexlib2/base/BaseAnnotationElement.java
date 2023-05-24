package org.jf.dexlib2.base;

import java.util.Comparator;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.AnnotationElement;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/BaseAnnotationElement.class */
public abstract class BaseAnnotationElement implements AnnotationElement {
    public static final Comparator<AnnotationElement> BY_NAME = new Comparator<AnnotationElement>() { // from class: org.jf.dexlib2.base.BaseAnnotationElement.1
        @Override // java.util.Comparator
        public int compare(@Nonnull AnnotationElement element1, @Nonnull AnnotationElement element2) {
            return element1.getName().compareTo(element2.getName());
        }
    };

    @Override // org.jf.dexlib2.iface.AnnotationElement
    public int hashCode() {
        int hashCode = getName().hashCode();
        return (hashCode * 31) + getValue().hashCode();
    }

    @Override // org.jf.dexlib2.iface.AnnotationElement
    public boolean equals(Object o) {
        if (o != null && (o instanceof AnnotationElement)) {
            AnnotationElement other = (AnnotationElement) o;
            return getName().equals(other.getName()) && getValue().equals(other.getValue());
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.jf.dexlib2.iface.AnnotationElement, java.lang.Comparable
    public int compareTo(AnnotationElement o) {
        int res = getName().compareTo(o.getName());
        return res != 0 ? res : getValue().compareTo(o.getValue());
    }
}
