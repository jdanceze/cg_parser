package soot.jimple.toolkits.annotation.arraycheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.IntType;
import soot.Local;
import soot.SootField;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AddExpr;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.LengthExpr;
import soot.jimple.MulExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
import soot.jimple.internal.JAddExpr;
import soot.jimple.internal.JSubExpr;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/arraycheck/ArrayIndexLivenessAnalysis.class */
class ArrayIndexLivenessAnalysis extends BackwardFlowAnalysis {
    private static final Logger logger = LoggerFactory.getLogger(ArrayIndexLivenessAnalysis.class);
    HashSet<Local> fullSet;
    ExceptionalUnitGraph eug;
    HashMap<Stmt, HashSet<Object>> genOfUnit;
    HashMap<Stmt, HashSet<Value>> absGenOfUnit;
    HashMap<Stmt, HashSet<Value>> killOfUnit;
    HashMap<Stmt, HashSet<Value>> conditionOfGen;
    HashMap<DefinitionStmt, Value> killArrayRelated;
    HashMap<DefinitionStmt, Boolean> killAllArrayRef;
    IntContainer zero;
    private final boolean fieldin;
    HashMap<Object, HashSet<Value>> localToFieldRef;
    HashMap<Object, HashSet<Value>> fieldToFieldRef;
    HashSet<Value> allFieldRefs;
    private final boolean arrayin;
    HashMap localToArrayRef;
    HashSet allArrayRefs;
    private final boolean csin;
    HashMap<Value, HashSet<Value>> localToExpr;
    private final boolean rectarray;
    HashSet<Local> multiarraylocals;

    public ArrayIndexLivenessAnalysis(DirectedGraph dg, boolean takeFieldRef, boolean takeArrayRef, boolean takeCSE, boolean takeRectArray) {
        super(dg);
        this.fullSet = new HashSet<>();
        this.zero = new IntContainer(0);
        this.fieldin = takeFieldRef;
        this.arrayin = takeArrayRef;
        this.csin = takeCSE;
        this.rectarray = takeRectArray;
        if (Options.v().debug()) {
            logger.debug("Enter ArrayIndexLivenessAnalysis");
        }
        this.eug = (ExceptionalUnitGraph) dg;
        retrieveAllArrayLocals(this.eug.getBody(), this.fullSet);
        this.genOfUnit = new HashMap<>((this.eug.size() * 2) + 1);
        this.absGenOfUnit = new HashMap<>((this.eug.size() * 2) + 1);
        this.killOfUnit = new HashMap<>((this.eug.size() * 2) + 1);
        this.conditionOfGen = new HashMap<>((this.eug.size() * 2) + 1);
        if (this.fieldin) {
            this.localToFieldRef = new HashMap<>();
            this.fieldToFieldRef = new HashMap<>();
            this.allFieldRefs = new HashSet<>();
        }
        if (this.arrayin) {
            this.localToArrayRef = new HashMap();
            this.allArrayRefs = new HashSet();
            this.killArrayRelated = new HashMap<>();
            this.killAllArrayRef = new HashMap<>();
            if (this.rectarray) {
                this.multiarraylocals = new HashSet<>();
                retrieveMultiArrayLocals(this.eug.getBody(), this.multiarraylocals);
            }
        }
        if (this.csin) {
            this.localToExpr = new HashMap<>();
        }
        getAllRelatedMaps(this.eug.getBody());
        getGenAndKillSet(this.eug.getBody(), this.absGenOfUnit, this.genOfUnit, this.killOfUnit, this.conditionOfGen);
        doAnalysis();
        if (Options.v().debug()) {
            logger.debug("Leave ArrayIndexLivenessAnalysis");
        }
    }

    public HashMap<Object, HashSet<Value>> getLocalToFieldRef() {
        return this.localToFieldRef;
    }

    public HashMap<Object, HashSet<Value>> getFieldToFieldRef() {
        return this.fieldToFieldRef;
    }

    public HashSet<Value> getAllFieldRefs() {
        return this.allFieldRefs;
    }

    public HashMap getLocalToArrayRef() {
        return this.localToArrayRef;
    }

    public HashSet getAllArrayRefs() {
        return this.allArrayRefs;
    }

    public HashMap<Value, HashSet<Value>> getLocalToExpr() {
        return this.localToExpr;
    }

    public HashSet<Local> getMultiArrayLocals() {
        return this.multiarraylocals;
    }

