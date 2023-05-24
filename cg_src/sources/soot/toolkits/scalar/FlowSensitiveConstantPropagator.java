package soot.toolkits.scalar;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Scene;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.dexpler.DexNullArrayRefTransformer;
import soot.dexpler.DexNullThrowTransformer;
import soot.jimple.AssignStmt;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.RealConstant;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.ImmediateBox;
import soot.jimple.toolkits.scalar.CopyPropagator;
import soot.options.Options;
import soot.tagkit.Tag;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.util.LocalBitSetPacker;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FlowSensitiveConstantPropagator.class */
public class FlowSensitiveConstantPropagator extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(FlowSensitiveConstantPropagator.class);
    protected ThrowAnalysis throwAnalysis;
    protected boolean omitExceptingUnitEdges;

    public FlowSensitiveConstantPropagator(Singletons.Global g) {
    }

    public FlowSensitiveConstantPropagator(ThrowAnalysis ta) {
        this(ta, false);
    }

    public FlowSensitiveConstantPropagator(ThrowAnalysis ta, boolean omitExceptingUnitEdges) {
        this.throwAnalysis = ta;
        this.omitExceptingUnitEdges = omitExceptingUnitEdges;
    }

    public static FlowSensitiveConstantPropagator v() {
        return G.v().soot_toolkits_scalar_FlowSensitiveConstantPropagator();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "] Splitting for shared initialization of locals...");
        }
        if (this.throwAnalysis == null) {
            this.throwAnalysis = Scene.v().getDefaultThrowAnalysis();
        }
        if (!this.omitExceptingUnitEdges) {
            this.omitExceptingUnitEdges = Options.v().omit_excepting_unit_edges();
        }
        LocalBitSetPacker localPacker = new LocalBitSetPacker(body);
        localPacker.pack();
        ExceptionalUnitGraph graph = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body, this.throwAnalysis, this.omitExceptingUnitEdges);
        BetterConstantPropagator bcp = createBetterConstantPropagator(graph);
        bcp.doAnalysis();
        boolean propagatedThrow = false;
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            ConstantState v = bcp.getFlowBefore(u);
            boolean expectsRealValue = false;
            if (u instanceof AssignStmt) {
                AssignStmt assign = (AssignStmt) u;
                Value rop = assign.getRightOp();
                if (rop instanceof Local) {
                    Local l = (Local) rop;
                    Constant c = v.getConstant(l);
                    if (c != null) {
                        List<Tag> oldTags = assign.getRightOpBox().getTags();
                        assign.setRightOp(c);
                        assign.getRightOpBox().getTags().addAll(oldTags);
                        CopyPropagator.copyLineTags(assign.getUseBoxes().get(0), (DefinitionStmt) assign);
                    }
                }
                expectsRealValue = expectsRealValue(rop);
            }
            if (u instanceof IfStmt) {
                expectsRealValue = expectsRealValue(((IfStmt) u).getCondition());
            }
            for (ValueBox r : u.getUseBoxes()) {
                if (r instanceof ImmediateBox) {
                    Value src = r.getValue();
                    if (src instanceof Local) {
                        Constant val = v.getConstant((Local) src);
                        if (val != null) {
                            if (u instanceof ThrowStmt) {
                                propagatedThrow = true;
                            }
                            if (expectsRealValue && !(val instanceof RealConstant)) {
                                if (val instanceof IntConstant) {
                                    val = FloatConstant.v(((IntConstant) val).value);
                                } else if (val instanceof LongConstant) {
                                    val = DoubleConstant.v(((LongConstant) val).value);
                                }
                            }
                            r.setValue(val);
                        }
                    }
                }
            }
        }
        localPacker.unpack();
        if (propagatedThrow) {
            DexNullThrowTransformer.v().transform(body);
        }
        DexNullArrayRefTransformer.v().transform(body);
    }

    protected BetterConstantPropagator createBetterConstantPropagator(ExceptionalUnitGraph graph) {
        return new BetterConstantPropagator(graph);
    }

    private static boolean expectsRealValue(Value op) {
        return (op instanceof CmpgExpr) || (op instanceof CmplExpr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FlowSensitiveConstantPropagator$ConstantState.class */
    public static class ConstantState {
        public BitSet nonConstant = new BitSet();
        public Map<Local, Constant> constants = createMap();

        protected ConstantState() {
        }

        protected Map<Local, Constant> createMap() {
            return new HashMap();
        }

        public Constant getConstant(Local l) {
            Constant r = this.constants.get(l);
            return r;
        }

        public void setNonConstant(Local l) {
            this.nonConstant.set(l.getNumber());
            this.constants.remove(l);
        }

        public void setConstant(Local l, Constant value) {
            if (value == null) {
                throw new IllegalArgumentException("Not valid");
            }
            this.constants.put(l, value);
            this.nonConstant.clear(l.getNumber());
        }

        public void copyTo(ConstantState dest) {
            dest.nonConstant = (BitSet) this.nonConstant.clone();
            dest.constants = new HashMap(this.constants);
        }

        private void checkConsistency() {
            for (Local i : this.constants.keySet()) {
                if (this.nonConstant.get(i.getNumber())) {
                    throw new IllegalStateException("A local seems to be constant and not at the same time: " + i + " (" + i.getNumber() + ")");
                }
            }
        }

        public String toString() {
            return "Non-constants: " + this.nonConstant + "\nConstants: " + this.constants;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.constants == null ? 0 : this.constants.hashCode());
            return (31 * result) + (this.nonConstant == null ? 0 : this.nonConstant.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ConstantState other = (ConstantState) obj;
            if (this.nonConstant == null) {
                if (other.nonConstant != null) {
                    return false;
                }
            } else if (!this.nonConstant.equals(other.nonConstant)) {
                return false;
            }
            if (this.constants == null) {
                if (other.constants != null) {
                    return false;
                }
                return true;
            } else if (!this.constants.equals(other.constants)) {
                return false;
            } else {
                return true;
            }
        }

        public void merge(ConstantState in1, ConstantState in2) {
            this.nonConstant.or(in1.nonConstant);
            this.nonConstant.or(in2.nonConstant);
            if (!in1.constants.isEmpty()) {
                mergeInternal(in1, in2);
            }
            if (!in2.constants.isEmpty()) {
                mergeInternal(in2, in1);
            }
        }

        private void mergeInternal(ConstantState in1, ConstantState in2) {
            for (Map.Entry<Local, Constant> r : in1.constants.entrySet()) {
                Local l = r.getKey();
                if (in2.nonConstant.get(l.getNumber())) {
                    setNonConstant(l);
                } else {
                    Constant rr = in2.getConstant(l);
                    if (rr == null) {
                        setConstant(l, r.getValue());
                    } else if (rr.equals(r.getValue())) {
                        setConstant(l, rr);
                    } else {
                        setNonConstant(l);
                    }
                }
            }
        }

        public void clear() {
            this.nonConstant.clear();
            this.constants = createMap();
        }

        public void mergeInto(ConstantState in) {
            this.nonConstant.or(in.nonConstant);
            if (!in.constants.isEmpty()) {
                mergeInternal(in, this);
            }
            Iterator<Local> it = this.constants.keySet().iterator();
            while (it.hasNext()) {
                if (in.nonConstant.get(it.next().getNumber())) {
                    it.remove();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/FlowSensitiveConstantPropagator$BetterConstantPropagator.class */
    public class BetterConstantPropagator extends ForwardFlowAnalysis<Unit, ConstantState> {
        public BetterConstantPropagator(DirectedGraph<Unit> graph) {
            super(graph);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.FlowAnalysis
        public boolean omissible(Unit n) {
            if (!(n instanceof DefinitionStmt)) {
                return true;
            }
            return super.omissible((BetterConstantPropagator) n);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.FlowAnalysis
        public void flowThrough(ConstantState in, Unit d, ConstantState out) {
            in.copyTo(out);
            if (d instanceof Stmt) {
                Stmt s = (Stmt) d;
                if (!(s instanceof AssignStmt)) {
                    if (s instanceof IdentityStmt) {
                        out.setNonConstant((Local) ((IdentityStmt) s).getLeftOp());
                        return;
                    }
                    return;
                }
                AssignStmt assign = (AssignStmt) s;
                if (assign.getLeftOp() instanceof Local) {
                    Local l = (Local) assign.getLeftOp();
                    Object rop = assign.getRightOp();
                    Constant value = null;
                    if (rop instanceof Constant) {
                        value = (Constant) rop;
                    } else if (rop instanceof Local) {
                        value = in.getConstant((Local) rop);
                    }
                    if (value == null) {
                        out.setNonConstant(l);
                    } else {
                        out.setConstant(l, value);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public ConstantState newInitialFlow() {
            return new ConstantState();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void mergeInto(Unit succNode, ConstantState inout, ConstantState in) {
            inout.mergeInto(in);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void merge(ConstantState in1, ConstantState in2, ConstantState out) {
            out.merge(in1, in2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.AbstractFlowAnalysis
        public void copy(ConstantState source, ConstantState dest) {
            if (source == dest) {
                return;
            }
            source.copyTo(dest);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // soot.toolkits.scalar.FlowAnalysis
        public void copyFreshToExisting(ConstantState in, ConstantState dest) {
            dest.constants = in.constants;
            dest.nonConstant = in.nonConstant;
        }
    }
}
