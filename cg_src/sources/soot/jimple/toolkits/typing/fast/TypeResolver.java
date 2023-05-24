package soot.jimple.toolkits.typing.fast;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.G;
import soot.IntegerType;
import soot.JavaBasicTypes;
import soot.Local;
import soot.LocalGenerator;
import soot.PatchingChain;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NegExpr;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.typing.Util;
import soot.toolkits.scalar.LocalDefs;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/TypeResolver.class */
public class TypeResolver {
    protected final JimpleBody jb;
    private final HashMap<Local, BitSet> depends;
    private final LocalGenerator localGenerator;
    final BooleanType booleanType = BooleanType.v();
    final ByteType byteType = ByteType.v();
    final ShortType shortType = ShortType.v();
    private final List<DefinitionStmt> assignments = new ArrayList();

    public TypeResolver(JimpleBody jb) {
        this.jb = jb;
        this.depends = new HashMap<>(jb.getLocalCount());
        this.localGenerator = Scene.v().createLocalGenerator(jb);
        initAssignments();
    }

    private void initAssignments() {
        Iterator<Unit> it = this.jb.getUnits().iterator();
        while (it.hasNext()) {
            Unit stmt = it.next();
            if (stmt instanceof DefinitionStmt) {
                initAssignment((DefinitionStmt) stmt);
            }
        }
    }

    private void initAssignment(DefinitionStmt ds) {
        Value lhs = ds.getLeftOp();
        if ((lhs instanceof Local) || (lhs instanceof ArrayRef)) {
            int assignmentIdx = this.assignments.size();
            this.assignments.add(ds);
            Value rhs = ds.getRightOp();
            if (rhs instanceof Local) {
                addDepend((Local) rhs, assignmentIdx);
            } else if (rhs instanceof BinopExpr) {
                BinopExpr be = (BinopExpr) rhs;
                Value lop = be.getOp1();
                Value rop = be.getOp2();
                if (lop instanceof Local) {
                    addDepend((Local) lop, assignmentIdx);
                }
                if (rop instanceof Local) {
                    addDepend((Local) rop, assignmentIdx);
                }
            } else if (rhs instanceof NegExpr) {
                Value op = ((NegExpr) rhs).getOp();
                if (op instanceof Local) {
                    addDepend((Local) op, assignmentIdx);
                }
            } else if (!(rhs instanceof CastExpr)) {
                if (rhs instanceof ArrayRef) {
                    addDepend((Local) ((ArrayRef) rhs).getBase(), assignmentIdx);
                }
            } else {
                Value op2 = ((CastExpr) rhs).getOp();
                if (op2 instanceof Local) {
                    addDepend((Local) op2, assignmentIdx);
                }
            }
        }
    }

    private void addDepend(Local v, int stmtIndex) {
        BitSet d = this.depends.get(v);
        if (d == null) {
            d = new BitSet();
            this.depends.put(v, d);
        }
        d.set(stmtIndex);
    }

    public void inferTypes() {
        ITypingStrategy typingStrategy = getTypingStrategy();
        AugEvalFunction ef = new AugEvalFunction(this.jb);
        BytecodeHierarchy bh = new BytecodeHierarchy();
        Collection<Typing> sigma = applyAssignmentConstraints(typingStrategy.createTyping(this.jb.getLocals()), ef, bh);
        if (sigma.isEmpty()) {
            return;
        }
        int[] castCount = new int[1];
        Typing tg = minCasts(sigma, bh, castCount);
        if (castCount[0] != 0) {
            split_new();
            tg = minCasts(applyAssignmentConstraints(typingStrategy.createTyping(this.jb.getLocals()), ef, bh), bh, castCount);
        }
        insertCasts(tg, bh, false);
        BottomType bottom = BottomType.v();
        for (Local v : this.jb.getLocals()) {
            Type t = tg.get(v);
            if (t instanceof IntegerType) {
                tg.set(v, bottom);
            }
            v.setType(t);
        }
        Typing tg2 = typePromotion(tg);
        if (tg2 == null) {
            soot.jimple.toolkits.typing.integer.TypeResolver.resolve(this.jb);
            return;
        }
        for (Local v2 : this.jb.getLocals()) {
            Type type = tg2.get(v2);
            v2.setType(type);
        }
    }

