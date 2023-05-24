package soot.JastAddJ;

import beaver.Symbol;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import soot.Immediate;
import soot.JastAddJ.ASTNode;
import soot.JastAddJ.Constant;
import soot.JastAddJ.Problem;
import soot.Local;
import soot.Type;
import soot.Value;
import soot.jimple.ConcreteRef;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.StringConstant;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/ASTNode.class */
public class ASTNode<T extends ASTNode> extends Symbol implements Cloneable, Iterable<T> {
    protected static final String PRIMITIVE_PACKAGE_NAME = "@primitive";
    public static final boolean generatedWithCircularEnabled = true;
    public static final boolean generatedWithCacheCycle = false;
    public static final boolean generatedWithComponentCheck = false;
    protected static State state = new State();
    public boolean in$Circle = false;
    public boolean is$Final = false;
    private int childIndex;
    protected int numChildren;
    protected ASTNode parent;
    protected ASTNode[] children;

    public void flushCache() {
    }

    public void flushCollectionCache() {
    }

    @Override // beaver.Symbol
    /* renamed from: clone */
    public ASTNode<T> mo287clone() throws CloneNotSupportedException {
        ASTNode node = (ASTNode) super.mo287clone();
        node.in$Circle(false);
        node.is$Final(false);
        return node;
    }

    public ASTNode<T> copy() {
        try {
            ASTNode node = mo287clone();
            node.parent = null;
            if (this.children != null) {
                node.children = (ASTNode[]) this.children.clone();
            }
            return node;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    public ASTNode<T> fullCopy() {
        ASTNode tree = copy();
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                ASTNode child = this.children[i];
                if (child != null) {
                    tree.setChild(child.fullCopy(), i);
                }
            }
        }
        return tree;
    }

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/ASTNode$State.class */
    public class State {
        public static final int REWRITE_CHANGE = 1;
        public static final int REWRITE_NOCHANGE = 2;
        public static final int REWRITE_INTERRUPT = 3;
        public boolean IN_CIRCLE = false;
        public int CIRCLE_INDEX = 1;
        public boolean CHANGE = false;
        public boolean RESET_CYCLE = false;
        public int boundariesCrossed = 0;
        public Options options = new Options();
        public int replacePos = 0;
        protected int duringImplicitConstructor = 0;
        protected int duringBoundNames = 0;
        protected int duringNameResolution = 0;
        protected int duringSyntacticClassification = 0;
        protected int duringAnonymousClasses = 0;
        protected int duringVariableDeclarationTransformation = 0;
        protected int duringLiterals = 0;
        protected int duringDU = 0;
        protected int duringAnnotations = 0;
        protected int duringEnums = 0;
        protected int duringGenericTypeVariables = 0;
        private int[] stack = new int[64];
        private int pos = 0;

        /* compiled from: ASTNode$State.java */
        /* loaded from: gencallgraphv3.jar:soot/JastAddJ/ASTNode$State$CircularValue.class */
        public static class CircularValue {
            Object value;
            int visited = -1;
        }

        private void ensureSize(int size) {
            if (size < this.stack.length) {
                return;
            }
            int[] newStack = new int[this.stack.length * 2];
            System.arraycopy(this.stack, 0, newStack, 0, this.stack.length);
            this.stack = newStack;
        }

        public void push(int i) {
            ensureSize(this.pos + 1);
            int[] iArr = this.stack;
            int i2 = this.pos;
            this.pos = i2 + 1;
            iArr[i2] = i;
        }

        public int pop() {
            int[] iArr = this.stack;
            int i = this.pos - 1;
            this.pos = i;
            return iArr[i];
        }

        public int peek() {
            return this.stack[this.pos - 1];
        }

        /* compiled from: ASTNode$State.java */
        /* loaded from: gencallgraphv3.jar:soot/JastAddJ/ASTNode$State$IdentityHashSet.class */
        static class IdentityHashSet extends AbstractSet implements Set {
            private IdentityHashMap map;
            private static final Object PRESENT = new Object();

