package soot.dava;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.G;
import soot.IntType;
import soot.Local;
import soot.PatchingChain;
import soot.PhaseOptions;
import soot.RefType;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPatchingChain;
import soot.Value;
import soot.ValueBox;
import soot.coffi.Instruction;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.SET.SETTopNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.dava.internal.javaRep.DCmpExpr;
import soot.dava.internal.javaRep.DCmpgExpr;
import soot.dava.internal.javaRep.DCmplExpr;
import soot.dava.internal.javaRep.DInstanceFieldRef;
import soot.dava.internal.javaRep.DIntConstant;
import soot.dava.internal.javaRep.DInterfaceInvokeExpr;
import soot.dava.internal.javaRep.DLengthExpr;
import soot.dava.internal.javaRep.DNegExpr;
import soot.dava.internal.javaRep.DNewArrayExpr;
import soot.dava.internal.javaRep.DNewInvokeExpr;
import soot.dava.internal.javaRep.DNewMultiArrayExpr;
import soot.dava.internal.javaRep.DSpecialInvokeExpr;
import soot.dava.internal.javaRep.DStaticFieldRef;
import soot.dava.internal.javaRep.DStaticInvokeExpr;
import soot.dava.internal.javaRep.DThisRef;
import soot.dava.internal.javaRep.DVirtualInvokeExpr;
import soot.dava.toolkits.base.AST.UselessTryRemover;
import soot.dava.toolkits.base.AST.transformations.LocalVariableCleaner;
import soot.dava.toolkits.base.AST.transformations.ShortcutIfGenerator;
import soot.dava.toolkits.base.AST.transformations.SuperFirstStmtHandler;
import soot.dava.toolkits.base.AST.transformations.TypeCastingError;
import soot.dava.toolkits.base.AST.traversals.CopyPropagation;
import soot.dava.toolkits.base.finders.AbruptEdgeFinder;
import soot.dava.toolkits.base.finders.CycleFinder;
import soot.dava.toolkits.base.finders.ExceptionFinder;
import soot.dava.toolkits.base.finders.ExceptionNode;
import soot.dava.toolkits.base.finders.IfFinder;
import soot.dava.toolkits.base.finders.LabeledBlockFinder;
import soot.dava.toolkits.base.finders.SequenceFinder;
import soot.dava.toolkits.base.finders.SwitchFinder;
import soot.dava.toolkits.base.finders.SynchronizedBlockFinder;
import soot.dava.toolkits.base.misc.MonitorConverter;
import soot.dava.toolkits.base.misc.ThrowNullConverter;
import soot.grimp.GrimpBody;
import soot.grimp.NewInvokeExpr;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.Expr;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LengthExpr;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.MonitorStmt;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.ReturnStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.UnopExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.internal.JGotoStmt;
import soot.jimple.internal.JimpleLocal;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.TrapUnitGraph;
import soot.util.IterableSet;
import soot.util.Switchable;
/* loaded from: gencallgraphv3.jar:soot/dava/DavaBody.class */
public class DavaBody extends Body {
    public boolean DEBUG;
    private Map<Integer, Value> pMap;
    private Set<Object> consumedConditions;
    private HashSet<Object> thisLocals;
    private IterableSet<ExceptionNode> synchronizedBlockFacts;
    private IterableSet<ExceptionNode> exceptionFacts;
    private IterableSet<AugmentedStmt> monitorFacts;
    private IterableSet<String> importList;
    private Local controlLocal;
    private InstanceInvokeExpr constructorExpr;
    private Unit constructorUnit;
    private List<CaughtExceptionRef> caughtrefs;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DavaBody(SootMethod m) {
        super(m);
        this.DEBUG = false;
        this.pMap = new HashMap();
        this.consumedConditions = new HashSet();
        this.thisLocals = new HashSet<>();
        this.synchronizedBlockFacts = new IterableSet<>();
        this.exceptionFacts = new IterableSet<>();
        this.monitorFacts = new IterableSet<>();
        this.importList = new IterableSet<>();
        this.caughtrefs = new LinkedList();
        this.controlLocal = null;
        this.constructorExpr = null;
    }

    public Unit get_ConstructorUnit() {
        return this.constructorUnit;
    }

    public List<CaughtExceptionRef> get_CaughtRefs() {
        return this.caughtrefs;
    }

    public InstanceInvokeExpr get_ConstructorExpr() {
        return this.constructorExpr;
    }

