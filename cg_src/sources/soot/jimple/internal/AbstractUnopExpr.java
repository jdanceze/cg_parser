package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Value;
import soot.ValueBox;
import soot.jimple.UnopExpr;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractUnopExpr.class */
public abstract class AbstractUnopExpr implements UnopExpr {
    protected final ValueBox opBox;

    @Override // soot.Value
    public abstract Object clone();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractUnopExpr(ValueBox opBox) {
        this.opBox = opBox;
    }

    @Override // soot.jimple.UnopExpr
    public Value getOp() {
        return this.opBox.getValue();
    }

    @Override // soot.jimple.UnopExpr
    public void setOp(Value op) {
        this.opBox.setValue(op);
    }

    @Override // soot.jimple.UnopExpr
    public ValueBox getOpBox() {
        return this.opBox;
    }

    @Override // soot.Value
    public final List<ValueBox> getUseBoxes() {
        List<ValueBox> list = new ArrayList<>(this.opBox.getValue().getUseBoxes());
        list.add(this.opBox);
        return list;
    }
}
