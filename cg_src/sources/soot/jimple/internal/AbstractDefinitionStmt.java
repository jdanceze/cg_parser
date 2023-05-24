package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.Value;
import soot.ValueBox;
import soot.jimple.DefinitionStmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractDefinitionStmt.class */
public abstract class AbstractDefinitionStmt extends AbstractStmt implements DefinitionStmt {
    protected final ValueBox leftBox;
    protected final ValueBox rightBox;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractDefinitionStmt(ValueBox leftBox, ValueBox rightBox) {
        this.leftBox = leftBox;
        this.rightBox = rightBox;
    }

    @Override // soot.jimple.DefinitionStmt
    public final Value getLeftOp() {
        return this.leftBox.getValue();
    }

    @Override // soot.jimple.DefinitionStmt
    public final Value getRightOp() {
        return this.rightBox.getValue();
    }

    @Override // soot.jimple.DefinitionStmt
    public final ValueBox getLeftOpBox() {
        return this.leftBox;
    }

    @Override // soot.jimple.DefinitionStmt
    public final ValueBox getRightOpBox() {
        return this.rightBox;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public final List<ValueBox> getDefBoxes() {
        return Collections.singletonList(this.leftBox);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public List<ValueBox> getUseBoxes() {
        List<ValueBox> list = new ArrayList<>();
        list.addAll(getLeftOp().getUseBoxes());
        list.add(this.rightBox);
        list.addAll(getRightOp().getUseBoxes());
        return list;
    }

    @Override // soot.Unit
    public boolean fallsThrough() {
        return true;
    }

    @Override // soot.Unit
    public boolean branches() {
        return false;
    }
}
