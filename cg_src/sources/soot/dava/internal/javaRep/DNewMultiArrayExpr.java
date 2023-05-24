package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.ArrayType;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.coffi.Instruction;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractNewMultiArrayExpr;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DNewMultiArrayExpr.class */
public class DNewMultiArrayExpr extends AbstractNewMultiArrayExpr {
    public DNewMultiArrayExpr(ArrayType type, List sizes) {
        super(type, new ValueBox[sizes.size()]);
        for (int i = 0; i < sizes.size(); i++) {
            this.sizeBoxes[i] = Grimp.v().newExprBox((Value) sizes.get(i));
        }
    }

    @Override // soot.jimple.internal.AbstractNewMultiArrayExpr, soot.Value
    public Object clone() {
        List clonedSizes = new ArrayList(getSizeCount());
        for (int i = 0; i < getSizeCount(); i++) {
            clonedSizes.add(i, Grimp.cloneIfNecessary(getSize(i)));
        }
        return new DNewMultiArrayExpr(getBaseType(), clonedSizes);
    }

    @Override // soot.jimple.internal.AbstractNewMultiArrayExpr, soot.Value
    public void toString(UnitPrinter up) {
        ValueBox[] valueBoxArr;
        up.literal("new");
        up.literal(Instruction.argsep);
        up.type(getBaseType().baseType);
        for (ValueBox element : this.sizeBoxes) {
            up.literal("[");
            element.toString(up);
            up.literal("]");
        }
        for (int i = getSizeCount(); i < getBaseType().numDimensions; i++) {
            up.literal("[]");
        }
    }

    @Override // soot.jimple.internal.AbstractNewMultiArrayExpr
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("new " + getBaseType().baseType);
        getSizes();
        Iterator it = getSizes().iterator();
        while (it.hasNext()) {
            buffer.append("[" + it.next().toString() + "]");
        }
        for (int i = getSizeCount(); i < getBaseType().numDimensions; i++) {
            buffer.append("[]");
        }
        return buffer.toString();
    }
}
