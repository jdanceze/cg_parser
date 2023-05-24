package soot.shimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.shimple.PhiExpr;
import soot.shimple.Shimple;
import soot.shimple.ShimpleExprSwitch;
import soot.toolkits.graph.Block;
import soot.toolkits.scalar.ValueUnitPair;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/shimple/internal/SPhiExpr.class */
public class SPhiExpr implements PhiExpr {
    private static final Logger logger = LoggerFactory.getLogger(SPhiExpr.class);
    protected Type type;
    protected List<ValueUnitPair> argPairs = new ArrayList();
    protected Map<Unit, ValueUnitPair> predToPair = new HashMap();
    protected int blockId = -1;

    public SPhiExpr(Local leftLocal, List<Block> preds) {
        this.type = leftLocal.getType();
        for (Block pred : preds) {
            addArg(leftLocal, pred);
        }
    }

    public SPhiExpr(List<Value> args, List<Unit> preds) {
        if (args.isEmpty()) {
            throw new RuntimeException("Arg list may not be empty");
        }
        if (args.size() != preds.size()) {
            throw new RuntimeException("Arg list does not match Pred list");
        }
        this.type = args.get(0).getType();
        Iterator<Unit> predsIt = preds.iterator();
        for (Value arg : args) {
            Unit pred = predsIt.next();
            if (pred instanceof Block) {
                addArg(arg, (Block) pred);
            } else {
                addArg(arg, pred);
            }
        }
    }

    @Override // soot.shimple.PhiExpr
    public List<ValueUnitPair> getArgs() {
        return Collections.unmodifiableList(this.argPairs);
    }

    @Override // soot.shimple.PhiExpr
    public List<Value> getValues() {
        List<Value> args = new ArrayList<>();
        for (ValueUnitPair vup : this.argPairs) {
            Value arg = vup.getValue();
            args.add(arg);
        }
        return args;
    }

    @Override // soot.shimple.PhiExpr
    public List<Unit> getPreds() {
        List<Unit> preds = new ArrayList<>();
        for (ValueUnitPair up : this.argPairs) {
            Unit arg = up.getUnit();
            preds.add(arg);
        }
        return preds;
    }

    @Override // soot.shimple.PhiExpr
    public int getArgCount() {
        return this.argPairs.size();
    }

    @Override // soot.shimple.PhiExpr
    public ValueUnitPair getArgBox(int index) {
        if (index < 0 || index >= this.argPairs.size()) {
            return null;
        }
        return this.argPairs.get(index);
    }

    @Override // soot.shimple.PhiExpr
    public Value getValue(int index) {
        ValueUnitPair arg = getArgBox(index);
        if (arg == null) {
            return null;
        }
        return arg.getValue();
    }

    @Override // soot.shimple.PhiExpr
    public Unit getPred(int index) {
        ValueUnitPair arg = getArgBox(index);
        if (arg == null) {
            return null;
        }
        return arg.getUnit();
    }

    @Override // soot.shimple.PhiExpr
    public int getArgIndex(Unit predTailUnit) {
        ValueUnitPair pair = getArgBox(predTailUnit);
        return this.argPairs.indexOf(pair);
    }

    @Override // soot.shimple.PhiExpr
    public ValueUnitPair getArgBox(Unit predTailUnit) {
        ValueUnitPair vup = this.predToPair.get(predTailUnit);
        if (vup == null || vup.getUnit() != predTailUnit) {
            updateCache();
            vup = this.predToPair.get(predTailUnit);
            if (vup != null && vup.getUnit() != predTailUnit) {
                throw new RuntimeException("Assertion failed.");
            }
        }
        return vup;
    }

    @Override // soot.shimple.PhiExpr
    public Value getValue(Unit predTailUnit) {
        ValueBox vb = getArgBox(predTailUnit);
        if (vb == null) {
            return null;
        }
        return vb.getValue();
    }

    @Override // soot.shimple.PhiExpr
    public int getArgIndex(Block pred) {
        ValueUnitPair box = getArgBox(pred);
        return this.argPairs.indexOf(box);
    }

    @Override // soot.shimple.PhiExpr
    public ValueUnitPair getArgBox(Block pred) {
        ValueUnitPair box;
        Unit predTailUnit = pred.getTail();
        ValueUnitPair argBox = getArgBox(predTailUnit);
        while (true) {
            box = argBox;
            if (box != null) {
                break;
            }
            predTailUnit = pred.getPredOf(predTailUnit);
            if (predTailUnit == null) {
                break;
            }
            argBox = getArgBox(predTailUnit);
        }
        return box;
    }

    @Override // soot.shimple.PhiExpr
    public Value getValue(Block pred) {
        ValueBox vb = getArgBox(pred);
        if (vb == null) {
            return null;
        }
        return vb.getValue();
    }

    @Override // soot.shimple.PhiExpr
    public boolean setArg(int index, Value arg, Unit predTailUnit) {
        boolean ret1 = setValue(index, arg);
        boolean ret2 = setPred(index, predTailUnit);
        if (ret1 != ret2) {
            throw new RuntimeException("Assertion failed.");
        }
        return ret1;
    }

    @Override // soot.shimple.PhiExpr
    public boolean setArg(int index, Value arg, Block pred) {
        return setArg(index, arg, pred.getTail());
    }