    public void set_ConstructorExpr(InstanceInvokeExpr expr) {
        this.constructorExpr = expr;
    }

    public void set_ConstructorUnit(Unit s) {
        this.constructorUnit = s;
    }

    public Map<Integer, Value> get_ParamMap() {
        return this.pMap;
    }

    public void set_ParamMap(Map<Integer, Value> map) {
        this.pMap = map;
    }

    public HashSet<Object> get_ThisLocals() {
        return this.thisLocals;
    }

    public Local get_ControlLocal() {
        if (this.controlLocal == null) {
            this.controlLocal = new JimpleLocal("controlLocal", IntType.v());
            getLocals().add(this.controlLocal);
        }
        return this.controlLocal;
    }

    public Set<Object> get_ConsumedConditions() {
        return this.consumedConditions;
    }

    public void consume_Condition(AugmentedStmt as) {
        this.consumedConditions.add(as);
    }

    @Override // soot.Body
    public Object clone() {
        Body b = Dava.v().newBody(getMethodUnsafe());
        b.importBodyContentsFrom(this);
        return b;
    }

    @Override // soot.Body
    public Object clone(boolean noLocalsClone) {
        return null;
    }

    public IterableSet<ExceptionNode> get_SynchronizedBlockFacts() {
        return this.synchronizedBlockFacts;
    }

    public IterableSet<ExceptionNode> get_ExceptionFacts() {
        return this.exceptionFacts;
    }

    public IterableSet<AugmentedStmt> get_MonitorFacts() {
        return this.monitorFacts;
    }