    private void getAllRelatedMaps(Body body) {
        Iterator unitIt = body.getUnits().iterator();
        while (unitIt.hasNext()) {
            Stmt stmt = (Stmt) unitIt.next();
            if (this.csin && (stmt instanceof DefinitionStmt)) {
                Value rhs = ((DefinitionStmt) stmt).getRightOp();
                if (rhs instanceof BinopExpr) {
                    Value op1 = ((BinopExpr) rhs).getOp1();
                    Value op2 = ((BinopExpr) rhs).getOp2();
                    if (rhs instanceof AddExpr) {
                        if ((op1 instanceof Local) && (op2 instanceof Local)) {
                            HashSet<Value> refs = this.localToExpr.get(op1);
                            if (refs == null) {
                                refs = new HashSet<>();
                                this.localToExpr.put(op1, refs);
                            }
                            refs.add(rhs);
                            HashSet<Value> refs2 = this.localToExpr.get(op2);
                            if (refs2 == null) {
                                refs2 = new HashSet<>();
                                this.localToExpr.put(op2, refs2);
                            }
                            refs2.add(rhs);
                        }
                    } else if (rhs instanceof MulExpr) {
                        HashSet<Value> refs3 = this.localToExpr.get(op1);
                        if (refs3 == null) {
                            refs3 = new HashSet<>();
                            this.localToExpr.put(op1, refs3);
                        }
                        refs3.add(rhs);
                        HashSet<Value> refs4 = this.localToExpr.get(op2);
                        if (refs4 == null) {
                            refs4 = new HashSet<>();
                            this.localToExpr.put(op2, refs4);
                        }
                        refs4.add(rhs);
                    } else if ((rhs instanceof SubExpr) && (op2 instanceof Local)) {
                        HashSet<Value> refs5 = this.localToExpr.get(op2);
                        if (refs5 == null) {
                            refs5 = new HashSet<>();
                            this.localToExpr.put(op2, refs5);
                        }
                        refs5.add(rhs);
                        if (op1 instanceof Local) {
                            HashSet<Value> refs6 = this.localToExpr.get(op1);
                            if (refs6 == null) {
                                refs6 = new HashSet<>();
                                this.localToExpr.put(op1, refs6);
                            }
                            refs6.add(rhs);
                        }
                    }
                }
            }
            for (ValueBox vbox : stmt.getUseAndDefBoxes()) {
                Value v = vbox.getValue();
                if (this.fieldin) {
                    if (v instanceof InstanceFieldRef) {
                        Value base = ((InstanceFieldRef) v).getBase();
                        SootField field = ((InstanceFieldRef) v).getField();
                        HashSet<Value> baseset = this.localToFieldRef.get(base);
                        if (baseset == null) {
                            baseset = new HashSet<>();
                            this.localToFieldRef.put(base, baseset);
                        }
                        baseset.add(v);
                        HashSet<Value> fieldset = this.fieldToFieldRef.get(field);
                        if (fieldset == null) {
                            fieldset = new HashSet<>();
                            this.fieldToFieldRef.put(field, fieldset);
                        }
                        fieldset.add(v);
                    }
                    if (v instanceof FieldRef) {
                        this.allFieldRefs.add(v);
                    }
                }
            }
        }
    }

    private void retrieveAllArrayLocals(Body body, Set<Local> container) {
        for (Local local : body.getLocals()) {
            Type type = local.getType();
            if ((type instanceof IntType) || (type instanceof ArrayType)) {
                container.add(local);
            }
        }
    }

    private void retrieveMultiArrayLocals(Body body, Set<Local> container) {
        for (Local local : body.getLocals()) {
            Type type = local.getType();
            if ((type instanceof ArrayType) && ((ArrayType) type).numDimensions > 1) {
                this.multiarraylocals.add(local);
            }
        }
    }

