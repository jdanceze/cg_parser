package soot.jimple.spark.pag;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.Context;
import soot.EntryPoints;
import soot.G;
import soot.JavaMethods;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.VoidType;
import soot.jimple.Stmt;
import soot.jimple.spark.builder.MethodNodeFactory;
import soot.jimple.spark.internal.SparkLibraryHelper;
import soot.util.NumberedString;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/MethodPAG.class */
public final class MethodPAG {
    private PAG pag;
    private Set<Context> addedContexts;
    SootMethod method;
    protected MethodNodeFactory nodeFactory;
    private static final String mainSubSignature = SootMethod.getSubSignature("main", Collections.singletonList(ArrayType.v(RefType.v("java.lang.String"), 1)), VoidType.v());
    private final ChunkedQueue<Node> internalEdges = new ChunkedQueue<>();
    private final ChunkedQueue<Node> inEdges = new ChunkedQueue<>();
    private final ChunkedQueue<Node> outEdges = new ChunkedQueue<>();
    private final QueueReader<Node> internalReader = this.internalEdges.reader();
    private final QueueReader<Node> inReader = this.inEdges.reader();
    private final QueueReader<Node> outReader = this.outEdges.reader();
    protected boolean hasBeenAdded = false;
    protected boolean hasBeenBuilt = false;
    protected final NumberedString sigCanonicalize = Scene.v().getSubSigNumberer().findOrAdd("java.lang.String canonicalize(java.lang.String)");

    public PAG pag() {
        return this.pag;
    }

    protected MethodPAG(PAG pag, SootMethod m) {
        this.pag = pag;
        this.method = m;
        this.nodeFactory = new MethodNodeFactory(pag, this);
    }

    public void addToPAG(Context varNodeParameter) {
        if (!this.hasBeenBuilt) {
            throw new RuntimeException(String.format("No PAG built for context %s and method %s", varNodeParameter, this.method));
        }
        if (varNodeParameter == null) {
            if (this.hasBeenAdded) {
                return;
            }
            this.hasBeenAdded = true;
        } else {
            if (this.addedContexts == null) {
                this.addedContexts = new HashSet();
            }
            if (!this.addedContexts.add(varNodeParameter)) {
                return;
            }
        }
        QueueReader<Node> reader = this.internalReader.m3089clone();
        while (reader.hasNext()) {
            Node src = reader.next();
            Node src2 = parameterize(src, varNodeParameter);
            Node dst = reader.next();
            this.pag.addEdge(src2, parameterize(dst, varNodeParameter));
        }
        QueueReader<Node> reader2 = this.inReader.m3089clone();
        while (reader2.hasNext()) {
            Node src3 = reader2.next();
            Node dst2 = reader2.next();
            this.pag.addEdge(src3, parameterize(dst2, varNodeParameter));
        }
        QueueReader<Node> reader3 = this.outReader.m3089clone();
        while (reader3.hasNext()) {
            Node src4 = reader3.next();
            Node src5 = parameterize(src4, varNodeParameter);
            Node dst3 = reader3.next();
            this.pag.addEdge(src5, dst3);
        }
    }

    public void addInternalEdge(Node src, Node dst) {
        if (src == null) {
            return;
        }
        this.internalEdges.add(src);
        this.internalEdges.add(dst);
        if (this.hasBeenAdded) {
            this.pag.addEdge(src, dst);
        }
    }

    public void addInEdge(Node src, Node dst) {
        if (src == null) {
            return;
        }
        this.inEdges.add(src);
        this.inEdges.add(dst);
        if (this.hasBeenAdded) {
            this.pag.addEdge(src, dst);
        }
    }

    public void addOutEdge(Node src, Node dst) {
        if (src == null) {
            return;
        }
        this.outEdges.add(src);
        this.outEdges.add(dst);
        if (this.hasBeenAdded) {
            this.pag.addEdge(src, dst);
        }
    }

    public SootMethod getMethod() {
        return this.method;
    }

    public MethodNodeFactory nodeFactory() {
        return this.nodeFactory;
    }

    public static MethodPAG v(PAG pag, SootMethod m) {
        MethodPAG ret = G.v().MethodPAG_methodToPag.get(m);
        if (ret == null) {
            ret = new MethodPAG(pag, m);
            G.v().MethodPAG_methodToPag.put(m, ret);
        }
        return ret;
    }

    public void build() {
        if (this.hasBeenBuilt) {
            return;
        }
        this.hasBeenBuilt = true;
        if (this.method.isNative()) {
            if (pag().getOpts().simulate_natives()) {
                buildNative();
            }
        } else if (this.method.isConcrete() && !this.method.isPhantom()) {
            buildNormal();
        }
        addMiscEdges();
    }

    protected VarNode parameterize(LocalVarNode vn, Context varNodeParameter) {
        SootMethod m = vn.getMethod();
        if (m != this.method && m != null) {
            throw new RuntimeException("VarNode " + vn + " with method " + m + " parameterized in method " + this.method);
        }
        return pag().makeContextVarNode(vn, varNodeParameter);
    }

