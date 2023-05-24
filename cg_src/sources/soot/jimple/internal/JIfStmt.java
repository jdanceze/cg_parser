package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.coffi.Instruction;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.BinopExpr;
import soot.jimple.ConvertToBaf;
import soot.jimple.EqExpr;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.LeExpr;
import soot.jimple.LtExpr;
import soot.jimple.NeExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JIfStmt.class */
public class JIfStmt extends AbstractStmt implements IfStmt {
    protected final ValueBox conditionBox;
    protected final UnitBox targetBox;
    protected final List<UnitBox> targetBoxes;

    public JIfStmt(Value condition, Unit target) {
        this(condition, Jimple.v().newStmtBox(target));
    }

    public JIfStmt(Value condition, UnitBox target) {
        this(Jimple.v().newConditionExprBox(condition), target);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JIfStmt(ValueBox conditionBox, UnitBox targetBox) {
        this.conditionBox = conditionBox;
        this.targetBox = targetBox;
        this.targetBoxes = Collections.singletonList(targetBox);
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new JIfStmt(Jimple.cloneIfNecessary(getCondition()), getTarget());
    }

    public String toString() {
        Unit t = getTarget();
        String target = t.branches() ? "(branch)" : t.toString();
        return "if " + getCondition().toString() + Instruction.argsep + Jimple.GOTO + Instruction.argsep + target;
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        up.literal("if ");
        this.conditionBox.toString(up);
        up.literal(" goto ");
        this.targetBox.toString(up);
    }

    @Override // soot.jimple.IfStmt
    public Value getCondition() {
        return this.conditionBox.getValue();
    }

    @Override // soot.jimple.IfStmt
    public void setCondition(Value condition) {
        this.conditionBox.setValue(condition);
    }

    @Override // soot.jimple.IfStmt
    public ValueBox getConditionBox() {
        return this.conditionBox;
    }

    @Override // soot.jimple.IfStmt
    public Stmt getTarget() {
        return (Stmt) this.targetBox.getUnit();
    }

    @Override // soot.jimple.IfStmt
    public void setTarget(Unit target) {
        this.targetBox.setUnit(target);
    }

    @Override // soot.jimple.IfStmt
    public UnitBox getTargetBox() {
        return this.targetBox;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public List<ValueBox> getUseBoxes() {
        List<ValueBox> useBoxes = new ArrayList<>(this.conditionBox.getValue().getUseBoxes());
        useBoxes.add(this.conditionBox);
        return useBoxes;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public final List<UnitBox> getUnitBoxes() {
        return this.targetBoxes;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((StmtSwitch) sw).caseIfStmt(this);
    }

    @Override // soot.jimple.internal.AbstractStmt, soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, final List<Unit> out) {
        Unit u;
        BinopExpr cond = (BinopExpr) getCondition();
        final Value op1 = cond.getOp1();
        Value op2 = cond.getOp2();
        context.setCurrentUnit(this);
        if ((op2 instanceof NullConstant) || (op1 instanceof NullConstant)) {
            if (op2 instanceof NullConstant) {
                ((ConvertToBaf) op1).convertToBaf(context, out);
            } else {
                ((ConvertToBaf) op2).convertToBaf(context, out);
            }
            if (cond instanceof EqExpr) {
                u = Baf.v().newIfNullInst(Baf.v().newPlaceholderInst(getTarget()));
            } else if (cond instanceof NeExpr) {
                u = Baf.v().newIfNonNullInst(Baf.v().newPlaceholderInst(getTarget()));
            } else {
                throw new RuntimeException("invalid condition");
            }
            u.addAllTagsOf(this);
            out.add(u);
        } else if ((op2 instanceof IntConstant) && ((IntConstant) op2).value == 0) {
            ((ConvertToBaf) op1).convertToBaf(context, out);
            cond.apply(new AbstractJimpleValueSwitch() { // from class: soot.jimple.internal.JIfStmt.1
                private void add(Unit u2) {
                    u2.addAllTagsOf(JIfStmt.this);
                    out.add(u2);
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseEqExpr(EqExpr expr) {
                    add(Baf.v().newIfEqInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseNeExpr(NeExpr expr) {
                    add(Baf.v().newIfNeInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseLtExpr(LtExpr expr) {
                    add(Baf.v().newIfLtInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseLeExpr(LeExpr expr) {
                    add(Baf.v().newIfLeInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseGtExpr(GtExpr expr) {
                    add(Baf.v().newIfGtInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseGeExpr(GeExpr expr) {
                    add(Baf.v().newIfGeInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }
            });
        } else if ((op1 instanceof IntConstant) && ((IntConstant) op1).value == 0) {
            ((ConvertToBaf) op2).convertToBaf(context, out);
            cond.apply(new AbstractJimpleValueSwitch() { // from class: soot.jimple.internal.JIfStmt.2
                private void add(Unit u2) {
                    u2.addAllTagsOf(JIfStmt.this);
                    out.add(u2);
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseEqExpr(EqExpr expr) {
                    add(Baf.v().newIfEqInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseNeExpr(NeExpr expr) {
                    add(Baf.v().newIfNeInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseLtExpr(LtExpr expr) {
                    add(Baf.v().newIfGtInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseLeExpr(LeExpr expr) {
                    add(Baf.v().newIfGeInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseGtExpr(GtExpr expr) {
                    add(Baf.v().newIfLtInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseGeExpr(GeExpr expr) {
                    add(Baf.v().newIfLeInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }
            });
        } else {
            ((ConvertToBaf) op1).convertToBaf(context, out);
            ((ConvertToBaf) op2).convertToBaf(context, out);
            cond.apply(new AbstractJimpleValueSwitch() { // from class: soot.jimple.internal.JIfStmt.3
                private void add(Unit u2) {
                    u2.addAllTagsOf(JIfStmt.this);
                    out.add(u2);
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseEqExpr(EqExpr expr) {
                    add(Baf.v().newIfCmpEqInst(op1.getType(), Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseNeExpr(NeExpr expr) {
                    add(Baf.v().newIfCmpNeInst(op1.getType(), Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseLtExpr(LtExpr expr) {
                    add(Baf.v().newIfCmpLtInst(op1.getType(), Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseLeExpr(LeExpr expr) {
                    add(Baf.v().newIfCmpLeInst(op1.getType(), Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseGtExpr(GtExpr expr) {
                    add(Baf.v().newIfCmpGtInst(op1.getType(), Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }

                @Override // soot.jimple.AbstractExprSwitch, soot.jimple.ExprSwitch
                public void caseGeExpr(GeExpr expr) {
                    add(Baf.v().newIfCmpGeInst(op1.getType(), Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
                }
            });
        }
    }

    @Override // soot.Unit
    public boolean fallsThrough() {
        return true;
    }

    @Override // soot.Unit
    public boolean branches() {
        return true;
    }
}
