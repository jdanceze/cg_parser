package soot.asm;

import java.util.ArrayList;
import soot.Local;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/asm/StackFrame.class */
final class StackFrame {
    private Operand[] out;
    private Local[] inStackLocals;
    private ValueBox[] boxes;
    private ArrayList<Operand[]> in;
    private final AsmMethodSource src;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StackFrame.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StackFrame(AsmMethodSource src) {
        this.src = src;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Operand[] out() {
        return this.out;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void in(Operand... oprs) {
        ArrayList<Operand[]> in = this.in;
        if (in == null) {
            ArrayList<Operand[]> arrayList = new ArrayList<>(1);
            this.in = arrayList;
            in = arrayList;
        } else {
            in.clear();
        }
        in.add(oprs);
        this.inStackLocals = new Local[oprs.length];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void boxes(ValueBox... boxes) {
        this.boxes = boxes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void out(Operand... oprs) {
        this.out = oprs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void mergeIn(Operand... oprs) {
        ArrayList<Operand[]> in = this.in;
        if (in.get(0).length != oprs.length) {
            throw new IllegalArgumentException("Invalid in operands length!");
        }
        int nrIn = in.size();
        boolean diff = false;
        for (int i = 0; i != oprs.length; i++) {
            Operand newOp = oprs[i];
            diff = true;
            Local stack = this.inStackLocals[i];
            if (stack != null) {
                if (newOp.stack == null) {
                    newOp.stack = stack;
                    AssignStmt as = Jimple.v().newAssignStmt(stack, newOp.value);
                    this.src.setUnit(newOp.insn, as);
                    newOp.updateBoxes();
                } else {
                    Unit prev = this.src.getUnit(newOp.insn);
                    boolean merge = true;
                    if (prev instanceof UnitContainer) {
                        Unit[] unitArr = ((UnitContainer) prev).units;
                        int length = unitArr.length;
                        int i2 = 0;
                        while (true) {
                            if (i2 >= length) {
                                break;
                            }
                            Unit t = unitArr[i2];
                            if (!AsmUtil.alreadyExists(t, stack, newOp.stackOrValue())) {
                                i2++;
                            } else {
                                merge = false;
                                break;
                            }
                        }
                    } else if (AsmUtil.alreadyExists(prev, stack, newOp.stackOrValue())) {
                        merge = false;
                    }
                    if (merge) {
                        AssignStmt as2 = Jimple.v().newAssignStmt(stack, newOp.stackOrValue());
                        this.src.mergeUnits(newOp.insn, as2);
                        newOp.addBox(as2.getRightOpBox());
                    }
                }
            } else {
                for (int j = 0; j != nrIn; j++) {
                    stack = in.get(j)[i].stack;
                    if (stack != null) {
                        break;
                    }
                }
                if (stack == null) {
                    stack = newOp.stack;
                    if (stack == null) {
                        stack = this.src.newStackLocal();
                    }
                }
                ValueBox box = this.boxes == null ? null : this.boxes[i];
                for (int j2 = 0; j2 != nrIn; j2++) {
                    Operand prevOp = in.get(j2)[i];
                    if (prevOp.stack != stack) {
                        prevOp.removeBox(box);
                        if (prevOp.stack == null) {
                            prevOp.stack = stack;
                            AssignStmt as3 = Jimple.v().newAssignStmt(stack, prevOp.value);
                            this.src.setUnit(prevOp.insn, as3);
                        } else {
                            Unit u = this.src.getUnit(prevOp.insn);
                            DefinitionStmt as4 = (DefinitionStmt) (u instanceof UnitContainer ? ((UnitContainer) u).getFirstUnit() : u);
                            ValueBox lvb = as4.getLeftOpBox();
                            if (!$assertionsDisabled && lvb.getValue() != prevOp.stack) {
                                throw new AssertionError("Invalid stack local!");
                            }
                            lvb.setValue(stack);
                            prevOp.stack = stack;
                        }
                        prevOp.updateBoxes();
                    }
                }
                if (newOp.stack != stack) {
                    if (newOp.stack == null) {
                        newOp.stack = stack;
                        AssignStmt as5 = Jimple.v().newAssignStmt(stack, newOp.value);
                        this.src.setUnit(newOp.insn, as5);
                    } else {
                        Unit u2 = this.src.getUnit(newOp.insn);
                        DefinitionStmt as6 = (DefinitionStmt) (u2 instanceof UnitContainer ? ((UnitContainer) u2).getFirstUnit() : u2);
                        ValueBox lvb2 = as6.getLeftOpBox();
                        if (!$assertionsDisabled && lvb2.getValue() != newOp.stack) {
                            throw new AssertionError("Invalid stack local!");
                        }
                        lvb2.setValue(stack);
                        newOp.stack = stack;
                    }
                    newOp.updateBoxes();
                }
                if (box != null) {
                    box.setValue(stack);
                }
                this.inStackLocals[i] = stack;
            }
        }
        if (diff) {
            in.add(oprs);
        }
    }
}
