package soot.shimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import soot.Local;
import soot.Type;
import soot.UnitBoxOwner;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Constant;
import soot.jimple.Expr;
import soot.jimple.toolkits.scalar.Evaluator;
import soot.shimple.PhiExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/scalar/SEvaluator.class */
public class SEvaluator {

    /* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/scalar/SEvaluator$MetaConstant.class */
    public static abstract class MetaConstant extends Constant {
    }

    public static boolean isValueConstantValued(Value op) {
        if (op instanceof PhiExpr) {
            Constant firstConstant = null;
            for (Value arg : ((PhiExpr) op).getValues()) {
                if (!(arg instanceof Constant)) {
                    return false;
                }
                if (firstConstant == null) {
                    firstConstant = (Constant) arg;
                } else if (!firstConstant.equals(arg)) {
                    return false;
                }
            }
            return true;
        }
        return Evaluator.isValueConstantValued(op);
    }

    public static Value getConstantValueOf(Value op) {
        if (op instanceof PhiExpr) {
            if (isValueConstantValued(op)) {
                return ((PhiExpr) op).getValue(0);
            }
            return null;
        }
        return Evaluator.getConstantValueOf(op);
    }

    public static Constant getFuzzyConstantValueOf(Value v) {
        if (v instanceof Constant) {
            return (Constant) v;
        }
        if (v instanceof Local) {
            return BottomConstant.v();
        }
        if (!(v instanceof Expr)) {
            return BottomConstant.v();
        }
        Constant constant = null;
        if (v instanceof PhiExpr) {
            PhiExpr phi = (PhiExpr) v;
            Iterator<Value> it = phi.getValues().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Value arg = it.next();
                if ((arg instanceof Constant) && !(arg instanceof TopConstant)) {
                    if (constant == null) {
                        constant = (Constant) arg;
                    } else if (!constant.equals(arg)) {
                        constant = BottomConstant.v();
                        break;
                    }
                }
            }
            if (constant == null) {
                constant = TopConstant.v();
            }
        } else {
            Iterator<ValueBox> it2 = v.getUseBoxes().iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                ValueBox name = it2.next();
                Value value = name.getValue();
                if (value instanceof BottomConstant) {
                    constant = BottomConstant.v();
                    break;
                } else if (value instanceof TopConstant) {
                    constant = TopConstant.v();
                }
            }
            if (constant == null) {
                constant = (Constant) getConstantValueOf(v);
            }
            if (constant == null) {
                constant = BottomConstant.v();
            }
        }
        return constant;
    }

    public static Constant getFuzzyConstantValueOf(Value v, Map<Local, Constant> localToConstant) {
        if (v instanceof Constant) {
            return (Constant) v;
        }
        if (v instanceof Local) {
            return localToConstant.get((Local) v);
        }
        if (!(v instanceof Expr)) {
            return BottomConstant.v();
        }
        Expr expr = (Expr) v.clone();
        for (ValueBox useBox : expr.getUseBoxes()) {
            Value use = useBox.getValue();
            if (use instanceof Local) {
                Constant constant = localToConstant.get((Local) use);
                if (useBox.canContainValue(constant)) {
                    useBox.setValue(constant);
                }
            }
        }
        if (expr instanceof UnitBoxOwner) {
            ((UnitBoxOwner) expr).clearUnitBoxes();
        }
        return getFuzzyConstantValueOf(expr);
    }

    /* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/scalar/SEvaluator$TopConstant.class */
    public static class TopConstant extends MetaConstant {
        private static final TopConstant constant = new TopConstant();

        private TopConstant() {
        }

        public static Constant v() {
            return constant;
        }

        @Override // soot.Value
        public Type getType() {
            return UnknownType.v();
        }

        @Override // soot.util.Switchable
        public void apply(Switch sw) {
            throw new RuntimeException("Not implemented.");
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/shimple/toolkits/scalar/SEvaluator$BottomConstant.class */
    public static class BottomConstant extends MetaConstant {
        private static final BottomConstant constant = new BottomConstant();

        private BottomConstant() {
        }

        public static Constant v() {
            return constant;
        }

        @Override // soot.Value
        public Type getType() {
            return UnknownType.v();
        }

        @Override // soot.util.Switchable
        public void apply(Switch sw) {
            throw new RuntimeException("Not implemented.");
        }
    }
}
