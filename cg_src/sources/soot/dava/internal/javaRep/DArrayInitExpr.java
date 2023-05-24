package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DArrayInitExpr.class */
public class DArrayInitExpr implements Value {
    ValueBox[] elements;
    Type type;

    public DArrayInitExpr(ValueBox[] elements, Type type) {
        this.elements = elements;
        this.type = type;
    }

    @Override // soot.Value
    public List<ValueBox> getUseBoxes() {
        ValueBox[] valueBoxArr;
        List<ValueBox> list = new ArrayList<>();
        for (ValueBox element : this.elements) {
            list.addAll(element.getValue().getUseBoxes());
            list.add(element);
        }
        return list;
    }

    @Override // soot.Value
    public Object clone() {
        return this;
    }

    @Override // soot.Value
    public Type getType() {
        return this.type;
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.literal("{");
        for (int i = 0; i < this.elements.length; i++) {
            this.elements[i].toString(up);
            if (i + 1 < this.elements.length) {
                up.literal(" , ");
            }
        }
        up.literal("}");
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("{");
        for (int i = 0; i < this.elements.length; i++) {
            b.append(this.elements[i].toString());
            if (i + 1 < this.elements.length) {
                b.append(" , ");
            }
        }
        b.append("}");
        return b.toString();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        ValueBox[] valueBoxArr;
        int toReturn = 0;
        for (ValueBox element : this.elements) {
            toReturn += element.getValue().equivHashCode();
        }
        return toReturn;
    }

    public ValueBox[] getElements() {
        return this.elements;
    }
}