    private void getGenAndKillSetForDefnStmt(DefinitionStmt asstmt, HashMap<Stmt, HashSet<Value>> absgen, HashSet<Object> genset, HashSet<Value> absgenset, HashSet<Value> killset, HashSet<Value> condset) {
        Value lhs = asstmt.getLeftOp();
        Value rhs = asstmt.getRightOp();
        boolean killarrayrelated = false;
        boolean killallarrayref = false;
        if (this.fieldin) {
            if (lhs instanceof Local) {
                HashSet<Value> related = this.localToFieldRef.get(lhs);
                if (related != null) {
                    killset.addAll(related);
                }
            } else if (lhs instanceof StaticFieldRef) {
                killset.add(lhs);
                condset.add(lhs);
            } else if (lhs instanceof InstanceFieldRef) {
                SootField field = ((InstanceFieldRef) lhs).getField();
                HashSet<Value> related2 = this.fieldToFieldRef.get(field);
                if (related2 != null) {
                    killset.addAll(related2);
                }
                condset.add(lhs);
            }
            if (asstmt.containsInvokeExpr()) {
                killset.addAll(this.allFieldRefs);
            }
        }
        if (this.arrayin) {
            if (lhs instanceof Local) {
                killarrayrelated = true;
            } else if (lhs instanceof ArrayRef) {
                killallarrayref = true;
                condset.add(lhs);
            }
            if (asstmt.containsInvokeExpr()) {
                killallarrayref = true;
            }
        }
        if (this.csin) {
            HashSet<Value> exprs = this.localToExpr.get(lhs);
            if (exprs != null) {
                killset.addAll(exprs);
            }
            if (rhs instanceof BinopExpr) {
                Value op1 = ((BinopExpr) rhs).getOp1();
                Value op2 = ((BinopExpr) rhs).getOp2();
                if (rhs instanceof AddExpr) {
                    if ((op1 instanceof Local) && (op2 instanceof Local)) {
                        genset.add(rhs);
                    }
                } else if (rhs instanceof MulExpr) {
                    if ((op1 instanceof Local) || (op2 instanceof Local)) {
                        genset.add(rhs);
                    }
                } else if ((rhs instanceof SubExpr) && (op2 instanceof Local)) {
                    genset.add(rhs);
                }
            }
        }
        if ((lhs instanceof Local) && this.fullSet.contains(lhs)) {
            killset.add(lhs);
            condset.add(lhs);
        } else if (lhs instanceof ArrayRef) {
            Value base = ((ArrayRef) lhs).getBase();
            Value index = ((ArrayRef) lhs).getIndex();
            absgenset.add(base);
            if (index instanceof Local) {
                absgenset.add(index);
            }
        }
        if (rhs instanceof Local) {
            if (this.fullSet.contains(rhs)) {
                genset.add(rhs);
            }
        } else if (rhs instanceof FieldRef) {
            if (this.fieldin) {
                genset.add(rhs);
            }
        } else if (rhs instanceof ArrayRef) {
            Value base2 = ((ArrayRef) rhs).getBase();
            Value index2 = ((ArrayRef) rhs).getIndex();
            absgenset.add(base2);
            if (index2 instanceof Local) {
                absgenset.add(index2);
            }
            if (this.arrayin) {
                genset.add(rhs);
                if (this.rectarray) {
                    genset.add(Array2ndDimensionSymbol.v(base2));
                }
            }
        } else if (rhs instanceof NewArrayExpr) {
            Value size = ((NewArrayExpr) rhs).getSize();
            if (size instanceof Local) {
                genset.add(size);
            }
        } else if (rhs instanceof NewMultiArrayExpr) {
            List sizes = ((NewMultiArrayExpr) rhs).getSizes();
            for (Value size2 : sizes) {
                if (size2 instanceof Local) {
                    genset.add(size2);
                }
            }
        } else if (rhs instanceof LengthExpr) {
            Value op = ((LengthExpr) rhs).getOp();
            genset.add(op);
        } else if (rhs instanceof JAddExpr) {
            Value op12 = ((JAddExpr) rhs).getOp1();
            Value op22 = ((JAddExpr) rhs).getOp2();
            if ((op12 instanceof IntConstant) && (op22 instanceof Local)) {
                genset.add(op22);
            } else if ((op22 instanceof IntConstant) && (op12 instanceof Local)) {
                genset.add(op12);
            }
        } else if (rhs instanceof JSubExpr) {
            Value op13 = ((JSubExpr) rhs).getOp1();
            Value op23 = ((JSubExpr) rhs).getOp2();
            if ((op13 instanceof Local) && (op23 instanceof IntConstant)) {
                genset.add(op13);
            }
        }
        if (this.arrayin) {
            if (killarrayrelated) {
                this.killArrayRelated.put(asstmt, lhs);
            }
            if (killallarrayref) {
                this.killAllArrayRef.put(asstmt, new Boolean(true));
            }
        }
    }