    @Override // soot.shimple.PhiExpr
    public boolean setValue(int index, Value arg) {
        ValueUnitPair argPair = getArgBox(index);
        if (argPair == null) {
            return false;
        }
        argPair.setValue(arg);
        return true;
    }

    @Override // soot.shimple.PhiExpr
    public boolean setValue(Unit predTailUnit, Value arg) {
        int index = getArgIndex(predTailUnit);
        return setValue(index, arg);
    }

    @Override // soot.shimple.PhiExpr
    public boolean setValue(Block pred, Value arg) {
        int index = getArgIndex(pred);
        return setValue(index, arg);
    }

    @Override // soot.shimple.PhiExpr
    public boolean setPred(int index, Unit predTailUnit) {
        ValueUnitPair argPair = getArgBox(index);
        if (argPair == null) {
            return false;
        }
        int other = getArgIndex(predTailUnit);
        if (other != -1) {
            logger.warn("An argument with control flow predecessor " + predTailUnit + " already exists in " + this + "!");
            logger.warn("setPred resulted in deletion of " + argPair + " from " + this + ".");
            removeArg(argPair);
            return false;
        }
        argPair.setUnit(predTailUnit);
        return true;
    }

    @Override // soot.shimple.PhiExpr
    public boolean setPred(int index, Block pred) {
        return setPred(index, pred.getTail());
    }

    @Override // soot.shimple.PhiExpr
    public boolean removeArg(int index) {
        ValueUnitPair arg = getArgBox(index);
        return removeArg(arg);
    }

    @Override // soot.shimple.PhiExpr
    public boolean removeArg(Unit predTailUnit) {
        ValueUnitPair arg = getArgBox(predTailUnit);
        return removeArg(arg);
    }

    @Override // soot.shimple.PhiExpr
    public boolean removeArg(Block pred) {
        ValueUnitPair arg = getArgBox(pred);
        return removeArg(arg);
    }

    @Override // soot.shimple.PhiExpr
    public boolean removeArg(ValueUnitPair arg) {
        if (this.argPairs.remove(arg)) {
            this.predToPair.remove(arg.getUnit());
            arg.getUnit().removeBoxPointingToThis(arg);
            return true;
        }
        return false;
    }

    @Override // soot.shimple.PhiExpr
    public boolean addArg(Value arg, Block pred) {
        return addArg(arg, pred.getTail());
    }

    @Override // soot.shimple.PhiExpr
    public boolean addArg(Value arg, Unit predTailUnit) {
        if (predTailUnit == null || this.predToPair.containsKey(predTailUnit)) {
            return false;
        }
        ValueUnitPair vup = new SValueUnitPair(arg, predTailUnit);
        this.argPairs.add(vup);
        this.predToPair.put(predTailUnit, vup);
        return true;
    }

    @Override // soot.shimple.PhiExpr
    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    @Override // soot.shimple.PhiExpr
    public int getBlockId() {
        if (this.blockId == -1) {
            throw new RuntimeException("Assertion failed:  Block Id unknown.");
        }
        return this.blockId;
    }

    protected void updateCache() {
        int needed = this.argPairs.size();
        this.predToPair = new HashMap(needed << 1, 1.0f);
        for (ValueUnitPair vup : this.argPairs) {
            this.predToPair.put(vup.getUnit(), vup);
        }
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof SPhiExpr) {
            SPhiExpr pe = (SPhiExpr) o;
            int argCount = getArgCount();
            if (argCount == pe.getArgCount()) {
                for (int i = 0; i < argCount; i++) {
                    if (!this.argPairs.get(i).equivTo(pe.argPairs.get(i))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        int hashcode = 1;
        for (ValueUnitPair p : this.argPairs) {
            hashcode = (hashcode * 17) + p.equivHashCode();
        }
        return hashcode;
    }

    @Override // soot.UnitBoxOwner
    public List<UnitBox> getUnitBoxes() {
        return Collections.unmodifiableList(new ArrayList(new HashSet(this.argPairs)));
    }

    @Override // soot.UnitBoxOwner
    public void clearUnitBoxes() {
        for (UnitBox box : this.argPairs) {
            box.setUnit(null);
        }
    }

    @Override // soot.Value
    public List<ValueBox> getUseBoxes() {
        Set<ValueBox> set = new HashSet<>();
        for (ValueUnitPair argPair : this.argPairs) {
            set.addAll(argPair.getValue().getUseBoxes());
            set.add(argPair);
        }
        return new ArrayList(set);
    }

    @Override // soot.Value
    public Type getType() {
        return this.type;
    }

    public String toString() {
        StringBuilder expr = new StringBuilder("Phi(");
        boolean isFirst = true;
        for (ValueUnitPair vuPair : this.argPairs) {
            if (!isFirst) {
                expr.append(", ");
            } else {
                isFirst = false;
            }
            expr.append(vuPair.getValue().toString());
        }
        expr.append(')');
        return expr.toString();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.literal(Shimple.PHI);
        up.literal("(");
        boolean isFirst = true;
        for (ValueUnitPair vuPair : this.argPairs) {
            if (!isFirst) {
                up.literal(", ");
            } else {
                isFirst = false;
            }
            vuPair.toString(up);
        }
        up.literal(")");
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ShimpleExprSwitch) sw).casePhiExpr(this);
    }

    @Override // soot.Value
    public Object clone() {
        return new SPhiExpr(getValues(), getPreds());
    }
}
