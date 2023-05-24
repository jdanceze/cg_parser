package soot.dava.internal.javaRep;

import soot.ArrayType;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.coffi.Instruction;
import soot.grimp.Grimp;
import soot.grimp.Precedence;
import soot.jimple.internal.AbstractNewArrayExpr;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DNewArrayExpr.class */
public class DNewArrayExpr extends AbstractNewArrayExpr implements Precedence {
    public DNewArrayExpr(Type type, Value size) {
        super(type, Grimp.v().newExprBox(size));
    }

    @Override // soot.grimp.Precedence
    public int getPrecedence() {
        return 850;
    }

    @Override // soot.jimple.internal.AbstractNewArrayExpr, soot.Value
    public Object clone() {
        return new DNewArrayExpr(getBaseType(), Grimp.cloneIfNecessary(getSize()));
    }

    @Override // soot.jimple.internal.AbstractNewArrayExpr, soot.Value
    public void toString(UnitPrinter up) {
        up.literal("new");
        up.literal(Instruction.argsep);
        Type type = getBaseType();
        if (type instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) type;
            up.type(arrayType.baseType);
            up.literal("[");
            getSizeBox().toString(up);
            up.literal("]");
            for (int i = 0; i < arrayType.numDimensions; i++) {
                up.literal("[]");
            }
            return;
        }
        up.type(getBaseType());
        up.literal("[");
        getSizeBox().toString(up);
        up.literal("]");
    }

    @Override // soot.jimple.internal.AbstractNewArrayExpr
    public String toString() {
        return "new " + getBaseType() + "[" + getSize() + "]";
    }
}
