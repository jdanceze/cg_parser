package polyglot.visit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.resource.spi.work.WorkException;
import polyglot.ast.Binary;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassMember;
import polyglot.ast.CodeDecl;
import polyglot.ast.ConstructorCall;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FieldAssign;
import polyglot.ast.FieldDecl;
import polyglot.ast.Formal;
import polyglot.ast.Initializer;
import polyglot.ast.Local;
import polyglot.ast.LocalAssign;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Special;
import polyglot.ast.Term;
import polyglot.ast.Unary;
import polyglot.frontend.Job;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.types.VarInstance;
import polyglot.visit.DataFlow;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/InitChecker.class */
public class InitChecker extends DataFlow {
    protected ClassBodyInfo currCBI;
    protected static final DataFlow.Item BOTTOM = new DataFlow.Item() { // from class: polyglot.visit.InitChecker.1
        @Override // polyglot.visit.DataFlow.Item
        public boolean equals(Object i) {
            return i == this;
        }

        @Override // polyglot.visit.DataFlow.Item
        public int hashCode() {
            return -5826349;
        }
    };

    public InitChecker(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, ts, nf, true, false);
        this.currCBI = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/InitChecker$ClassBodyInfo.class */
    public static class ClassBodyInfo {
        ClassBodyInfo outer = null;
        CodeDecl currCodeDecl = null;
        Map currClassFinalFieldInitCounts = new HashMap();
        List allConstructors = new ArrayList();
        Map constructorCalls = new HashMap();
        Map fieldsConstructorInitializes = new HashMap();
        Set outerLocalsUsed = new HashSet();
        Map localsUsedInClassBodies = new HashMap();
        Set localDeclarations = new HashSet();