    protected FieldRefNode parameterize(FieldRefNode frn, Context varNodeParameter) {
        return pag().makeFieldRefNode((VarNode) parameterize(frn.getBase(), varNodeParameter), frn.getField());
    }

    public Node parameterize(Node n, Context varNodeParameter) {
        if (varNodeParameter == null) {
            return n;
        }
        if (n instanceof LocalVarNode) {
            return parameterize((LocalVarNode) n, varNodeParameter);
        }
        if (n instanceof FieldRefNode) {
            return parameterize((FieldRefNode) n, varNodeParameter);
        }
        return n;
    }

    protected void buildNormal() {
        Body b = this.method.retrieveActiveBody();
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            this.nodeFactory.handleStmt((Stmt) u);
        }
    }

    protected void buildNative() {
        ValNode thisNode = null;
        ValNode retNode = null;
        if (!this.method.isStatic()) {
            thisNode = (ValNode) this.nodeFactory.caseThis();
        }
        if (this.method.getReturnType() instanceof RefLikeType) {
            retNode = (ValNode) this.nodeFactory.caseRet();
            if (this.pag.getCGOpts().library() != 1) {
                Type retType = this.method.getReturnType();
                retType.apply(new SparkLibraryHelper(this.pag, retNode, this.method));
            }
        }
        ValNode[] args = new ValNode[this.method.getParameterCount()];
        for (int i = 0; i < this.method.getParameterCount(); i++) {
            if (this.method.getParameterType(i) instanceof RefLikeType) {
                args[i] = (ValNode) this.nodeFactory.caseParm(i);
            }
        }
        this.pag.nativeMethodDriver.process(this.method, thisNode, retNode, args);
    }

    protected void addMiscEdges() {
        String signature = this.method.getSignature();
        if (this.method.getSubSignature().equals(mainSubSignature)) {
            addInEdge(pag().nodeFactory().caseArgv(), this.nodeFactory.caseParm(0));
        } else if (signature.equals(JavaMethods.THREAD_INIT_STRING)) {
            addInEdge(pag().nodeFactory().caseMainThread(), this.nodeFactory.caseThis());
            addInEdge(pag().nodeFactory().caseMainThreadGroup(), this.nodeFactory.caseParm(0));
        } else if (signature.equals("<java.lang.ref.Finalizer: void <init>(java.lang.Object)>")) {
            addInEdge(this.nodeFactory.caseThis(), pag().nodeFactory().caseFinalizeQueue());
        } else if (signature.equals(JavaMethods.RUN_FINALIZE)) {
            addInEdge(this.pag.nodeFactory().caseFinalizeQueue(), this.nodeFactory.caseThis());
        } else if (signature.equals("<java.lang.ref.Finalizer: void access$100(java.lang.Object)>")) {
            addInEdge(this.pag.nodeFactory().caseFinalizeQueue(), this.nodeFactory.caseParm(0));
        } else if (signature.equals(JavaMethods.CLASSLOADER_INIT)) {
            addInEdge(this.pag.nodeFactory().caseDefaultClassLoader(), this.nodeFactory.caseThis());
        } else if (signature.equals(JavaMethods.THREAD_EXIT)) {
            addInEdge(this.pag.nodeFactory().caseMainThread(), this.nodeFactory.caseThis());
        } else if (signature.equals(JavaMethods.PRIV_ACTION_EXC_INIT)) {
            addInEdge(this.pag.nodeFactory().caseThrow(), this.nodeFactory.caseParm(0));
            addInEdge(this.pag.nodeFactory().casePrivilegedActionException(), this.nodeFactory.caseThis());
        }
        if (this.method.getNumberedSubSignature().equals(this.sigCanonicalize)) {
            SootClass declaringClass = this.method.getDeclaringClass();
            while (true) {
                SootClass cl = declaringClass;
                if (cl == null) {
                    break;
                }
                if (cl.equals(Scene.v().getSootClassUnsafe("java.io.FileSystem"))) {
                    addInEdge(this.pag.nodeFactory().caseCanonicalPath(), this.nodeFactory.caseRet());
                }
                declaringClass = cl.getSuperclassUnsafe();
            }
        }
        boolean isImplicit = false;
        Iterator<SootMethod> it = EntryPoints.v().implicit().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            SootMethod implicitMethod = it.next();
            if (implicitMethod.getNumberedSubSignature().equals(this.method.getNumberedSubSignature())) {
                isImplicit = true;
                break;
            }
        }
        if (isImplicit) {
            SootClass declaringClass2 = this.method.getDeclaringClass();
            while (true) {
                SootClass c = declaringClass2;
                if (!c.getName().equals("java.lang.ClassLoader")) {
                    if (c.hasSuperclass()) {
                        declaringClass2 = c.getSuperclass();
                    } else {
                        return;
                    }
                } else if (!this.method.getName().equals("<init>")) {
                    addInEdge(pag().nodeFactory().caseDefaultClassLoader(), this.nodeFactory.caseThis());
                    addInEdge(pag().nodeFactory().caseMainClassNameString(), this.nodeFactory.caseParm(0));
                    return;
                } else {
                    return;
                }
            }
        }
    }
}