    private void getGenAndKillSet(Body body, HashMap<Stmt, HashSet<Value>> absgen, HashMap<Stmt, HashSet<Object>> gen, HashMap<Stmt, HashSet<Value>> kill, HashMap<Stmt, HashSet<Value>> condition) {
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt stmt = (Stmt) u;
            HashSet<Object> genset = new HashSet<>();
            HashSet<Value> absgenset = new HashSet<>();
            HashSet<Value> killset = new HashSet<>();
            HashSet<Value> condset = new HashSet<>();
            if (stmt instanceof DefinitionStmt) {
                getGenAndKillSetForDefnStmt((DefinitionStmt) stmt, absgen, genset, absgenset, killset, condset);
            } else if (stmt instanceof IfStmt) {
                Value cmpcond = ((IfStmt) stmt).getCondition();
                if (cmpcond instanceof ConditionExpr) {
                    Value op1 = ((ConditionExpr) cmpcond).getOp1();
                    Value op2 = ((ConditionExpr) cmpcond).getOp2();
                    if (this.fullSet.contains(op1) && this.fullSet.contains(op2)) {
                        condset.add(op1);
                        condset.add(op2);
                        genset.add(op1);
                        genset.add(op2);
                    }
                }
            }
            if (genset.size() != 0) {
                gen.put(stmt, genset);
            }
            if (absgenset.size() != 0) {
                absgen.put(stmt, absgenset);
            }
            if (killset.size() != 0) {
                kill.put(stmt, killset);
            }
            if (condset.size() != 0) {
                condition.put(stmt, condset);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public Object newInitialFlow() {
        return new HashSet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public Object entryInitialFlow() {
        return new HashSet();
    }

    @Override // soot.toolkits.scalar.FlowAnalysis
    protected void flowThrough(Object inValue, Object unit, Object outValue) {
        HashSet inset = (HashSet) inValue;
        HashSet outset = (HashSet) outValue;
        Stmt stmt = (Stmt) unit;
        outset.clear();
        outset.addAll(inset);
        HashSet<Object> genset = this.genOfUnit.get(unit);
        HashSet<Value> absgenset = this.absGenOfUnit.get(unit);
        HashSet<Value> killset = this.killOfUnit.get(unit);
        HashSet<Value> condset = this.conditionOfGen.get(unit);
        if (killset != null) {
            outset.removeAll(killset);
        }
        if (this.arrayin) {
            Boolean killall = this.killAllArrayRef.get(stmt);
            if (killall != null && killall.booleanValue()) {
                List keylist = new ArrayList(outset);
                for (Object key : keylist) {
                    if (key instanceof ArrayRef) {
                        outset.remove(key);
                    }
                }
            } else {
                Object local = this.killArrayRelated.get(stmt);
                if (local != null) {
                    List keylist2 = new ArrayList(outset);
                    for (Object key2 : keylist2) {
                        if (key2 instanceof ArrayRef) {
                            Value base = ((ArrayRef) key2).getBase();
                            Value index = ((ArrayRef) key2).getIndex();
                            if (base.equals(local) || index.equals(local)) {
                                outset.remove(key2);
                            }
                        }
                        if (this.rectarray && (key2 instanceof Array2ndDimensionSymbol)) {
                            Object base2 = ((Array2ndDimensionSymbol) key2).getVar();
                            if (base2.equals(local)) {
                                outset.remove(key2);
                            }
                        }
                    }
                }
            }
        }
        if (genset != null) {
            if (condset == null || condset.size() == 0) {
                outset.addAll(genset);
            } else {
                Iterator condIt = condset.iterator();
                while (true) {
                    if (condIt.hasNext()) {
                        if (inset.contains(condIt.next())) {
                            outset.addAll(genset);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        if (absgenset != null) {
            outset.addAll(absgenset);
        }
    }

    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    protected void merge(Object in1, Object in2, Object out) {
        HashSet inset1 = (HashSet) in1;
        HashSet inset2 = (HashSet) in2;
        HashSet outset = (HashSet) out;
        HashSet src = inset1;
        if (outset == inset1) {
            src = inset2;
        } else if (outset == inset2) {
            src = inset1;
        } else {
            outset.clear();
            outset.addAll(inset2);
        }
        outset.addAll(src);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.toolkits.scalar.AbstractFlowAnalysis
    public void copy(Object source, Object dest) {
        if (source == dest) {
            return;
        }
        HashSet sourceSet = (HashSet) source;
        HashSet destSet = (HashSet) dest;
        destSet.clear();
        destSet.addAll(sourceSet);
    }
}
