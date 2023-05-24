package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Value;
import soot.ValueBox;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractOpStmt.class */
public abstract class AbstractOpStmt extends AbstractStmt {
    protected final ValueBox opBox;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractOpStmt(ValueBox opBox) {
        this.opBox = opBox;
    }

    public final Value getOp() {
        return this.opBox.getValue();
    }

    public final void setOp(Value op) {
        this.opBox.setValue(op);
    }

    public final ValueBox getOpBox() {
        return this.opBox;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public final List<ValueBox> getUseBoxes() {
        List<ValueBox> list = new ArrayList<>(this.opBox.getValue().getUseBoxes());
        list.add(this.opBox);
        return list;
    }
}
