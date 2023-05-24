package org.jf.dexlib2.base;

import com.google.common.primitives.Ints;
import java.util.Comparator;
import org.jf.dexlib2.iface.Annotation;
import org.jf.util.CollectionUtils;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/BaseAnnotation.class */
public abstract class BaseAnnotation implements Annotation {
    public static final Comparator<? super Annotation> BY_TYPE = new Comparator<Annotation>() { // from class: org.jf.dexlib2.base.BaseAnnotation.1
        @Override // java.util.Comparator
        public int compare(Annotation annotation1, Annotation annotation2) {
            return annotation1.getType().compareTo(annotation2.getType());
        }
    };

    @Override // org.jf.dexlib2.iface.Annotation
    public int hashCode() {
        int hashCode = getVisibility();
        return (((hashCode * 31) + getType().hashCode()) * 31) + getElements().hashCode();
    }

    @Override // org.jf.dexlib2.iface.Annotation
    public boolean equals(Object o) {
        if (o instanceof Annotation) {
            Annotation other = (Annotation) o;
            return getVisibility() == other.getVisibility() && getType().equals(other.getType()) && getElements().equals(other.getElements());
        }
        return false;
    }

    @Override // org.jf.dexlib2.iface.Annotation, java.lang.Comparable
    public int compareTo(Annotation o) {
        int res = Ints.compare(getVisibility(), o.getVisibility());
        if (res != 0) {
            return res;
        }
        int res2 = getType().compareTo(o.getType());
        return res2 != 0 ? res2 : CollectionUtils.compareAsSet(getElements(), o.getElements());
    }
}
