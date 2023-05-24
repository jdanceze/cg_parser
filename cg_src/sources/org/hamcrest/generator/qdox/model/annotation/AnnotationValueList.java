package org.hamcrest.generator.qdox.model.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/AnnotationValueList.class */
public class AnnotationValueList implements AnnotationValue {
    private final List valueList;

    public AnnotationValueList(List valueList) {
        this.valueList = valueList;
    }

    public List getValueList() {
        return this.valueList;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("{");
        int pos = buf.length();
        ListIterator i = this.valueList.listIterator();
        while (i.hasNext()) {
            buf.append(i.next().toString());
            buf.append(", ");
        }
        if (buf.length() > pos) {
            buf.setLength(buf.length() - 2);
        }
        buf.append("}");
        return buf.toString();
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object accept(AnnotationVisitor visitor) {
        return visitor.visitAnnotationValueList(this);
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationValue
    public Object getParameterValue() {
        List list = new ArrayList();
        ListIterator i = this.valueList.listIterator();
        while (i.hasNext()) {
            AnnotationValue value = (AnnotationValue) i.next();
            list.add(value.getParameterValue());
        }
        return list;
    }
}
