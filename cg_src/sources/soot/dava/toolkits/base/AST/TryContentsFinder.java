package soot.dava.toolkits.base.AST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.Type;
import soot.Value;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.jimple.FieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.ThrowStmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/TryContentsFinder.class */
public class TryContentsFinder extends ASTAnalysis {
    private IterableSet curExceptionSet = new IterableSet();
    private final HashMap<Object, IterableSet> node2ExceptionSet = new HashMap<>();

    public TryContentsFinder(Singletons.Global g) {
    }

    public static TryContentsFinder v() {
        return G.v().soot_dava_toolkits_base_AST_TryContentsFinder();
    }

    @Override // soot.dava.toolkits.base.AST.ASTAnalysis
    public int getAnalysisDepth() {
        return 2;
    }

    public IterableSet remove_CurExceptionSet() {
        IterableSet s = this.curExceptionSet;
        set_CurExceptionSet(new IterableSet());
        return s;
    }

    public void set_CurExceptionSet(IterableSet curExceptionSet) {
        this.curExceptionSet = curExceptionSet;
    }

    @Override // soot.dava.toolkits.base.AST.ASTAnalysis
    public void analyseThrowStmt(ThrowStmt s) {
        Value op = s.getOp();
        if (op instanceof Local) {
            add_ThrownType(((Local) op).getType());
        } else if (op instanceof FieldRef) {
            add_ThrownType(((FieldRef) op).getType());
        }
    }

    private void add_ThrownType(Type t) {
        if (t instanceof RefType) {
            this.curExceptionSet.add(((RefType) t).getSootClass());
        }
    }

    @Override // soot.dava.toolkits.base.AST.ASTAnalysis
    public void analyseInvokeExpr(InvokeExpr ie) {
        this.curExceptionSet.addAll(ie.getMethod().getExceptions());
    }

    @Override // soot.dava.toolkits.base.AST.ASTAnalysis
    public void analyseInstanceInvokeExpr(InstanceInvokeExpr iie) {
        analyseInvokeExpr(iie);
    }

    @Override // soot.dava.toolkits.base.AST.ASTAnalysis
    public void analyseASTNode(ASTNode n) {
        if (n instanceof ASTTryNode) {
            ASTTryNode tryNode = (ASTTryNode) n;
            ArrayList<Object> toRemove = new ArrayList<>();
            IterableSet tryExceptionSet = this.node2ExceptionSet.get(tryNode.get_TryBodyContainer());
            if (tryExceptionSet == null) {
                tryExceptionSet = new IterableSet();
                this.node2ExceptionSet.put(tryNode.get_TryBodyContainer(), tryExceptionSet);
            }
            List<Object> catchBodies = tryNode.get_CatchList();
            List<Object> subBodies = tryNode.get_SubBodies();
            for (Object catchBody : catchBodies) {
                SootClass exception = (SootClass) tryNode.get_ExceptionMap().get(catchBody);
                if (!catches_Exception(tryExceptionSet, exception) && !catches_RuntimeException(exception)) {
                    toRemove.add(catchBody);
                }
            }
            Iterator<Object> trit = toRemove.iterator();
            while (trit.hasNext()) {
                Object catchBody2 = trit.next();
                subBodies.remove(catchBody2);
                catchBodies.remove(catchBody2);
            }
            IterableSet passingSet = (IterableSet) tryExceptionSet.clone();
            for (Object obj : catchBodies) {
                passingSet.remove(tryNode.get_ExceptionMap().get(obj));
            }
            for (Object obj2 : catchBodies) {
                passingSet.addAll(get_ExceptionSet(obj2));
            }
            this.node2ExceptionSet.put(n, passingSet);
        } else {
            Iterator<Object> sbit = n.get_SubBodies().iterator();
            while (sbit.hasNext()) {
                for (Object obj3 : (List) sbit.next()) {
                    add_ExceptionSet(n, get_ExceptionSet(obj3));
                }
            }
        }
        remove_CurExceptionSet();
    }

    public IterableSet get_ExceptionSet(Object node) {
        IterableSet fullSet = this.node2ExceptionSet.get(node);
        if (fullSet == null) {
            fullSet = new IterableSet();
            this.node2ExceptionSet.put(node, fullSet);
        }
        return fullSet;
    }

    public void add_ExceptionSet(Object node, IterableSet s) {
        IterableSet fullSet = this.node2ExceptionSet.get(node);
        if (fullSet == null) {
            fullSet = new IterableSet();
            this.node2ExceptionSet.put(node, fullSet);
        }
        fullSet.addAll(s);
    }

    private boolean catches_Exception(IterableSet tryExceptionSet, SootClass c) {
        Iterator it = tryExceptionSet.iterator();
        while (it.hasNext()) {
            SootClass sootClass = (SootClass) it.next();
            while (true) {
                SootClass thrownException = sootClass;
                if (thrownException == c) {
                    return true;
                }
                if (!thrownException.hasSuperclass()) {
                    break;
                }
                sootClass = thrownException.getSuperclass();
            }
        }
        return false;
    }

    private boolean catches_RuntimeException(SootClass c) {
        if (c == Scene.v().getSootClass("java.lang.Throwable") || c == Scene.v().getSootClass("java.lang.Exception")) {
            return true;
        }
        SootClass runtimeException = Scene.v().getSootClass("java.lang.RuntimeException");
        for (SootClass caughtException = c; caughtException != runtimeException; caughtException = caughtException.getSuperclass()) {
            if (!caughtException.hasSuperclass()) {
                return false;
            }
        }
        return true;
    }
}
