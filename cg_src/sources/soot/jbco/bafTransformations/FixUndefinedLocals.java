package soot.jbco.bafTransformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.RefLikeType;
import soot.StmtAddressType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.baf.DoubleWordType;
import soot.baf.IdentityInst;
import soot.baf.IncInst;
import soot.baf.NopInst;
import soot.baf.OpTypeArgInst;
import soot.baf.PushInst;
import soot.baf.WordType;
import soot.baf.internal.AbstractOpTypeInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Rand;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.scalar.GuaranteedDefs;
/* loaded from: gencallgraphv3.jar:soot/jbco/bafTransformations/FixUndefinedLocals.class */
public class FixUndefinedLocals extends BodyTransformer implements IJbcoTransform {
    private int undefined = 0;
    public static String[] dependancies = {"bb.jbco_j2bl", "bb.jbco_ful", "bb.lp"};
    public static String name = "bb.jbco_ful";

    @Override // soot.jbco.IJbcoTransform
    public String[] getDependencies() {
        return dependancies;
    }

    @Override // soot.jbco.IJbcoTransform
    public String getName() {
        return name;
    }

    @Override // soot.jbco.IJbcoTransform
    public void outputSummary() {
        out.println("Undefined Locals fixed with pre-initializers: " + this.undefined);
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Type t;
        int icount = 0;
        boolean passedIDs = false;
        Map<Local, Local> bafToJLocals = Main.methods2Baf2JLocals.get(b.getMethod());
        ArrayList<Value> initialized = new ArrayList<>();
        PatchingChain<Unit> units = b.getUnits();
        GuaranteedDefs gd = new GuaranteedDefs(ExceptionalUnitGraphFactory.createExceptionalUnitGraph(b));
        Iterator<Unit> unitIt = units.snapshotIterator();
        Unit after = null;
        while (unitIt.hasNext()) {
            Unit u = unitIt.next();
            if (!passedIDs && (u instanceof IdentityInst)) {
                Value v = ((IdentityInst) u).getLeftOp();
                if (v instanceof Local) {
                    initialized.add(v);
                    icount++;
                }
                after = u;
            } else {
                passedIDs = true;
                if (after == null) {
                    after = Baf.v().newNopInst();
                    units.addFirst((PatchingChain<Unit>) after);
                }
                List<?> defs = gd.getGuaranteedDefs(u);
                for (ValueBox valueBox : u.getUseBoxes()) {
                    Value v2 = valueBox.getValue();
                    if ((v2 instanceof Local) && !defs.contains(v2) && !initialized.contains(v2)) {
                        Local l = (Local) v2;
                        Local jl = bafToJLocals.get(l);
                        if (jl != null) {
                            t = jl.getType();
                        } else {
                            t = l.getType();
                            if (u instanceof OpTypeArgInst) {
                                OpTypeArgInst ota = (OpTypeArgInst) u;
                                t = ota.getOpType();
                            } else if (u instanceof AbstractOpTypeInst) {
                                AbstractOpTypeInst ota2 = (AbstractOpTypeInst) u;
                                t = ota2.getOpType();
                            } else if (u instanceof IncInst) {
                                t = IntType.v();
                            }
                            if ((t instanceof DoubleWordType) || (t instanceof WordType)) {
                                throw new RuntimeException("Shouldn't get here (t is a double or word type: in FixUndefinedLocals)");
                            }
                        }
                        Unit store = Baf.v().newStoreInst(t, l);
                        units.insertAfter(store, after);
                        if (t instanceof ArrayType) {
                            Unit tmp = Baf.v().newInstanceCastInst(t);
                            units.insertBefore(tmp, store);
                            store = tmp;
                        }
                        Unit pinit = getPushInitializer(l, t);
                        units.insertBefore(pinit, store);
                        initialized.add(l);
                    }
                }
                continue;
            }
        }
        if (after instanceof NopInst) {
            units.remove(after);
        }
        this.undefined += initialized.size() - icount;
    }

    public static PushInst getPushInitializer(Local l, Type t) {
        if (t instanceof IntegerType) {
            return Baf.v().newPushInst(IntConstant.v(Rand.getInt()));
        }
        if ((t instanceof RefLikeType) || (t instanceof StmtAddressType)) {
            return Baf.v().newPushInst(NullConstant.v());
        }
        if (t instanceof LongType) {
            return Baf.v().newPushInst(LongConstant.v(Rand.getLong()));
        }
        if (t instanceof FloatType) {
            return Baf.v().newPushInst(FloatConstant.v(Rand.getFloat()));
        }
        if (t instanceof DoubleType) {
            return Baf.v().newPushInst(DoubleConstant.v(Rand.getDouble()));
        }
        return null;
    }
}