        protected ClassBodyInfo() {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/InitChecker$InitCount.class */
    public static class InitCount {
        static InitCount ZERO = new InitCount(0);
        static InitCount ONE = new InitCount(1);
        static InitCount MANY = new InitCount(2);
        protected int count;

        protected InitCount(int i) {
            this.count = i;
        }

        public int hashCode() {
            return this.count;
        }

        public boolean equals(Object o) {
            return (o instanceof InitCount) && this.count == ((InitCount) o).count;
        }

        public String toString() {
            if (this.count == 0) {
                return WorkException.UNDEFINED;
            }
            if (this.count == 1) {
                return WorkException.START_TIMED_OUT;
            }
            if (this.count == 2) {
                return "many";
            }
            throw new RuntimeException("Unexpected value for count");
        }

        public InitCount increment() {
            if (this.count == 0) {
                return ONE;
            }
            return MANY;
        }

        public static InitCount min(InitCount a, InitCount b) {
            if (ZERO.equals(a) || ZERO.equals(b)) {
                return ZERO;
            }
            if (ONE.equals(a) || ONE.equals(b)) {
                return ONE;
            }
            return MANY;
        }

        public static InitCount max(InitCount a, InitCount b) {
            if (MANY.equals(a) || MANY.equals(b)) {
                return MANY;
            }
            if (ONE.equals(a) || ONE.equals(b)) {
                return ONE;
            }
            return ZERO;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/InitChecker$MinMaxInitCount.class */
    public static class MinMaxInitCount {
        protected InitCount min;
        protected InitCount max;

        MinMaxInitCount(InitCount min, InitCount max) {
            this.min = min;
            this.max = max;
        }

        InitCount getMin() {
            return this.min;
        }

        InitCount getMax() {
            return this.max;
        }

        public int hashCode() {
            return (this.min.hashCode() * 4) + this.max.hashCode();
        }

        public String toString() {
            return new StringBuffer().append("[ min: ").append(this.min).append("; max: ").append(this.max).append(" ]").toString();
        }

        public boolean equals(Object o) {
            return (o instanceof MinMaxInitCount) && this.min.equals(((MinMaxInitCount) o).min) && this.max.equals(((MinMaxInitCount) o).max);
        }

        static MinMaxInitCount join(MinMaxInitCount initCount1, MinMaxInitCount initCount2) {
            if (initCount1 == null) {
                return initCount2;
            }
            if (initCount2 == null) {
                return initCount1;
            }
            MinMaxInitCount t = new MinMaxInitCount(InitCount.min(initCount1.getMin(), initCount2.getMin()), InitCount.max(initCount1.getMax(), initCount2.getMax()));
            return t;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/InitChecker$DataFlowItem.class */
    public static class DataFlowItem extends DataFlow.Item {
        Map initStatus;

        DataFlowItem(Map m) {
            this.initStatus = Collections.unmodifiableMap(m);
        }

        public String toString() {
            return this.initStatus.toString();
        }

        @Override // polyglot.visit.DataFlow.Item
        public boolean equals(Object o) {
            if (o instanceof DataFlowItem) {
                return this.initStatus.equals(((DataFlowItem) o).initStatus);
            }
            return false;
        }

        @Override // polyglot.visit.DataFlow.Item
        public int hashCode() {
            return this.initStatus.hashCode();
        }
    }

    @Override // polyglot.visit.DataFlow
    protected FlowGraph initGraph(CodeDecl code, Term root) {
        this.currCBI.currCodeDecl = code;
        return new FlowGraph(root, this.forward);
    }

    @Override // polyglot.visit.DataFlow, polyglot.visit.ErrorHandlingVisitor
    protected NodeVisitor enterCall(Node n) throws SemanticException {
        if (n instanceof ClassBody) {
            setupClassBody((ClassBody) n);
        }
        return super.enterCall(n);
    }

    @Override // polyglot.visit.DataFlow, polyglot.visit.ErrorHandlingVisitor
    public Node leaveCall(Node n) throws SemanticException {
        if (n instanceof ConstructorDecl) {
            this.currCBI.allConstructors.add(n);
            return n;
        }
        if (n instanceof ClassBody) {
            for (ConstructorDecl cd : this.currCBI.allConstructors) {
                dataflow(cd);
            }
            checkStaticFinalFieldsInit((ClassBody) n);
            checkNonStaticFinalFieldsInit((ClassBody) n);
            if (this.currCBI.outer != null) {
                this.currCBI.outer.localsUsedInClassBodies.put(n, this.currCBI.outerLocalsUsed);
            }
            this.currCBI = this.currCBI.outer;
        }
        return super.leaveCall(n);
    }

    protected void setupClassBody(ClassBody n) throws SemanticException {
        MinMaxInitCount initCount;
        ClassBodyInfo newCDI = new ClassBodyInfo();
        newCDI.outer = this.currCBI;
        this.currCBI = newCDI;
        for (ClassMember cm : n.members()) {
            if (cm instanceof FieldDecl) {
                FieldDecl fd = (FieldDecl) cm;
                if (fd.flags().isFinal()) {
                    if (fd.init() != null) {
                        initCount = new MinMaxInitCount(InitCount.ONE, InitCount.ONE);
                        if (this.currCBI.outer != null) {
                            dataflow(fd.init());
                        }
                    } else {
                        initCount = new MinMaxInitCount(InitCount.ZERO, InitCount.ZERO);
                    }
                    newCDI.currClassFinalFieldInitCounts.put(fd.fieldInstance(), initCount);
                }
            }
        }
    }

    protected void checkStaticFinalFieldsInit(ClassBody cb) throws SemanticException {
        for (Map.Entry e : this.currCBI.currClassFinalFieldInitCounts.entrySet()) {
            if (e.getKey() instanceof FieldInstance) {
                FieldInstance fi = (FieldInstance) e.getKey();
                if (fi.flags().isStatic() && fi.flags().isFinal()) {
                    MinMaxInitCount initCount = (MinMaxInitCount) e.getValue();
                    if (InitCount.ZERO.equals(initCount.getMin())) {
                        throw new SemanticException(new StringBuffer().append("field \"").append(fi.name()).append("\" might not have been initialized").toString(), cb.position());
                    }
                }
            }
        }
    }

    protected void checkNonStaticFinalFieldsInit(ClassBody cb) throws SemanticException {
        for (FieldInstance fi : this.currCBI.currClassFinalFieldInitCounts.keySet()) {
            if (fi.flags().isFinal() && !fi.flags().isStatic()) {
                boolean fieldInitializedBeforeConstructors = false;
                MinMaxInitCount ic = (MinMaxInitCount) this.currCBI.currClassFinalFieldInitCounts.get(fi);
                if (ic != null && !InitCount.ZERO.equals(ic.getMin())) {
                    fieldInitializedBeforeConstructors = true;
                }
                for (ConstructorDecl cd : this.currCBI.allConstructors) {
                    ConstructorInstance ciStart = cd.constructorInstance();
                    ConstructorInstance ci = ciStart;
                    boolean isInitialized = fieldInitializedBeforeConstructors;
                    while (ci != null) {
                        Set s = (Set) this.currCBI.fieldsConstructorInitializes.get(ci);
                        if (s != null && s.contains(fi)) {
                            if (isInitialized) {
                                throw new SemanticException(new StringBuffer().append("field \"").append(fi.name()).append("\" might have already been initialized").toString(), cd.position());
                            }
                            isInitialized = true;
                        }
                        ci = (ConstructorInstance) this.currCBI.constructorCalls.get(ci);
                    }
                    if (!isInitialized) {
                        throw new SemanticException(new StringBuffer().append("field \"").append(fi.name()).append("\" might not have been initialized").toString(), ciStart.position());
                    }
                }
                continue;
            }
        }
    }

    protected void dataflow(Expr root) throws SemanticException {
        FlowGraph g = new FlowGraph(root, this.forward);
        CFGBuilder v = createCFGBuilder(this.ts, g);
        v.visitGraph();
        dataflow(g);
        post(g, root);
    }

    @Override // polyglot.visit.DataFlow
    public DataFlow.Item createInitialItem(FlowGraph graph, Term node) {
        if (node == graph.startNode()) {
            return createInitDFI();
        }
        return BOTTOM;
    }

    private DataFlowItem createInitDFI() {
        return new DataFlowItem(new HashMap(this.currCBI.currClassFinalFieldInitCounts));
    }

    @Override // polyglot.visit.DataFlow
    protected DataFlow.Item confluence(List items, List itemKeys, Term node, FlowGraph graph) {
        if ((node instanceof Initializer) || (node instanceof ConstructorDecl)) {
            List filtered = filterItemsNonException(items, itemKeys);
            if (filtered.isEmpty()) {
                return createInitDFI();
            }
            if (filtered.size() == 1) {
                return (DataFlow.Item) filtered.get(0);
            }
            return confluence(filtered, node, graph);
        }
        return confluence(items, node, graph);
    }

    @Override // polyglot.visit.DataFlow
    public DataFlow.Item confluence(List inItems, Term node, FlowGraph graph) {
        Iterator iter = inItems.iterator();
        Map m = null;
        while (iter.hasNext()) {
            DataFlow.Item itm = (DataFlow.Item) iter.next();
            if (itm != BOTTOM) {
                if (m == null) {
                    m = new HashMap(((DataFlowItem) itm).initStatus);
                } else {
                    Map n = ((DataFlowItem) itm).initStatus;
                    for (Map.Entry entry : n.entrySet()) {
                        VarInstance v = (VarInstance) entry.getKey();
                        MinMaxInitCount initCount1 = (MinMaxInitCount) m.get(v);
                        MinMaxInitCount initCount2 = (MinMaxInitCount) entry.getValue();
                        m.put(v, MinMaxInitCount.join(initCount1, initCount2));
                    }
                }
            }
        }
        return m == null ? BOTTOM : new DataFlowItem(m);
    }

    @Override // polyglot.visit.DataFlow
    protected Map flow(List inItems, List inItemKeys, FlowGraph graph, Term n, Set edgeKeys) {
        return flowToBooleanFlow(inItems, inItemKeys, graph, n, edgeKeys);
    }

    @Override // polyglot.visit.DataFlow
    public Map flow(DataFlow.Item trueItem, DataFlow.Item falseItem, DataFlow.Item otherItem, FlowGraph graph, Term n, Set succEdgeKeys) {
        DataFlow.Item inItem = safeConfluence(trueItem, FlowGraph.EDGE_KEY_TRUE, falseItem, FlowGraph.EDGE_KEY_FALSE, otherItem, FlowGraph.EDGE_KEY_OTHER, n, graph);
        if (inItem == BOTTOM) {
            return itemToMap(BOTTOM, succEdgeKeys);
        }
        DataFlowItem inDFItem = (DataFlowItem) inItem;
        Map ret = null;
        if (n instanceof Formal) {
            ret = flowFormal(inDFItem, graph, (Formal) n, succEdgeKeys);
        } else if (n instanceof LocalDecl) {
            ret = flowLocalDecl(inDFItem, graph, (LocalDecl) n, succEdgeKeys);
        } else if (n instanceof LocalAssign) {
            ret = flowLocalAssign(inDFItem, graph, (LocalAssign) n, succEdgeKeys);
        } else if (n instanceof FieldAssign) {
            ret = flowFieldAssign(inDFItem, graph, (FieldAssign) n, succEdgeKeys);
        } else if (n instanceof ConstructorCall) {
            ret = flowConstructorCall(inDFItem, graph, (ConstructorCall) n, succEdgeKeys);
        } else if ((n instanceof Expr) && ((Expr) n).type().isBoolean() && ((n instanceof Binary) || (n instanceof Unary))) {
            if (trueItem == null) {
                trueItem = inDFItem;
            }
            if (falseItem == null) {
                falseItem = inDFItem;
            }
            ret = flowBooleanConditions(trueItem, falseItem, inDFItem, graph, (Expr) n, succEdgeKeys);
        }
        if (ret != null) {
            return ret;
        }
        return itemToMap(inItem, succEdgeKeys);
    }

    protected Map flowFormal(DataFlowItem inItem, FlowGraph graph, Formal f, Set succEdgeKeys) {
        Map m = new HashMap(inItem.initStatus);
        m.put(f.localInstance(), new MinMaxInitCount(InitCount.ONE, InitCount.ONE));
        this.currCBI.localDeclarations.add(f.localInstance());
        return itemToMap(new DataFlowItem(m), succEdgeKeys);
    }

    protected Map flowLocalDecl(DataFlowItem inItem, FlowGraph graph, LocalDecl ld, Set succEdgeKeys) {
        MinMaxInitCount initCount;
        Map m = new HashMap(inItem.initStatus);
        MinMaxInitCount minMaxInitCount = (MinMaxInitCount) m.get(ld.localInstance());
        if (ld.init() != null) {
            initCount = new MinMaxInitCount(InitCount.ONE, InitCount.ONE);
        } else {
            initCount = new MinMaxInitCount(InitCount.ZERO, InitCount.ZERO);
        }
        m.put(ld.localInstance(), initCount);
        this.currCBI.localDeclarations.add(ld.localInstance());
        return itemToMap(new DataFlowItem(m), succEdgeKeys);
    }

    protected Map flowLocalAssign(DataFlowItem inItem, FlowGraph graph, LocalAssign a, Set succEdgeKeys) {
        Local l = (Local) a.left();
        Map m = new HashMap(inItem.initStatus);
        MinMaxInitCount initCount = (MinMaxInitCount) m.get(l.localInstance());
        if (initCount == null) {
            initCount = new MinMaxInitCount(InitCount.ZERO, InitCount.ZERO);
        }
        m.put(l.localInstance(), new MinMaxInitCount(initCount.getMin().increment(), initCount.getMax().increment()));
        return itemToMap(new DataFlowItem(m), succEdgeKeys);
    }

    protected Map flowFieldAssign(DataFlowItem inItem, FlowGraph graph, FieldAssign a, Set succEdgeKeys) {
        Field f = (Field) a.left();
        FieldInstance fi = f.fieldInstance();
        if (fi.flags().isFinal() && isFieldsTargetAppropriate(f)) {
            Map m = new HashMap(inItem.initStatus);
            MinMaxInitCount initCount = (MinMaxInitCount) m.get(fi);
            if (initCount != null) {
                m.put(fi, new MinMaxInitCount(initCount.getMin().increment(), initCount.getMax().increment()));
                return itemToMap(new DataFlowItem(m), succEdgeKeys);
            }
            return null;
        }
        return null;
    }

    protected Map flowConstructorCall(DataFlowItem inItem, FlowGraph graph, ConstructorCall cc, Set succEdgeKeys) {
        if (ConstructorCall.THIS.equals(cc.kind())) {
            this.currCBI.constructorCalls.put(((ConstructorDecl) this.currCBI.currCodeDecl).constructorInstance(), cc.constructorInstance());
            return null;
        }
        return null;
    }

    protected boolean isFieldsTargetAppropriate(Field f) {
        if (!f.fieldInstance().flags().isStatic()) {
            return (f.target() instanceof Special) && Special.THIS.equals(((Special) f.target()).kind());
        }
        ClassType containingClass = (ClassType) this.currCBI.currCodeDecl.codeInstance().container();
        return containingClass.equals(f.fieldInstance().container());
    }

    @Override // polyglot.visit.DataFlow
    public void check(FlowGraph graph, Term n, DataFlow.Item inItem, Map outItems) throws SemanticException {
        Set localsUsed;
        DataFlowItem dfIn = (DataFlowItem) inItem;
        if (dfIn == null) {
            dfIn = createInitDFI();
        }
        DataFlowItem dfOut = null;
        if (outItems != null && !outItems.isEmpty()) {
            dfOut = (DataFlowItem) outItems.values().iterator().next();
        }
        if (n instanceof Local) {
            checkLocal(graph, (Local) n, dfIn, dfOut);
        } else if (n instanceof LocalAssign) {
            checkLocalAssign(graph, (LocalAssign) n, dfIn, dfOut);
        } else if (n instanceof FieldAssign) {
            checkFieldAssign(graph, (FieldAssign) n, dfIn, dfOut);
        } else if ((n instanceof ClassBody) && (localsUsed = (Set) this.currCBI.localsUsedInClassBodies.get(n)) != null) {
            checkLocalsUsedByInnerClass(graph, (ClassBody) n, localsUsed, dfIn, dfOut);
        }
        if (n == graph.finishNode()) {
            if (this.currCBI.currCodeDecl instanceof Initializer) {
                finishInitializer(graph, (Initializer) this.currCBI.currCodeDecl, dfIn, dfOut);
            }
            if (this.currCBI.currCodeDecl instanceof ConstructorDecl) {
                finishConstructorDecl(graph, (ConstructorDecl) this.currCBI.currCodeDecl, dfIn, dfOut);
            }
        }
    }

    protected void finishInitializer(FlowGraph graph, Initializer initializer, DataFlowItem dfIn, DataFlowItem dfOut) {
        for (Map.Entry e : dfOut.initStatus.entrySet()) {
            if (e.getKey() instanceof FieldInstance) {
                FieldInstance fi = (FieldInstance) e.getKey();
                if (fi.flags().isFinal()) {
                    this.currCBI.currClassFinalFieldInitCounts.put(fi, e.getValue());
                }
            }
        }
    }

    protected void finishConstructorDecl(FlowGraph graph, ConstructorDecl cd, DataFlowItem dfIn, DataFlowItem dfOut) {
        ConstructorInstance ci = cd.constructorInstance();
        HashSet hashSet = new HashSet();
        for (Map.Entry e : dfOut.initStatus.entrySet()) {
            if ((e.getKey() instanceof FieldInstance) && ((FieldInstance) e.getKey()).flags().isFinal() && !((FieldInstance) e.getKey()).flags().isStatic()) {
                FieldInstance fi = (FieldInstance) e.getKey();
                MinMaxInitCount initCount = (MinMaxInitCount) e.getValue();
                MinMaxInitCount origInitCount = (MinMaxInitCount) this.currCBI.currClassFinalFieldInitCounts.get(fi);
                if (initCount.getMin() == InitCount.ONE && (origInitCount == null || origInitCount.getMin() == InitCount.ZERO)) {
                    hashSet.add(fi);
                }
            }
        }
        if (!hashSet.isEmpty()) {
            this.currCBI.fieldsConstructorInitializes.put(ci, hashSet);
        }
    }

    protected void checkLocal(FlowGraph graph, Local l, DataFlowItem dfIn, DataFlowItem dfOut) throws SemanticException {
        if (!this.currCBI.localDeclarations.contains(l.localInstance())) {
            this.currCBI.outerLocalsUsed.add(l.localInstance());
            return;
        }
        MinMaxInitCount initCount = (MinMaxInitCount) dfIn.initStatus.get(l.localInstance());
        if (initCount != null && InitCount.ZERO.equals(initCount.getMin()) && l.reachable()) {
            throw new SemanticException(new StringBuffer().append("Local variable \"").append(l.name()).append("\" may not have been initialized").toString(), l.position());
        }
    }

    protected void checkLocalAssign(FlowGraph graph, LocalAssign a, DataFlowItem dfIn, DataFlowItem dfOut) throws SemanticException {
        LocalInstance li = ((Local) a.left()).localInstance();
        if (!this.currCBI.localDeclarations.contains(li)) {
            throw new SemanticException(new StringBuffer().append("Final local variable \"").append(li.name()).append("\" cannot be assigned to in an inner class.").toString(), a.position());
        }
        MinMaxInitCount initCount = (MinMaxInitCount) dfOut.initStatus.get(li);
        if (li.flags().isFinal() && InitCount.MANY.equals(initCount.getMax())) {
            throw new SemanticException(new StringBuffer().append("variable \"").append(li.name()).append("\" might already have been assigned to").toString(), a.position());
        }
    }

    protected void checkFieldAssign(FlowGraph graph, FieldAssign a, DataFlowItem dfIn, DataFlowItem dfOut) throws SemanticException {
        Field f = (Field) a.left();
        FieldInstance fi = f.fieldInstance();
        if (fi.flags().isFinal()) {
            if (((this.currCBI.currCodeDecl instanceof ConstructorDecl) || (this.currCBI.currCodeDecl instanceof Initializer)) && isFieldsTargetAppropriate(f)) {
                MinMaxInitCount initCount = (MinMaxInitCount) dfOut.initStatus.get(fi);
                if (InitCount.MANY.equals(initCount.getMax())) {
                    throw new SemanticException(new StringBuffer().append("field \"").append(fi.name()).append("\" might already have been assigned to").toString(), a.position());
                }
                return;
            }
            throw new SemanticException(new StringBuffer().append("Cannot assign a value to final field \"").append(fi.name()).append("\"").toString(), a.position());
        }
    }

    protected void checkLocalsUsedByInnerClass(FlowGraph graph, ClassBody cb, Set localsUsed, DataFlowItem dfIn, DataFlowItem dfOut) throws SemanticException {
        Iterator iter = localsUsed.iterator();
        while (iter.hasNext()) {
            LocalInstance li = (LocalInstance) iter.next();
            MinMaxInitCount initCount = (MinMaxInitCount) dfOut.initStatus.get(li);
            if (!this.currCBI.localDeclarations.contains(li)) {
                this.currCBI.outerLocalsUsed.add(li);
            } else if (initCount == null || InitCount.ZERO.equals(initCount.getMin())) {
                throw new SemanticException(new StringBuffer().append("Local variable \"").append(li.name()).append("\" must be initialized before the class ").append("declaration.").toString(), cb.position());
            }
        }
    }
}