            public IdentityHashSet(int initialCapacity) {
                this.map = new IdentityHashMap(initialCapacity);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator iterator() {
                return this.map.keySet().iterator();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return this.map.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean isEmpty() {
                return this.map.isEmpty();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object o) {
                return this.map.containsKey(o);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean add(Object o) {
                return this.map.put(o, PRESENT) == null;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object o) {
                return this.map.remove(o) == PRESENT;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public void clear() {
                this.map.clear();
            }
        }

        public void reset() {
            this.IN_CIRCLE = false;
            this.CIRCLE_INDEX = 1;
            this.CHANGE = false;
            this.boundariesCrossed = 0;
            if (this.duringImplicitConstructor != 0) {
                System.out.println("Warning: resetting duringImplicitConstructor");
                this.duringImplicitConstructor = 0;
            }
            if (this.duringBoundNames != 0) {
                System.out.println("Warning: resetting duringBoundNames");
                this.duringBoundNames = 0;
            }
            if (this.duringNameResolution != 0) {
                System.out.println("Warning: resetting duringNameResolution");
                this.duringNameResolution = 0;
            }
            if (this.duringSyntacticClassification != 0) {
                System.out.println("Warning: resetting duringSyntacticClassification");
                this.duringSyntacticClassification = 0;
            }
            if (this.duringAnonymousClasses != 0) {
                System.out.println("Warning: resetting duringAnonymousClasses");
                this.duringAnonymousClasses = 0;
            }
            if (this.duringVariableDeclarationTransformation != 0) {
                System.out.println("Warning: resetting duringVariableDeclarationTransformation");
                this.duringVariableDeclarationTransformation = 0;
            }
            if (this.duringLiterals != 0) {
                System.out.println("Warning: resetting duringLiterals");
                this.duringLiterals = 0;
            }
            if (this.duringDU != 0) {
                System.out.println("Warning: resetting duringDU");
                this.duringDU = 0;
            }
            if (this.duringAnnotations != 0) {
                System.out.println("Warning: resetting duringAnnotations");
                this.duringAnnotations = 0;
            }
            if (this.duringEnums != 0) {
                System.out.println("Warning: resetting duringEnums");
                this.duringEnums = 0;
            }
            if (this.duringGenericTypeVariables != 0) {
                System.out.println("Warning: resetting duringGenericTypeVariables");
                this.duringGenericTypeVariables = 0;
            }
        }
    }

    public void accessControl() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void collectExceptions(Collection c, ASTNode target) {
        for (int i = 0; i < getNumChild(); i++) {
            getChild(i).collectExceptions(c, target);
        }
    }

    public void collectBranches(Collection c) {
        for (int i = 0; i < getNumChild(); i++) {
            getChild(i).collectBranches(c);
        }
    }

    public Stmt branchTarget(Stmt branchStmt) {
        if (getParent() != null) {
            return getParent().branchTarget(branchStmt);
        }
        return null;
    }

    public void collectFinally(Stmt branchStmt, ArrayList list) {
        if (getParent() != null) {
            getParent().collectFinally(branchStmt, list);
        }
    }

    public int varChildIndex(Block b) {
        ASTNode aSTNode = this;
        while (true) {
            ASTNode node = aSTNode;
            if (node.getParent().getParent() != b) {
                aSTNode = node.getParent();
            } else {
                return b.getStmtListNoTransform().getIndexOfChild(node);
            }
        }
    }

    public int varChildIndex(TypeDecl t) {
        ASTNode node;
        ASTNode aSTNode = this;
        while (true) {
            node = aSTNode;
            if (node == null || node.getParent() == null || node.getParent().getParent() == t) {
                break;
            }
            aSTNode = node.getParent();
        }
        if (node == null) {
            return -1;
        }
        return t.getBodyDeclListNoTransform().getIndexOfChild(node);
    }

    public void definiteAssignment() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean checkDUeverywhere(Variable v) {
        for (int i = 0; i < getNumChild(); i++) {
            if (!getChild(i).checkDUeverywhere(v)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isDescendantTo(ASTNode node) {
        if (this == node) {
            return true;
        }
        if (getParent() == null) {
            return false;
        }
        return getParent().isDescendantTo(node);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String sourceFile() {
        ASTNode node;
        ASTNode aSTNode = this;
        while (true) {
            node = aSTNode;
            if (node == null || (node instanceof CompilationUnit)) {
                break;
            }
            aSTNode = node.getParent();
        }
        if (node == null) {
            return "Unknown file";
        }
        CompilationUnit u = (CompilationUnit) node;
        return u.relativeName();
    }

    public ASTNode setLocation(ASTNode node) {
        setStart(node.getStart());
        setEnd(node.getEnd());
        return this;
    }

    public ASTNode setStart(int i) {
        this.start = i;
        return this;
    }

    public int start() {
        return this.start;
    }

    public ASTNode setEnd(int i) {
        this.end = i;
        return this;
    }

    public int end() {
        return this.end;
    }

    public String location() {
        return new StringBuilder().append(lineNumber()).toString();
    }

    public String errorPrefix() {
        return String.valueOf(sourceFile()) + ":" + location() + ":\n  *** Semantic Error: ";
    }

    public String warningPrefix() {
        return String.valueOf(sourceFile()) + ":" + location() + ":\n  *** WARNING: ";
    }

    public void error(String s) {
        ASTNode node;
        ASTNode aSTNode = this;
        while (true) {
            node = aSTNode;
            if (node == null || (node instanceof CompilationUnit)) {
                break;
            }
            aSTNode = node.getParent();
        }
        CompilationUnit cu = (CompilationUnit) node;
        if (getNumChild() == 0 && getStart() != 0 && getEnd() != 0) {
            int line = getLine(getStart());
            int column = getColumn(getStart());
            int endLine = getLine(getEnd());
            int endColumn = getColumn(getEnd());
            cu.errors.add(new Problem(sourceFile(), s, line, column, endLine, endColumn, Problem.Severity.ERROR, Problem.Kind.SEMANTIC));
            return;
        }
        cu.errors.add(new Problem(sourceFile(), s, lineNumber(), Problem.Severity.ERROR, Problem.Kind.SEMANTIC));
    }

    public void warning(String s) {
        ASTNode node;
        ASTNode aSTNode = this;
        while (true) {
            node = aSTNode;
            if (node == null || (node instanceof CompilationUnit)) {
                break;
            }
            aSTNode = node.getParent();
        }
        CompilationUnit cu = (CompilationUnit) node;
        cu.warnings.add(new Problem(sourceFile(), "WARNING: " + s, lineNumber(), Problem.Severity.WARNING));
    }

    public void exceptionHandling() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean reachedException(TypeDecl type) {
        for (int i = 0; i < getNumChild(); i++) {
            if (getChild(i).reachedException(type)) {
                return true;
            }
        }
        return false;
    }

    public static Collection removeInstanceMethods(Collection c) {
        Collection c2 = new LinkedList(c);
        Iterator iter = c2.iterator();
        while (iter.hasNext()) {
            MethodDecl m = (MethodDecl) iter.next();
            if (!m.isStatic()) {
                iter.remove();
            }
        }
        return c2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void putSimpleSetElement(HashMap map, Object key, Object value) {
        SimpleSet set = (SimpleSet) map.get(key);
        if (set == null) {
            set = SimpleSet.emptySet;
        }
        map.put(key, set.add(value));
    }

    public SimpleSet removeInstanceVariables(SimpleSet oldSet) {
        SimpleSet newSet = SimpleSet.emptySet;
        Iterator iter = oldSet.iterator();
        while (iter.hasNext()) {
            Variable v = (Variable) iter.next();
            if (!v.isInstanceVariable()) {
                newSet = newSet.add(v);
            }
        }
        return newSet;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkModifiers() {
    }

    public void nameCheck() {
    }

    public TypeDecl extractSingleType(SimpleSet c) {
        if (c.size() != 1) {
            return null;
        }
        return (TypeDecl) c.iterator().next();
    }

    public Options options() {
        return state().options;
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        toString(s);
        return s.toString().trim();
    }

    public void toString(StringBuffer s) {
        throw new Error("Operation toString(StringBuffer s) not implemented for " + getClass().getName());
    }

    public String dumpTree() {
        StringBuffer s = new StringBuffer();
        dumpTree(s, 0);
        return s.toString();
    }

    public void dumpTree(StringBuffer s, int j) {
        for (int i = 0; i < j; i++) {
            s.append("  ");
        }
        s.append(String.valueOf(dumpString()) + "\n");
        for (int i2 = 0; i2 < getNumChild(); i2++) {
            getChild(i2).dumpTree(s, j + 1);
        }
    }

    public String dumpTreeNoRewrite() {
        StringBuffer s = new StringBuffer();
        dumpTreeNoRewrite(s, 0);
        return s.toString();
    }

    protected void dumpTreeNoRewrite(StringBuffer s, int indent) {
        for (int i = 0; i < indent; i++) {
            s.append("  ");
        }
        s.append(dumpString());
        s.append("\n");
        for (int i2 = 0; i2 < getNumChildNoTransform(); i2++) {
            getChildNoTransform(i2).dumpTreeNoRewrite(s, indent + 1);
        }
    }

    public void typeCheck() {
    }

    void checkUnreachableStmt() {
    }

    public void clearLocations() {
        setStart(0);
        setEnd(0);
        for (int i = 0; i < getNumChildNoTransform(); i++) {
            getChildNoTransform(i).clearLocations();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void transformEnumConstructors() {
        for (int i = 0; i < getNumChildNoTransform(); i++) {
            ASTNode child = getChildNoTransform(i);
            if (child != null) {
                child.transformEnumConstructors();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkEnum(EnumDecl enumDecl) {
        for (int i = 0; i < getNumChild(); i++) {
            getChild(i).checkEnum(enumDecl);
        }
    }

    public void flushCaches() {
        flushCache();
        for (int i = 0; i < getNumChild(); i++) {
            getChild(i).flushCaches();
        }
    }

    public void collectEnclosingVariables(HashSet set, TypeDecl typeDecl) {
        for (int i = 0; i < getNumChild(); i++) {
            getChild(i).collectEnclosingVariables(set, typeDecl);
        }
    }

    public void transformation() {
        for (int i = 0; i < getNumChild(); i++) {
            getChild(i).transformation();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ASTNode replace(ASTNode node) {
        state().replacePos = node.getParent().getIndexOfChild(node);
        node.getParent().in$Circle(true);
        return node.getParent();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ASTNode with(ASTNode node) {
        setChild(node, state().replacePos);
        in$Circle(false);
        return node;
    }

    public void jimplify1phase1() {
        for (int i = 0; i < getNumChild(); i++) {
            getChild(i).jimplify1phase1();
        }
    }

    public void jimplify1phase2() {
        for (int i = 0; i < getNumChild(); i++) {
            getChild(i).jimplify1phase2();
        }
    }

    public void jimplify2() {
        for (int i = 0; i < getNumChild(); i++) {
            getChild(i).jimplify2();
        }
    }

    public void jimplify2(Body b) {
        for (int i = 0; i < getNumChild(); i++) {
            getChild(i).jimplify2(b);
        }
    }

    public Immediate asImmediate(Body b, Value v) {
        return v instanceof Immediate ? (Immediate) v : b.newTemp(v);
    }

    public Local asLocal(Body b, Value v) {
        return v instanceof Local ? (Local) v : b.newTemp(v);
    }

    public Local asLocal(Body b, Value v, Type t) {
        if (v instanceof Local) {
            return (Local) v;
        }
        Local local = b.newTemp(t);
        b.add(b.newAssignStmt(local, v, null));
        b.copyLocation(v, local);
        return local;
    }

    public Value asRValue(Body b, Value v) {
        if (!(v instanceof Local) && !(v instanceof soot.jimple.Constant) && !(v instanceof ConcreteRef) && !(v instanceof soot.jimple.Expr)) {
            throw new Error("Need to convert " + v.getClass().getName() + " to RValue");
        }
        return v;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public soot.jimple.Stmt newLabel() {
        return Jimple.v().newNopStmt();
    }

    public void addAttributes() {
    }

    public static Value emitConstant(Constant constant) {
        if (constant instanceof Constant.ConstantInt) {
            return IntType.emitConstant(constant.intValue());
        }
        if (constant instanceof Constant.ConstantLong) {
            return LongConstant.v(constant.longValue());
        }
        if (constant instanceof Constant.ConstantFloat) {
            return FloatConstant.v(constant.floatValue());
        }
        if (constant instanceof Constant.ConstantDouble) {
            return DoubleConstant.v(constant.doubleValue());
        }
        if (constant instanceof Constant.ConstantChar) {
            return IntType.emitConstant(constant.intValue());
        }
        if (constant instanceof Constant.ConstantBoolean) {
            return BooleanType.emitConstant(constant.booleanValue());
        }
        if (constant instanceof Constant.ConstantString) {
            return StringConstant.v(constant.stringValue());
        }
        throw new Error("Unexpected constant");
    }

    public void endExceptionRange(Body b, ArrayList list) {
        if (list != null) {
            soot.jimple.Stmt label = newLabel();
            b.addLabel(label);
            list.add(label);
        }
    }

    public void beginExceptionRange(Body b, ArrayList list) {
        if (list != null) {
            b.addNextStmt(list);
        }
    }

    public ASTNode cloneSubtree() {
        try {
            ASTNode tree = mo287clone();
            tree.setParent(null);
            if (this.children != null) {
                tree.children = new ASTNode[this.children.length];
                for (int i = 0; i < this.children.length; i++) {
                    if (this.children[i] == null) {
                        tree.children[i] = null;
                    } else {
                        tree.children[i] = this.children[i].cloneSubtree();
                        tree.children[i].setParent(tree);
                    }
                }
            }
            return tree;
        } catch (CloneNotSupportedException e) {
            throw new Error("Error: clone not supported for " + getClass().getName());
        }
    }

    public void checkUncheckedConversion(TypeDecl source, TypeDecl dest) {
        if (source.isUncheckedConversionTo(dest)) {
            warning("unchecked conversion from raw type " + source.typeName() + " to generic type " + dest.typeName());
        }
    }

    public void checkWarnings() {
    }

    public void collectTypesToHierarchy(Collection<Type> set) {
        for (int i = 0; i < getNumChild(); i++) {
            getChild(i).collectTypesToHierarchy(set);
        }
    }

    public void collectTypesToSignatures(Collection<Type> set) {
        for (int i = 0; i < getNumChild(); i++) {
            getChild(i).collectTypesToSignatures(set);
        }
    }

    public ASTNode() {
        init$Children();
    }

    public void init$Children() {
    }

    public final State state() {
        return state;
    }

    public boolean in$Circle() {
        return this.in$Circle;
    }

    public void in$Circle(boolean b) {
        this.in$Circle = b;
    }

    public boolean is$Final() {
        return this.is$Final;
    }

    public void is$Final(boolean b) {
        this.is$Final = b;
    }

    public T getChild(int i) {
        int rewriteState;
        T childNoTransform = getChildNoTransform(i);
        if (childNoTransform == null) {
            return null;
        }
        if (childNoTransform.is$Final()) {
            return childNoTransform;
        }
        if (!childNoTransform.mayHaveRewrite()) {
            childNoTransform.is$Final(is$Final());
            return childNoTransform;
        }
        if (!childNoTransform.in$Circle()) {
            int num = state().boundariesCrossed;
            do {
                state().push(1);
                ASTNode oldNode = childNoTransform;
                oldNode.in$Circle(true);
                childNoTransform = childNoTransform.rewriteTo();
                if (childNoTransform != oldNode) {
                    setChild(childNoTransform, i);
                }
                oldNode.in$Circle(false);
                rewriteState = state().pop();
            } while (rewriteState == 1);
            if (rewriteState == 2 && is$Final()) {
                childNoTransform.is$Final(true);
                state().boundariesCrossed = num;
            }
        } else if (is$Final() != childNoTransform.is$Final()) {
            state().boundariesCrossed++;
        }
        return childNoTransform;
    }

    public int getIndexOfChild(ASTNode node) {
        if (node != null && node.childIndex < getNumChildNoTransform() && node == getChildNoTransform(node.childIndex)) {
            return node.childIndex;
        }
        for (int i = 0; i < getNumChildNoTransform(); i++) {
            if (getChildNoTransform(i) == node) {
                node.childIndex = i;
                return i;
            }
        }
        return -1;
    }

    public void addChild(T node) {
        setChild(node, getNumChildNoTransform());
    }

    public final T getChildNoTransform(int i) {
        if (this.children != null) {
            return (T) this.children[i];
        }
        return null;
    }

    protected int numChildren() {
        return this.numChildren;
    }

    public int getNumChild() {
        return numChildren();
    }

    public final int getNumChildNoTransform() {
        return numChildren();
    }

    public void setChild(ASTNode node, int i) {
        if (this.children == null) {
            this.children = new ASTNode[i + 1 > 4 ? i + 1 : 4];
        } else if (i >= this.children.length) {
            ASTNode[] c = new ASTNode[i << 1];
            System.arraycopy(this.children, 0, c, 0, this.children.length);
            this.children = c;
        }
        this.children[i] = node;
        if (i >= this.numChildren) {
            this.numChildren = i + 1;
        }
        if (node != null) {
            node.setParent(this);
            node.childIndex = i;
        }
    }

    public void insertChild(ASTNode node, int i) {
        if (this.children == null) {
            this.children = new ASTNode[i + 1 > 4 ? i + 1 : 4];
            this.children[i] = node;
        } else {
            ASTNode[] c = new ASTNode[this.children.length + 1];
            System.arraycopy(this.children, 0, c, 0, i);
            c[i] = node;
            if (i < this.children.length) {
                System.arraycopy(this.children, i, c, i + 1, this.children.length - i);
                for (int j = i + 1; j < c.length; j++) {
                    if (c[j] != null) {
                        c[j].childIndex = j;
                    }
                }
            }
            this.children = c;
        }
        this.numChildren++;
        if (node != null) {
            node.setParent(this);
            node.childIndex = i;
        }
    }

    public void removeChild(int i) {
        if (this.children != null) {
            ASTNode child = this.children[i];
            if (child != null) {
                child.parent = null;
                child.childIndex = -1;
            }
            if ((this instanceof List) || (this instanceof Opt)) {
                System.arraycopy(this.children, i + 1, this.children, i, (this.children.length - i) - 1);
                this.children[this.children.length - 1] = null;
                this.numChildren--;
                for (int j = i; j < this.numChildren; j++) {
                    if (this.children[j] != null) {
                        this.children[j].childIndex = j;
                    }
                }
                return;
            }
            this.children[i] = null;
        }
    }

    public ASTNode getParent() {
        if (this.parent != null && this.parent.is$Final() != is$Final()) {
            state().boundariesCrossed++;
        }
        return this.parent;
    }

    public void setParent(ASTNode node) {
        this.parent = node;
    }

    protected boolean duringImplicitConstructor() {
        if (state().duringImplicitConstructor == 0) {
            return false;
        }
        state().pop();
        state().push(3);
        return true;
    }

    protected boolean duringBoundNames() {
        if (state().duringBoundNames == 0) {
            return false;
        }
        state().pop();
        state().push(3);
        return true;
    }

    protected boolean duringNameResolution() {
        if (state().duringNameResolution == 0) {
            return false;
        }
        state().pop();
        state().push(3);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean duringSyntacticClassification() {
        if (state().duringSyntacticClassification == 0) {
            return false;
        }
        state().pop();
        state().push(3);
        return true;
    }

    protected boolean duringAnonymousClasses() {
        if (state().duringAnonymousClasses == 0) {
            return false;
        }
        state().pop();
        state().push(3);
        return true;
    }

    protected boolean duringVariableDeclarationTransformation() {
        if (state().duringVariableDeclarationTransformation == 0) {
            return false;
        }
        state().pop();
        state().push(3);
        return true;
    }

    protected boolean duringLiterals() {
        if (state().duringLiterals == 0) {
            return false;
        }
        state().pop();
        state().push(3);
        return true;
    }

    protected boolean duringDU() {
        if (state().duringDU == 0) {
            return false;
        }
        state().pop();
        state().push(3);
        return true;
    }

    protected boolean duringAnnotations() {
        if (state().duringAnnotations == 0) {
            return false;
        }
        state().pop();
        state().push(3);
        return true;
    }

    protected boolean duringEnums() {
        if (state().duringEnums == 0) {
            return false;
        }
        state().pop();
        state().push(3);
        return true;
    }

    protected boolean duringGenericTypeVariables() {
        if (state().duringGenericTypeVariables == 0) {
            return false;
        }
        state().pop();
        state().push(3);
        return true;
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return (Iterator<T>) new Iterator<T>() { // from class: soot.JastAddJ.ASTNode.1
            private int counter = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.counter < ASTNode.this.getNumChild();
            }

            @Override // java.util.Iterator
            public T next() {
                if (hasNext()) {
                    ASTNode aSTNode = ASTNode.this;
                    int i = this.counter;
                    this.counter = i + 1;
                    return (T) aSTNode.getChild(i);
                }
                return null;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public boolean mayHaveRewrite() {
        return false;
    }

    public void collectErrors() {
        nameCheck();
        typeCheck();
        accessControl();
        exceptionHandling();
        checkUnreachableStmt();
        definiteAssignment();
        checkModifiers();
        checkWarnings();
        for (int i = 0; i < getNumChild(); i++) {
            getChild(i).collectErrors();
        }
    }

    public boolean unassignedEverywhere(Variable v, TryStmt stmt) {
        state();
        for (int i = 0; i < getNumChild(); i++) {
            if (!getChild(i).unassignedEverywhere(v, stmt)) {
                return false;
            }
        }
        return true;
    }

    public int lineNumber() {
        ASTNode n;
        state();
        ASTNode aSTNode = this;
        while (true) {
            n = aSTNode;
            if (n.getParent() == null || n.getStart() != 0) {
                break;
            }
            aSTNode = n.getParent();
        }
        return getLine(n.getStart());
    }

    public String indent() {
        state();
        String indent = extractIndent();
        return indent.startsWith("\n") ? indent : "\n" + indent;
    }

    public String extractIndent() {
        state();
        if (getParent() == null) {
            return "";
        }
        String indent = getParent().extractIndent();
        if (getParent().addsIndentationLevel()) {
            indent = String.valueOf(indent) + "  ";
        }
        return indent;
    }

    public boolean addsIndentationLevel() {
        state();
        return false;
    }

    public String dumpString() {
        state();
        return getClass().getName();
    }

    public boolean usesTypeVariable() {
        state();
        for (int i = 0; i < getNumChild(); i++) {
            if (getChild(i).usesTypeVariable()) {
                return true;
            }
        }
        return false;
    }

    public boolean isStringAdd() {
        state();
        return false;
    }

    public boolean definesLabel() {
        state();
        return false;
    }

    public CompilationUnit compilationUnit() {
        state();
        CompilationUnit compilationUnit_value = getParent().Define_CompilationUnit_compilationUnit(this, null);
        return compilationUnit_value;
    }

    public ASTNode rewriteTo() {
        if (state().peek() == 1) {
            state().pop();
            state().push(2);
        }
        return this;
    }

    public TypeDecl Define_TypeDecl_superType(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_superType(this, caller);
    }

    public ConstructorDecl Define_ConstructorDecl_constructorDecl(ASTNode caller, ASTNode child) {
        return getParent().Define_ConstructorDecl_constructorDecl(this, caller);
    }

    public TypeDecl Define_TypeDecl_componentType(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_componentType(this, caller);
    }

    public LabeledStmt Define_LabeledStmt_lookupLabel(ASTNode caller, ASTNode child, String name) {
        return getParent().Define_LabeledStmt_lookupLabel(this, caller, name);
    }

    public boolean Define_boolean_isDest(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_isDest(this, caller);
    }

    public boolean Define_boolean_isSource(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_isSource(this, caller);
    }

    public boolean Define_boolean_isIncOrDec(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_isIncOrDec(this, caller);
    }

    public boolean Define_boolean_isDAbefore(ASTNode caller, ASTNode child, Variable v) {
        return getParent().Define_boolean_isDAbefore(this, caller, v);
    }

    public boolean Define_boolean_isDUbefore(ASTNode caller, ASTNode child, Variable v) {
        return getParent().Define_boolean_isDUbefore(this, caller, v);
    }

    public TypeDecl Define_TypeDecl_typeException(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeException(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeRuntimeException(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeRuntimeException(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeError(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeError(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeNullPointerException(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeNullPointerException(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeThrowable(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeThrowable(this, caller);
    }

    public boolean Define_boolean_handlesException(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
        return getParent().Define_boolean_handlesException(this, caller, exceptionType);
    }

    public Collection Define_Collection_lookupConstructor(ASTNode caller, ASTNode child) {
        return getParent().Define_Collection_lookupConstructor(this, caller);
    }

    public Collection Define_Collection_lookupSuperConstructor(ASTNode caller, ASTNode child) {
        return getParent().Define_Collection_lookupSuperConstructor(this, caller);
    }

    public Expr Define_Expr_nestedScope(ASTNode caller, ASTNode child) {
        return getParent().Define_Expr_nestedScope(this, caller);
    }

    public Collection Define_Collection_lookupMethod(ASTNode caller, ASTNode child, String name) {
        return getParent().Define_Collection_lookupMethod(this, caller, name);
    }

    public TypeDecl Define_TypeDecl_typeObject(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeObject(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeCloneable(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeCloneable(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeSerializable(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeSerializable(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeBoolean(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeBoolean(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeByte(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeByte(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeShort(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeShort(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeChar(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeChar(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeInt(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeInt(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeLong(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeLong(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeFloat(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeFloat(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeDouble(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeDouble(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeString(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeString(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeVoid(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeVoid(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeNull(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeNull(this, caller);
    }

    public TypeDecl Define_TypeDecl_unknownType(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_unknownType(this, caller);
    }

    public boolean Define_boolean_hasPackage(ASTNode caller, ASTNode child, String packageName) {
        return getParent().Define_boolean_hasPackage(this, caller, packageName);
    }

    public TypeDecl Define_TypeDecl_lookupType(ASTNode caller, ASTNode child, String packageName, String typeName) {
        return getParent().Define_TypeDecl_lookupType(this, caller, packageName, typeName);
    }

    public SimpleSet Define_SimpleSet_lookupType(ASTNode caller, ASTNode child, String name) {
        return getParent().Define_SimpleSet_lookupType(this, caller, name);
    }

    public SimpleSet Define_SimpleSet_lookupVariable(ASTNode caller, ASTNode child, String name) {
        return getParent().Define_SimpleSet_lookupVariable(this, caller, name);
    }

    public boolean Define_boolean_mayBePublic(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_mayBePublic(this, caller);
    }

    public boolean Define_boolean_mayBeProtected(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_mayBeProtected(this, caller);
    }

    public boolean Define_boolean_mayBePrivate(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_mayBePrivate(this, caller);
    }

    public boolean Define_boolean_mayBeStatic(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_mayBeStatic(this, caller);
    }

    public boolean Define_boolean_mayBeFinal(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_mayBeFinal(this, caller);
    }

    public boolean Define_boolean_mayBeAbstract(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_mayBeAbstract(this, caller);
    }

    public boolean Define_boolean_mayBeVolatile(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_mayBeVolatile(this, caller);
    }

    public boolean Define_boolean_mayBeTransient(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_mayBeTransient(this, caller);
    }

    public boolean Define_boolean_mayBeStrictfp(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_mayBeStrictfp(this, caller);
    }

    public boolean Define_boolean_mayBeSynchronized(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_mayBeSynchronized(this, caller);
    }

    public boolean Define_boolean_mayBeNative(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_mayBeNative(this, caller);
    }

    public ASTNode Define_ASTNode_enclosingBlock(ASTNode caller, ASTNode child) {
        return getParent().Define_ASTNode_enclosingBlock(this, caller);
    }

    public VariableScope Define_VariableScope_outerScope(ASTNode caller, ASTNode child) {
        return getParent().Define_VariableScope_outerScope(this, caller);
    }

    public boolean Define_boolean_insideLoop(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_insideLoop(this, caller);
    }

    public boolean Define_boolean_insideSwitch(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_insideSwitch(this, caller);
    }

    public Case Define_Case_bind(ASTNode caller, ASTNode child, Case c) {
        return getParent().Define_Case_bind(this, caller, c);
    }

    public String Define_String_typeDeclIndent(ASTNode caller, ASTNode child) {
        return getParent().Define_String_typeDeclIndent(this, caller);
    }

    public NameType Define_NameType_nameType(ASTNode caller, ASTNode child) {
        return getParent().Define_NameType_nameType(this, caller);
    }

    public boolean Define_boolean_isAnonymous(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_isAnonymous(this, caller);
    }

    public Variable Define_Variable_unknownField(ASTNode caller, ASTNode child) {
        return getParent().Define_Variable_unknownField(this, caller);
    }

    public MethodDecl Define_MethodDecl_unknownMethod(ASTNode caller, ASTNode child) {
        return getParent().Define_MethodDecl_unknownMethod(this, caller);
    }

    public ConstructorDecl Define_ConstructorDecl_unknownConstructor(ASTNode caller, ASTNode child) {
        return getParent().Define_ConstructorDecl_unknownConstructor(this, caller);
    }

    public TypeDecl Define_TypeDecl_declType(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_declType(this, caller);
    }

    public BodyDecl Define_BodyDecl_enclosingBodyDecl(ASTNode caller, ASTNode child) {
        return getParent().Define_BodyDecl_enclosingBodyDecl(this, caller);
    }

    public boolean Define_boolean_isMemberType(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_isMemberType(this, caller);
    }

    public TypeDecl Define_TypeDecl_hostType(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_hostType(this, caller);
    }

    public TypeDecl Define_TypeDecl_switchType(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_switchType(this, caller);
    }

    public TypeDecl Define_TypeDecl_returnType(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_returnType(this, caller);
    }

    public TypeDecl Define_TypeDecl_enclosingInstance(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_enclosingInstance(this, caller);
    }

    public String Define_String_methodHost(ASTNode caller, ASTNode child) {
        return getParent().Define_String_methodHost(this, caller);
    }

    public boolean Define_boolean_inExplicitConstructorInvocation(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_inExplicitConstructorInvocation(this, caller);
    }

    public boolean Define_boolean_inStaticContext(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_inStaticContext(this, caller);
    }

    public boolean Define_boolean_reportUnreachable(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_reportUnreachable(this, caller);
    }

    public boolean Define_boolean_isMethodParameter(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_isMethodParameter(this, caller);
    }

    public boolean Define_boolean_isConstructorParameter(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_isConstructorParameter(this, caller);
    }

    public boolean Define_boolean_isExceptionHandlerParameter(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_isExceptionHandlerParameter(this, caller);
    }

    public boolean Define_boolean_mayUseAnnotationTarget(ASTNode caller, ASTNode child, String name) {
        return getParent().Define_boolean_mayUseAnnotationTarget(this, caller, name);
    }

    public ElementValue Define_ElementValue_lookupElementTypeValue(ASTNode caller, ASTNode child, String name) {
        return getParent().Define_ElementValue_lookupElementTypeValue(this, caller, name);
    }

    public boolean Define_boolean_withinSuppressWarnings(ASTNode caller, ASTNode child, String s) {
        return getParent().Define_boolean_withinSuppressWarnings(this, caller, s);
    }

    public boolean Define_boolean_withinDeprecatedAnnotation(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_withinDeprecatedAnnotation(this, caller);
    }

    public Annotation Define_Annotation_lookupAnnotation(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
        return getParent().Define_Annotation_lookupAnnotation(this, caller, typeDecl);
    }

    public TypeDecl Define_TypeDecl_enclosingAnnotationDecl(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_enclosingAnnotationDecl(this, caller);
    }

    public TypeDecl Define_TypeDecl_assignConvertedType(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_assignConvertedType(this, caller);
    }

    public boolean Define_boolean_inExtendsOrImplements(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_inExtendsOrImplements(this, caller);
    }

    public TypeDecl Define_TypeDecl_typeWildcard(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_typeWildcard(this, caller);
    }

    public TypeDecl Define_TypeDecl_lookupWildcardExtends(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
        return getParent().Define_TypeDecl_lookupWildcardExtends(this, caller, typeDecl);
    }

    public TypeDecl Define_TypeDecl_lookupWildcardSuper(ASTNode caller, ASTNode child, TypeDecl typeDecl) {
        return getParent().Define_TypeDecl_lookupWildcardSuper(this, caller, typeDecl);
    }

    public LUBType Define_LUBType_lookupLUBType(ASTNode caller, ASTNode child, Collection bounds) {
        return getParent().Define_LUBType_lookupLUBType(this, caller, bounds);
    }

    public GLBType Define_GLBType_lookupGLBType(ASTNode caller, ASTNode child, ArrayList bounds) {
        return getParent().Define_GLBType_lookupGLBType(this, caller, bounds);
    }

    public TypeDecl Define_TypeDecl_genericDecl(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_genericDecl(this, caller);
    }

    public boolean Define_boolean_variableArityValid(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_variableArityValid(this, caller);
    }

    public TypeDecl Define_TypeDecl_expectedType(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_expectedType(this, caller);
    }

    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_false_label(ASTNode caller, ASTNode child) {
        return getParent().Define_soot_jimple_Stmt_condition_false_label(this, caller);
    }

    public soot.jimple.Stmt Define_soot_jimple_Stmt_condition_true_label(ASTNode caller, ASTNode child) {
        return getParent().Define_soot_jimple_Stmt_condition_true_label(this, caller);
    }

    public int Define_int_localNum(ASTNode caller, ASTNode child) {
        return getParent().Define_int_localNum(this, caller);
    }

    public boolean Define_boolean_enclosedByExceptionHandler(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_enclosedByExceptionHandler(this, caller);
    }

    public ArrayList Define_ArrayList_exceptionRanges(ASTNode caller, ASTNode child) {
        return getParent().Define_ArrayList_exceptionRanges(this, caller);
    }

    public boolean Define_boolean_isCatchParam(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_isCatchParam(this, caller);
    }

    public CatchClause Define_CatchClause_catchClause(ASTNode caller, ASTNode child) {
        return getParent().Define_CatchClause_catchClause(this, caller);
    }

    public boolean Define_boolean_resourcePreviouslyDeclared(ASTNode caller, ASTNode child, String name) {
        return getParent().Define_boolean_resourcePreviouslyDeclared(this, caller, name);
    }

    public ClassInstanceExpr Define_ClassInstanceExpr_getClassInstanceExpr(ASTNode caller, ASTNode child) {
        return getParent().Define_ClassInstanceExpr_getClassInstanceExpr(this, caller);
    }

    public boolean Define_boolean_isAnonymousDecl(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_isAnonymousDecl(this, caller);
    }

    public boolean Define_boolean_isExplicitGenericConstructorAccess(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_isExplicitGenericConstructorAccess(this, caller);
    }

    public CompilationUnit Define_CompilationUnit_compilationUnit(ASTNode caller, ASTNode child) {
        return getParent().Define_CompilationUnit_compilationUnit(this, caller);
    }

    public SimpleSet Define_SimpleSet_allImportedTypes(ASTNode caller, ASTNode child, String name) {
        return getParent().Define_SimpleSet_allImportedTypes(this, caller, name);
    }

    public String Define_String_packageName(ASTNode caller, ASTNode child) {
        return getParent().Define_String_packageName(this, caller);
    }

    public TypeDecl Define_TypeDecl_enclosingType(ASTNode caller, ASTNode child) {
        return getParent().Define_TypeDecl_enclosingType(this, caller);
    }

    public boolean Define_boolean_isNestedType(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_isNestedType(this, caller);
    }

    public boolean Define_boolean_isLocalClass(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_isLocalClass(this, caller);
    }

    public String Define_String_hostPackage(ASTNode caller, ASTNode child) {
        return getParent().Define_String_hostPackage(this, caller);
    }

    public boolean Define_boolean_reachable(ASTNode caller, ASTNode child) {
        return getParent().Define_boolean_reachable(this, caller);
    }

    public boolean Define_boolean_inhModifiedInScope(ASTNode caller, ASTNode child, Variable var) {
        return getParent().Define_boolean_inhModifiedInScope(this, caller, var);
    }

    public boolean Define_boolean_reachableCatchClause(ASTNode caller, ASTNode child, TypeDecl exceptionType) {
        return getParent().Define_boolean_reachableCatchClause(this, caller, exceptionType);
    }

    public Collection<TypeDecl> Define_Collection_TypeDecl__caughtExceptions(ASTNode caller, ASTNode child) {
        return getParent().Define_Collection_TypeDecl__caughtExceptions(this, caller);
    }
}
