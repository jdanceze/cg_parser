package javax.management;

import java.util.ArrayList;
import java.util.Collection;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/AttributeList.class */
public class AttributeList extends ArrayList {
    private static final long serialVersionUID = -4077085769279709076L;

    public AttributeList() {
    }

    public AttributeList(int i) {
        super(i);
    }

    public AttributeList(AttributeList attributeList) {
        super(attributeList);
    }

    public void add(Attribute attribute) {
        super.add((AttributeList) attribute);
    }

    public void add(int i, Attribute attribute) {
        try {
            super.add(i, (int) attribute);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeOperationsException(e, "The specified index is out of range");
        }
    }

    public void set(int i, Attribute attribute) {
        try {
            super.set(i, (int) attribute);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeOperationsException(e, "The specified index is out of range");
        }
    }

    public boolean addAll(AttributeList attributeList) {
        return super.addAll((Collection) attributeList);
    }

    public boolean addAll(int i, AttributeList attributeList) {
        try {
            return super.addAll(i, (Collection) attributeList);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeOperationsException(e, "The specified index is out of range");
        }
    }
}
