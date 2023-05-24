package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import soot.ArrayType;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JNewMultiArrayExpr.class */
public class JNewMultiArrayExpr extends AbstractNewMultiArrayExpr {
    public JNewMultiArrayExpr(ArrayType type, List<? extends Value> sizes) {
        super(type, new ValueBox[sizes.size()]);
        Jimple jimp = Jimple.v();
        ListIterator<? extends Value> it = sizes.listIterator();
        while (it.hasNext()) {
            Value v = it.next();
            this.sizeBoxes[it.previousIndex()] = jimp.newImmediateBox(v);
        }
    }

    public JNewMultiArrayExpr(ArrayType type, ValueBox[] sizes) {
        super(type, sizes);
        for (int i = 0; i < sizes.length; i++) {
            ValueBox v = sizes[i];
            this.sizeBoxes[i] = v;
        }
    }

    @Override // soot.jimple.internal.AbstractNewMultiArrayExpr, soot.Value
    public Object clone() {
        ValueBox[] boxes = this.sizeBoxes;
        List<Value> clonedSizes = new ArrayList<>(boxes.length);
        for (ValueBox vb : boxes) {
            clonedSizes.add(Jimple.cloneIfNecessary(vb.getValue()));
        }
        return new JNewMultiArrayExpr(this.baseType, clonedSizes);
    }
}
