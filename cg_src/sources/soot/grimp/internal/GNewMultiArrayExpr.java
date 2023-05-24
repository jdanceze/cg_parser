package soot.grimp.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import soot.ArrayType;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.jimple.internal.AbstractNewMultiArrayExpr;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/GNewMultiArrayExpr.class */
public class GNewMultiArrayExpr extends AbstractNewMultiArrayExpr {
    public GNewMultiArrayExpr(ArrayType type, List<? extends Value> sizes) {
        super(type, new ValueBox[sizes.size()]);
        Grimp grmp = Grimp.v();
        ListIterator<? extends Value> it = sizes.listIterator();
        while (it.hasNext()) {
            Value v = it.next();
            this.sizeBoxes[it.previousIndex()] = grmp.newExprBox(v);
        }
    }

    @Override // soot.jimple.internal.AbstractNewMultiArrayExpr, soot.Value
    public Object clone() {
        ValueBox[] boxes = this.sizeBoxes;
        List<Value> clonedSizes = new ArrayList<>(boxes.length);
        for (ValueBox vb : boxes) {
            clonedSizes.add(Grimp.cloneIfNecessary(vb.getValue()));
        }
        return new GNewMultiArrayExpr(getBaseType(), clonedSizes);
    }
}