    public IterableSet<String> getImportList() {
        return this.importList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DavaBody(Body body) {
        this(body.getMethod());
        debug("DavaBody", "creating DavaBody for" + body.getMethod().toString());
        Dava.v().log("\nstart method " + body.getMethod().toString());
        if (this.DEBUG && !body.getMethod().getExceptions().isEmpty()) {
            debug("DavaBody", "printing NON EMPTY exception list for " + body.getMethod().toString() + Instruction.argsep + body.getMethod().getExceptions().toString());
        }
        copy_Body(body);
        AugmentedStmtGraph asg = new AugmentedStmtGraph(new BriefUnitGraph(this), new TrapUnitGraph(this));
        ExceptionFinder.v().preprocess(this, asg);
        SETNode SET = new SETTopNode(asg.get_ChainView());
        while (true) {
            try {
                CycleFinder.v().find(this, asg, SET);
                IfFinder.v().find(this, asg, SET);
                SwitchFinder.v().find(this, asg, SET);
                SynchronizedBlockFinder.v().find(this, asg, SET);
                ExceptionFinder.v().find(this, asg, SET);
                SequenceFinder.v().find(this, asg, SET);
                LabeledBlockFinder.v().find(this, asg, SET);
                AbruptEdgeFinder.v().find(this, asg, SET);
                break;
            } catch (RetriggerAnalysisException e) {
                SET = new SETTopNode(asg.get_ChainView());
                this.consumedConditions = new HashSet();
            }
        }
        MonitorConverter.v().convert(this);
        ThrowNullConverter.v().convert(this);
        ASTNode AST = SET.emit_AST();
        getTraps().clear();
        getUnits().clear();
        getUnits().addLast((UnitPatchingChain) AST);
        do {
            G.v().ASTAnalysis_modified = false;
            AST.perform_Analysis(UselessTryRemover.v());
        } while (G.v().ASTAnalysis_modified);
        if (AST instanceof ASTMethodNode) {
            ((ASTMethodNode) AST).storeLocals(this);
            Map<String, String> options = PhaseOptions.v().getPhaseOptions("db.force-recompile");
            boolean force = PhaseOptions.getBoolean(options, "enabled");
            if (force) {
                AST.apply(new SuperFirstStmtHandler((ASTMethodNode) AST));
            }
            debug("DavaBody", "PreInit booleans is" + G.v().SootMethodAddedByDava);
        }
        Dava.v().log("end method " + body.getMethod().toString());
    }

    public void applyBugFixes() {
        ASTNode AST = (ASTNode) getUnits().getFirst();
        debug("applyBugFixes", "Applying AST analyzes for method" + getMethod().toString());
        AST.apply(new ShortcutIfGenerator());
        debug("applyBugFixes", "after ShortcutIfGenerator" + G.v().ASTTransformations_modified);
        AST.apply(new TypeCastingError());
        debug("applyBugFixes", "after TypeCastingError" + G.v().ASTTransformations_modified);
    }

    public void analyzeAST() {
        ASTNode AST = (ASTNode) getUnits().getFirst();
        debug("analyzeAST", "Applying AST analyzes for method" + getMethod().toString());
        applyASTAnalyses(AST);
        debug("analyzeAST", "Applying structure analysis" + getMethod().toString());
        applyStructuralAnalyses(AST);
        debug("analyzeAST", "Applying structure analysis DONE" + getMethod().toString());
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x03a5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void applyASTAnalyses(soot.dava.internal.AST.ASTNode r7) {
        /*
            Method dump skipped, instructions count: 1042
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.dava.DavaBody.applyASTAnalyses(soot.dava.internal.AST.ASTNode):void");
    }

    private void applyStructuralAnalyses(ASTNode AST) {
        debug("applyStructureAnalyses", "invoking copy propagation");
        CopyPropagation prop = new CopyPropagation(AST);
        AST.apply(prop);
        debug("applyStructureAnalyses", "invoking copy propagation DONE");
        debug("applyStructureAnalyses", "Local Variable Cleaner started");
        AST.apply(new LocalVariableCleaner(AST));
        debug("applyStructureAnalyses", "Local Variable Cleaner DONE");
    }

    private void copy_Body(Body body) {
        if (!(body instanceof GrimpBody)) {
            throw new RuntimeException("You can only create a DavaBody from a GrimpBody!");
        }
        GrimpBody grimpBody = (GrimpBody) body;
        HashMap<Switchable, Switchable> bindings = new HashMap<>();
        HashMap<Unit, Unit> reverse_binding = new HashMap<>();
        Iterator<Unit> it = grimpBody.getUnits().iterator();
        while (it.hasNext()) {
            Unit original = it.next();
            Unit copy = (Unit) original.clone();
            getUnits().addLast((UnitPatchingChain) copy);
            bindings.put(original, copy);
            reverse_binding.put(copy, original);
        }
        Iterator<Unit> it2 = getUnits().iterator();
        while (it2.hasNext()) {
            Unit u = it2.next();
            Stmt s = (Stmt) u;
            if (s instanceof TableSwitchStmt) {
                TableSwitchStmt ts = (TableSwitchStmt) s;
                TableSwitchStmt original_switch = (TableSwitchStmt) reverse_binding.get(u);
                ts.setDefaultTarget((Unit) bindings.get(original_switch.getDefaultTarget()));
                LinkedList<Unit> new_target_list = new LinkedList<>();
                int target_count = (ts.getHighIndex() - ts.getLowIndex()) + 1;
                for (int i = 0; i < target_count; i++) {
                    new_target_list.add((Unit) bindings.get(original_switch.getTarget(i)));
                }
                ts.setTargets(new_target_list);
            } else if (s instanceof LookupSwitchStmt) {
                LookupSwitchStmt ls = (LookupSwitchStmt) s;
                LookupSwitchStmt original_switch2 = (LookupSwitchStmt) reverse_binding.get(u);
                ls.setDefaultTarget((Unit) bindings.get(original_switch2.getDefaultTarget()));
                Unit[] new_target_list2 = new Unit[original_switch2.getTargetCount()];
                for (int i2 = 0; i2 < original_switch2.getTargetCount(); i2++) {
                    new_target_list2[i2] = (Unit) bindings.get(original_switch2.getTarget(i2));
                }
                ls.setTargets(new_target_list2);
                ls.setLookupValues(original_switch2.getLookupValues());
            }
        }
        for (Local original2 : grimpBody.getLocals()) {
            Local copy2 = Dava.v().newLocal(original2.getName(), original2.getType());
            getLocals().add(copy2);
            bindings.put(original2, copy2);
        }
        for (UnitBox box : getAllUnitBoxes()) {
            Unit newObject = (Unit) bindings.get(box.getUnit());
            if (newObject != null) {
                box.setUnit(newObject);
            }
        }
        for (ValueBox vb : getUseAndDefBoxes()) {
            Value val = vb.getValue();
            if (val instanceof Local) {
                vb.setValue((Value) bindings.get(val));
            }
        }
        for (Trap originalTrap : grimpBody.getTraps()) {
            Trap cloneTrap = (Trap) originalTrap.clone();
            cloneTrap.setHandlerUnit((Unit) bindings.get(originalTrap.getHandlerUnit()));
            cloneTrap.setBeginUnit((Unit) bindings.get(originalTrap.getBeginUnit()));
            cloneTrap.setEndUnit((Unit) bindings.get(originalTrap.getEndUnit()));
            getTraps().add(cloneTrap);
        }
        PatchingChain<Unit> units = getUnits();
        Iterator<Unit> it3 = units.snapshotIterator();
        while (it3.hasNext()) {
            Unit u2 = it3.next();
            if (u2 instanceof IfStmt) {
                IfStmt ifs = (IfStmt) u2;
                JGotoStmt jgs = new JGotoStmt(units.getSuccOf((PatchingChain<Unit>) u2));
                units.insertAfter(jgs, (JGotoStmt) u2);
                JGotoStmt jumper = new JGotoStmt(ifs.getTarget());
                units.insertAfter(jumper, jgs);
                ifs.setTarget(jumper);
            } else if (u2 instanceof TableSwitchStmt) {
                TableSwitchStmt tss = (TableSwitchStmt) u2;
                int targetCount = (tss.getHighIndex() - tss.getLowIndex()) + 1;
                for (int i3 = 0; i3 < targetCount; i3++) {
                    JGotoStmt jgs2 = new JGotoStmt(tss.getTarget(i3));
                    units.insertAfter(jgs2, (JGotoStmt) tss);
                    tss.setTarget(i3, jgs2);
                }
                JGotoStmt jgs3 = new JGotoStmt(tss.getDefaultTarget());
                units.insertAfter(jgs3, (JGotoStmt) tss);
                tss.setDefaultTarget(jgs3);
            } else if (u2 instanceof LookupSwitchStmt) {
                LookupSwitchStmt lss = (LookupSwitchStmt) u2;
                for (int i4 = 0; i4 < lss.getTargetCount(); i4++) {
                    JGotoStmt jgs4 = new JGotoStmt(lss.getTarget(i4));
                    units.insertAfter(jgs4, (JGotoStmt) lss);
                    lss.setTarget(i4, jgs4);
                }
                JGotoStmt jgs5 = new JGotoStmt(lss.getDefaultTarget());
                units.insertAfter(jgs5, (JGotoStmt) lss);
                lss.setDefaultTarget(jgs5);
            }
        }
        for (Trap t : getTraps()) {
            JGotoStmt jgs6 = new JGotoStmt(t.getHandlerUnit());
            units.addLast((PatchingChain<Unit>) jgs6);
            t.setHandlerUnit(jgs6);
        }
        for (Local l : getLocals()) {
            Type t2 = l.getType();
            if (t2 instanceof RefType) {
                RefType rt = (RefType) t2;
                String className = rt.getSootClass().toString();
                String packageName = rt.getSootClass().getJavaPackageName();
                String classPackageName = packageName;
                if (className.lastIndexOf(46) > 0) {
                    classPackageName = className.substring(0, className.lastIndexOf(46));
                }
                if (!packageName.equals(classPackageName)) {
                    throw new DecompilationException("Unable to retrieve package name for identifier. Please report to developer.");
                }
                addToImportList(className);
            }
        }
        Iterator<Unit> it4 = getUnits().iterator();
        while (it4.hasNext()) {
            Unit u3 = it4.next();
            if (u3 instanceof IfStmt) {
                javafy(((IfStmt) u3).getConditionBox());
            } else if (u3 instanceof ThrowStmt) {
                javafy(((ThrowStmt) u3).getOpBox());
            } else if (u3 instanceof TableSwitchStmt) {
                javafy(((TableSwitchStmt) u3).getKeyBox());
            } else if (u3 instanceof LookupSwitchStmt) {
                javafy(((LookupSwitchStmt) u3).getKeyBox());
            } else if (u3 instanceof MonitorStmt) {
                javafy(((MonitorStmt) u3).getOpBox());
            } else if (u3 instanceof DefinitionStmt) {
                DefinitionStmt ds = (DefinitionStmt) u3;
                javafy(ds.getRightOpBox());
                javafy(ds.getLeftOpBox());
                Value rightOp = ds.getRightOp();
                if (rightOp instanceof IntConstant) {
                    ds.getRightOpBox().setValue(DIntConstant.v(((IntConstant) rightOp).value, ds.getLeftOp().getType()));
                }
            } else if (u3 instanceof ReturnStmt) {
                ReturnStmt rs = (ReturnStmt) u3;
                Value op = rs.getOp();
                if (op instanceof IntConstant) {
                    rs.getOpBox().setValue(DIntConstant.v(((IntConstant) op).value, body.getMethod().getReturnType()));
                } else {
                    javafy(rs.getOpBox());
                }
            } else if (u3 instanceof InvokeStmt) {
                javafy(((InvokeStmt) u3).getInvokeExprBox());
            }
        }
        Iterator<Unit> it5 = getUnits().iterator();
        while (it5.hasNext()) {
            Unit u4 = it5.next();
            if (u4 instanceof IdentityStmt) {
                IdentityStmt ids = (IdentityStmt) u4;
                Value ids_rightOp = ids.getRightOp();
                Value ids_leftOp = ids.getLeftOp();
                if ((ids_leftOp instanceof Local) && (ids_rightOp instanceof ThisRef)) {
                    Local thisLocal = (Local) ids_leftOp;
                    this.thisLocals.add(thisLocal);
                    thisLocal.setName("this");
                }
            }
            if (u4 instanceof DefinitionStmt) {
                DefinitionStmt ds2 = (DefinitionStmt) u4;
                Value rightOp2 = ds2.getRightOp();
                if (rightOp2 instanceof ParameterRef) {
                    this.pMap.put(Integer.valueOf(((ParameterRef) rightOp2).getIndex()), ds2.getLeftOp());
                }
                if (rightOp2 instanceof CaughtExceptionRef) {
                    this.caughtrefs.add((CaughtExceptionRef) rightOp2);
                }
            }
        }
        Iterator<Unit> it6 = getUnits().iterator();
        while (it6.hasNext()) {
            Unit u5 = it6.next();
            if (u5 instanceof InvokeStmt) {
                InvokeStmt ivs = (InvokeStmt) u5;
                Value ie = ivs.getInvokeExpr();
                if (ie instanceof InstanceInvokeExpr) {
                    InstanceInvokeExpr iie = (InstanceInvokeExpr) ie;
                    Value base = iie.getBase();
                    if ((base instanceof Local) && "this".equals(((Local) base).getName())) {
                        String name = iie.getMethodRef().name();
                        if ("<init>".equals(name) || "<clinit>".equals(name)) {
                            if (this.constructorUnit != null) {
                                throw new RuntimeException("More than one candidate for constructor found.");
                            }
                            this.constructorExpr = iie;
                            this.constructorUnit = u5;
                        }
                    }
                } else {
                    continue;
                }
            }
        }
    }

    private void javafy(ValueBox vb) {
        Value v = vb.getValue();
        if (v instanceof Expr) {
            javafy_expr(vb);
        } else if (v instanceof Ref) {
            javafy_ref(vb);
        } else if (v instanceof Local) {
            javafy_local(vb);
        } else if (v instanceof Constant) {
            javafy_constant(vb);
        }
    }

    private void javafy_expr(ValueBox vb) {
        Expr e = (Expr) vb.getValue();
        if (e instanceof BinopExpr) {
            javafy_binop_expr(vb);
        } else if (e instanceof UnopExpr) {
            javafy_unop_expr(vb);
        } else if (e instanceof CastExpr) {
            javafy_cast_expr(vb);
        } else if (e instanceof NewArrayExpr) {
            javafy_newarray_expr(vb);
        } else if (e instanceof NewMultiArrayExpr) {
            javafy_newmultiarray_expr(vb);
        } else if (e instanceof InstanceOfExpr) {
            javafy_instanceof_expr(vb);
        } else if (e instanceof InvokeExpr) {
            javafy_invoke_expr(vb);
        } else if (e instanceof NewExpr) {
            javafy_new_expr(vb);
        }
    }

    private void javafy_ref(ValueBox vb) {
        Ref r = (Ref) vb.getValue();
        if (r instanceof StaticFieldRef) {
            SootFieldRef fieldRef = ((StaticFieldRef) r).getFieldRef();
            String className = fieldRef.declaringClass().toString();
            String packageName = fieldRef.declaringClass().getJavaPackageName();
            String classPackageName = packageName;
            if (className.lastIndexOf(46) > 0) {
                classPackageName = className.substring(0, className.lastIndexOf(46));
            }
            if (!packageName.equals(classPackageName)) {
                throw new DecompilationException("Unable to retrieve package name for identifier. Please report to developer.");
            }
            addToImportList(className);
            vb.setValue(new DStaticFieldRef(fieldRef, getMethod().getDeclaringClass().getName()));
        } else if (r instanceof ArrayRef) {
            ArrayRef ar = (ArrayRef) r;
            javafy(ar.getBaseBox());
            javafy(ar.getIndexBox());
        } else if (r instanceof InstanceFieldRef) {
            InstanceFieldRef ifr = (InstanceFieldRef) r;
            javafy(ifr.getBaseBox());
            vb.setValue(new DInstanceFieldRef(ifr.getBase(), ifr.getFieldRef(), this.thisLocals));
        } else if (r instanceof ThisRef) {
            ThisRef tr = (ThisRef) r;
            vb.setValue(new DThisRef((RefType) tr.getType()));
        }
    }

    private void javafy_local(ValueBox vb) {
    }

    private void javafy_constant(ValueBox vb) {
    }

    private void javafy_binop_expr(ValueBox vb) {
        BinopExpr boe = (BinopExpr) vb.getValue();
        ValueBox leftOpBox = boe.getOp1Box();
        ValueBox rightOpBox = boe.getOp2Box();
        Value leftOp = leftOpBox.getValue();
        Value rightOp = rightOpBox.getValue();
        if (rightOp instanceof IntConstant) {
            if (!(leftOp instanceof IntConstant)) {
                javafy(leftOpBox);
                leftOp = leftOpBox.getValue();
                if (boe instanceof ConditionExpr) {
                    rightOpBox.setValue(DIntConstant.v(((IntConstant) rightOp).value, leftOp.getType()));
                } else {
                    rightOpBox.setValue(DIntConstant.v(((IntConstant) rightOp).value, null));
                }
            }
        } else if (leftOp instanceof IntConstant) {
            javafy(rightOpBox);
            rightOp = rightOpBox.getValue();
            if (boe instanceof ConditionExpr) {
                leftOpBox.setValue(DIntConstant.v(((IntConstant) leftOp).value, rightOp.getType()));
            } else {
                leftOpBox.setValue(DIntConstant.v(((IntConstant) leftOp).value, null));
            }
        } else {
            javafy(rightOpBox);
            rightOp = rightOpBox.getValue();
            javafy(leftOpBox);
            leftOp = leftOpBox.getValue();
        }
        if (boe instanceof CmpExpr) {
            vb.setValue(new DCmpExpr(leftOp, rightOp));
        } else if (boe instanceof CmplExpr) {
            vb.setValue(new DCmplExpr(leftOp, rightOp));
        } else if (boe instanceof CmpgExpr) {
            vb.setValue(new DCmpgExpr(leftOp, rightOp));
        }
    }

    private void javafy_unop_expr(ValueBox vb) {
        UnopExpr uoe = (UnopExpr) vb.getValue();
        javafy(uoe.getOpBox());
        if (uoe instanceof LengthExpr) {
            vb.setValue(new DLengthExpr(((LengthExpr) uoe).getOp()));
        } else if (uoe instanceof NegExpr) {
            vb.setValue(new DNegExpr(((NegExpr) uoe).getOp()));
        }
    }

    private void javafy_cast_expr(ValueBox vb) {
        CastExpr ce = (CastExpr) vb.getValue();
        javafy(ce.getOpBox());
    }

    private void javafy_newarray_expr(ValueBox vb) {
        NewArrayExpr nae = (NewArrayExpr) vb.getValue();
        javafy(nae.getSizeBox());
        vb.setValue(new DNewArrayExpr(nae.getBaseType(), nae.getSize()));
    }

    private void javafy_newmultiarray_expr(ValueBox vb) {
        NewMultiArrayExpr nmae = (NewMultiArrayExpr) vb.getValue();
        for (int i = 0; i < nmae.getSizeCount(); i++) {
            javafy(nmae.getSizeBox(i));
        }
        vb.setValue(new DNewMultiArrayExpr(nmae.getBaseType(), nmae.getSizes()));
    }

    private void javafy_instanceof_expr(ValueBox vb) {
        InstanceOfExpr ioe = (InstanceOfExpr) vb.getValue();
        javafy(ioe.getOpBox());
    }

    private void javafy_invoke_expr(ValueBox vb) {
        InvokeExpr ie = (InvokeExpr) vb.getValue();
        String className = ie.getMethodRef().declaringClass().toString();
        String packageName = ie.getMethodRef().declaringClass().getJavaPackageName();
        String classPackageName = packageName;
        if (className.lastIndexOf(46) > 0) {
            classPackageName = className.substring(0, className.lastIndexOf(46));
        }
        if (!packageName.equals(classPackageName)) {
            throw new DecompilationException("Unable to retrieve package name for identifier. Please report to developer.");
        }
        addToImportList(className);
        for (int i = 0; i < ie.getArgCount(); i++) {
            Value arg = ie.getArg(i);
            if (arg instanceof IntConstant) {
                ie.getArgBox(i).setValue(DIntConstant.v(((IntConstant) arg).value, ie.getMethodRef().parameterType(i)));
            } else {
                javafy(ie.getArgBox(i));
            }
        }
        if (ie instanceof InstanceInvokeExpr) {
            javafy(((InstanceInvokeExpr) ie).getBaseBox());
            if (ie instanceof VirtualInvokeExpr) {
                VirtualInvokeExpr vie = (VirtualInvokeExpr) ie;
                vb.setValue(new DVirtualInvokeExpr(vie.getBase(), vie.getMethodRef(), vie.getArgs(), this.thisLocals));
            } else if (ie instanceof SpecialInvokeExpr) {
                SpecialInvokeExpr sie = (SpecialInvokeExpr) ie;
                vb.setValue(new DSpecialInvokeExpr(sie.getBase(), sie.getMethodRef(), sie.getArgs()));
            } else if (ie instanceof InterfaceInvokeExpr) {
                InterfaceInvokeExpr iie = (InterfaceInvokeExpr) ie;
                vb.setValue(new DInterfaceInvokeExpr(iie.getBase(), iie.getMethodRef(), iie.getArgs()));
            } else {
                throw new RuntimeException("InstanceInvokeExpr " + ie + " not javafied correctly");
            }
        } else if (ie instanceof StaticInvokeExpr) {
            StaticInvokeExpr sie2 = (StaticInvokeExpr) ie;
            if (sie2 instanceof NewInvokeExpr) {
                NewInvokeExpr nie = (NewInvokeExpr) sie2;
                RefType rt = nie.getBaseType();
                String className2 = rt.getSootClass().toString();
                String packageName2 = rt.getSootClass().getJavaPackageName();
                String classPackageName2 = packageName2;
                if (className2.lastIndexOf(46) > 0) {
                    classPackageName2 = className2.substring(0, className2.lastIndexOf(46));
                }
                if (!packageName2.equals(classPackageName2)) {
                    throw new DecompilationException("Unable to retrieve package name for identifier. Please report to developer.");
                }
                addToImportList(className2);
                vb.setValue(new DNewInvokeExpr((RefType) nie.getType(), nie.getMethodRef(), nie.getArgs()));
                return;
            }
            SootMethodRef methodRef = sie2.getMethodRef();
            String className3 = methodRef.declaringClass().toString();
            String packageName3 = methodRef.declaringClass().getJavaPackageName();
            String classPackageName3 = packageName3;
            if (className3.lastIndexOf(46) > 0) {
                classPackageName3 = className3.substring(0, className3.lastIndexOf(46));
            }
            if (!packageName3.equals(classPackageName3)) {
                throw new DecompilationException("Unable to retrieve package name for identifier. Please report to developer.");
            }
            addToImportList(className3);
            vb.setValue(new DStaticInvokeExpr(methodRef, sie2.getArgs()));
        } else {
            throw new RuntimeException("InvokeExpr " + ie + " not javafied correctly");
        }
    }

    private void javafy_new_expr(ValueBox vb) {
        NewExpr ne = (NewExpr) vb.getValue();
        String className = ne.getBaseType().getSootClass().toString();
        String packageName = ne.getBaseType().getSootClass().getJavaPackageName();
        String classPackageName = packageName;
        if (className.lastIndexOf(46) > 0) {
            classPackageName = className.substring(0, className.lastIndexOf(46));
        }
        if (!packageName.equals(classPackageName)) {
            throw new DecompilationException("Unable to retrieve package name for identifier. Please report to developer.");
        }
        addToImportList(className);
    }

    public void addToImportList(String className) {
        if (!className.isEmpty()) {
            this.importList.add(className);
        }
    }

    public void debug(String methodName, String debug) {
        if (this.DEBUG) {
            System.out.println(String.valueOf(methodName) + "    DEBUG: " + debug);
        }
    }
}