    protected ITypingStrategy getTypingStrategy() {
        return DefaultTypingStrategy.INSTANCE;
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/TypeResolver$CastInsertionUseVisitor.class */
    public class CastInsertionUseVisitor implements IUseVisitor {
        protected JimpleBody jb;
        protected Typing tg;
        protected IHierarchy h;
        private final boolean countOnly;
        private int count = 0;

        public CastInsertionUseVisitor(boolean countOnly, JimpleBody jb, Typing tg, IHierarchy h) {
            this.jb = jb;
            this.tg = tg;
            this.h = h;
            this.countOnly = countOnly;
        }

        @Override // soot.jimple.toolkits.typing.fast.IUseVisitor
        public Value visit(Value op, Type useType, Stmt stmt, boolean checkOnly) {
            Local vold;
            Type t = AugEvalFunction.eval_(this.tg, op, stmt, this.jb);
            if (useType == t) {
                return op;
            }
            boolean needCast = false;
            if ((useType instanceof PrimType) && (t instanceof PrimType) && t.isAllowedInFinalCode() && useType.isAllowedInFinalCode()) {
                needCast = true;
            }
            if (!needCast && this.h.ancestor(useType, t)) {
                return op;
            }
            this.count++;
            if (this.countOnly) {
                return op;
            }
            if (stmt.containsArrayRef() && stmt.getArrayRef().getBase() == op && (stmt instanceof DefinitionStmt)) {
                Value leftOp = ((DefinitionStmt) stmt).getLeftOp();
                if (leftOp instanceof Local) {
                    Type baseType = this.tg.get((Local) op);
                    if ((baseType instanceof RefType) && isObjectLikeType((RefType) baseType)) {
                        this.tg.set((Local) leftOp, ((ArrayType) useType).getElementType());
                    }
                }
            }
            if (!(op instanceof Local)) {
                vold = TypeResolver.this.localGenerator.generateLocal(t);
                this.tg.set(vold, t);
                this.jb.getUnits().insertBefore(Jimple.v().newAssignStmt(vold, op), (AssignStmt) Util.findFirstNonIdentityUnit(this.jb, stmt));
            } else {
                vold = (Local) op;
            }
            return createCast(useType, stmt, vold, false);
        }

        private boolean isObjectLikeType(RefType rt) {
            if (rt instanceof WeakObjectType) {
                return true;
            }
            String name = rt.getSootClass().getName();
            return JavaBasicTypes.JAVA_LANG_OBJECT.equals(name) || JavaBasicTypes.JAVA_IO_SERIALIZABLE.equals(name) || "java.lang.Cloneable".equals(name);
        }

        protected Local createCast(Type useType, Stmt stmt, Local old, boolean after) {
            Local vnew = TypeResolver.this.localGenerator.generateLocal(useType);
            this.tg.set(vnew, useType);
            Jimple jimple = Jimple.v();
            AssignStmt newStmt = jimple.newAssignStmt(vnew, jimple.newCastExpr(old, useType));
            Unit u = Util.findFirstNonIdentityUnit(this.jb, stmt);
            if (after) {
                this.jb.getUnits().insertAfter(newStmt, (AssignStmt) u);
            } else {
                this.jb.getUnits().insertBefore(newStmt, (AssignStmt) u);
            }
            return vnew;
        }

        public int getCount() {
            return this.count;
        }

        @Override // soot.jimple.toolkits.typing.fast.IUseVisitor
        public boolean finish() {
            return false;
        }
    }

    private Typing typePromotion(Typing tg) {
        boolean conversionsPending;
        do {
            AugEvalFunction ef = new AugEvalFunction(this.jb);
            AugHierarchy h = new AugHierarchy();
            UseChecker uc = new UseChecker(this.jb);
            TypePromotionUseVisitor uv = new TypePromotionUseVisitor(this.jb, tg);
            do {
                Collection<Typing> sigma = applyAssignmentConstraints(tg, ef, h);
                if (sigma.isEmpty()) {
                    return null;
                }
                tg = sigma.iterator().next();
                uv.typingChanged = false;
                uc.check(tg, uv);
                if (uv.fail) {
                    return null;
                }
            } while (uv.typingChanged);
            conversionsPending = false;
            for (Local v : this.jb.getLocals()) {
                Type t = tg.get(v);
                Type r = convert(t);
                if (r != null) {
                    tg.set(v, r);
                    conversionsPending = true;
                }
            }
        } while (conversionsPending);
        return tg;
    }

    protected Type convert(Type t) {
        if (t instanceof Integer1Type) {
            return this.booleanType;
        }
        if (t instanceof Integer127Type) {
            return this.byteType;
        }
        if (t instanceof Integer32767Type) {
            return this.shortType;
        }
        if (t instanceof WeakObjectType) {
            return RefType.v(((WeakObjectType) t).getClassName());
        }
        if (t instanceof ArrayType) {
            ArrayType r = (ArrayType) t;
            Type cv = convert(r.getElementType());
            if (cv != null) {
                return ArrayType.v(cv, r.numDimensions);
            }
            return null;
        }
        return null;
    }

    private int insertCasts(Typing tg, IHierarchy h, boolean countOnly) {
        UseChecker uc = new UseChecker(this.jb);
        CastInsertionUseVisitor uv = createCastInsertionUseVisitor(tg, h, countOnly);
        uc.check(tg, uv);
        return uv.getCount();
    }

    protected CastInsertionUseVisitor createCastInsertionUseVisitor(Typing tg, IHierarchy h, boolean countOnly) {
        return new CastInsertionUseVisitor(countOnly, this.jb, tg, h);
    }

    private Typing minCasts(Collection<Typing> sigma, IHierarchy h, int[] count) {
        count[0] = -1;
        Typing r = null;
        for (Typing tg : sigma) {
            int n = insertCasts(tg, h, true);
            if (count[0] == -1 || n < count[0]) {
                count[0] = n;
                r = tg;
            }
        }
        return r;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/fast/TypeResolver$WorklistElement.class */
    public static class WorklistElement {
        Typing typing;
        BitSet worklist;

        public WorklistElement(Typing tg, BitSet wl) {
            this.typing = tg;
            this.worklist = wl;
        }

        public String toString() {
            return "Left in worklist: " + this.worklist.size() + ", typing: " + this.typing;
        }
    }

    protected Collection<Typing> applyAssignmentConstraints(Typing tg, IEvalFunction ef, IHierarchy h) {
        Set<Type> lcas;
        Typing tg_;
        BitSet wl_;
        int numAssignments = this.assignments.size();
        if (numAssignments == 0) {
            return Collections.emptyList();
        }
        ArrayDeque<WorklistElement> sigma = createSigmaQueue();
        List<Typing> r = createResultList();
        ITypingStrategy typingStrategy = getTypingStrategy();
        BitSet wl = new BitSet(numAssignments);
        wl.set(0, numAssignments);
        sigma.add(new WorklistElement(tg, wl));
        Set<Type> throwable = null;
        while (!sigma.isEmpty()) {
            WorklistElement element = sigma.element();
            Typing tg2 = element.typing;
            BitSet wl2 = element.worklist;
            int defIdx = wl2.nextSetBit(0);
            if (defIdx == -1) {
                r.add(tg2);
                sigma.remove();
            } else {
                wl2.clear(defIdx);
                DefinitionStmt stmt = this.assignments.get(defIdx);
                Value lhs = stmt.getLeftOp();
                Local v = lhs instanceof Local ? (Local) lhs : (Local) ((ArrayRef) lhs).getBase();
                Type told = tg2.get(v);
                boolean isFirstType = true;
                Iterator<Type> it = ef.eval(tg2, stmt.getRightOp(), stmt).iterator();
                while (it.hasNext()) {
                    Type t_ = it.next();
                    if (lhs instanceof ArrayRef) {
                        if ((t_ instanceof RefType) || (t_ instanceof ArrayType) || (t_ instanceof WeakObjectType)) {
                            t_ = t_.makeArrayType();
                        }
                    }
                    if (!typesEqual(told, t_) && (told instanceof RefType) && (t_ instanceof RefType) && ((((RefType) told).getSootClass().isPhantom() || ((RefType) t_).getSootClass().isPhantom()) && (stmt.getRightOp() instanceof CaughtExceptionRef))) {
                        if (throwable == null) {
                            throwable = Collections.singleton(Scene.v().getBaseExceptionType());
                        }
                        lcas = throwable;
                    } else {
                        lcas = h.lcas(told, t_, true);
                    }
                    for (Type t : lcas) {
                        if (!typesEqual(t, told)) {
                            BitSet dependsV = this.depends.get(v);
                            if (isFirstType) {
                                tg_ = tg2;
                                wl_ = wl2;
                            } else {
                                tg_ = typingStrategy.createTyping(tg2);
                                wl_ = (BitSet) wl2.clone();
                                WorklistElement e = new WorklistElement(tg_, wl_);
                                sigma.add(e);
                            }
                            tg_.set(v, t);
                            if (dependsV != null) {
                                wl_.or(dependsV);
                            }
                        }
                        isFirstType = false;
                    }
                }
            }
        }
        typingStrategy.minimize(r, h);
        return r;
    }

    protected ArrayDeque<WorklistElement> createSigmaQueue() {
        return new ArrayDeque<>();
    }

    protected List<Typing> createResultList() {
        return new ArrayList();
    }

    public static boolean typesEqual(Type a, Type b) {
        if ((a instanceof ArrayType) && (b instanceof ArrayType)) {
            ArrayType a_ = (ArrayType) a;
            ArrayType b_ = (ArrayType) b;
            return a_.numDimensions == b_.numDimensions && a_.baseType.equals(b_.baseType);
        }
        return a.equals(b);
    }

    private void split_new() {
        Jimple jimp = Jimple.v();
        JimpleBody body = this.jb;
        LocalDefs defs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(body);
        PatchingChain<Unit> units = body.getUnits();
        Iterator<Unit> it = units.snapshotIterator();
        while (it.hasNext()) {
            Unit stmt = it.next();
            if (stmt instanceof InvokeStmt) {
                InvokeExpr invokeExpr = ((InvokeStmt) stmt).getInvokeExpr();
                if ((invokeExpr instanceof SpecialInvokeExpr) && "<init>".equals(invokeExpr.getMethodRef().getName())) {
                    SpecialInvokeExpr special = (SpecialInvokeExpr) invokeExpr;
                    List<Unit> defsOfAt = defs.getDefsOfAt((Local) special.getBase(), stmt);
                    while (true) {
                        List<Unit> deflist = defsOfAt;
                        if (deflist.size() != 1) {
                            break;
                        }
                        Stmt stmt2 = (Stmt) deflist.get(0);
                        if (stmt2 instanceof AssignStmt) {
                            AssignStmt assign = (AssignStmt) stmt2;
                            Value rightOp = assign.getRightOp();
                            if (rightOp instanceof Local) {
                                defsOfAt = defs.getDefsOfAt((Local) rightOp, assign);
                            } else if (rightOp instanceof NewExpr) {
                                Local newlocal = this.localGenerator.generateLocal(assign.getLeftOp().getType());
                                special.setBase(newlocal);
                                DefinitionStmt assignStmt = jimp.newAssignStmt(assign.getLeftOp(), newlocal);
                                units.insertAfter(assignStmt, (DefinitionStmt) Util.findLastIdentityUnit(body, assign));
                                assign.setLeftOp(newlocal);
                                initAssignment(assignStmt);
                            }
                        }
                    }
                }
            }
        }
    }
}
