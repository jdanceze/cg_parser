package soot.jimple.spark.internal;

import soot.G;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.toolkits.pointer.representations.AbstractObject;
import soot.jimple.toolkits.pointer.representations.ReferenceVariable;
import soot.jimple.toolkits.pointer.util.NativeHelper;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/internal/SparkNativeHelper.class */
public class SparkNativeHelper extends NativeHelper {
    protected PAG pag;

    public SparkNativeHelper(PAG pag) {
        this.pag = pag;
    }

    @Override // soot.jimple.toolkits.pointer.util.NativeHelper
    protected void assignImpl(ReferenceVariable lhs, ReferenceVariable rhs) {
        this.pag.addEdge((Node) rhs, (Node) lhs);
    }

    @Override // soot.jimple.toolkits.pointer.util.NativeHelper
    protected void assignObjectToImpl(ReferenceVariable lhs, AbstractObject obj) {
        VarNode var;
        AllocNode objNode = this.pag.makeAllocNode(new Pair("AbstractObject", obj.getType()), obj.getType(), null);
        if (lhs instanceof FieldRefNode) {
            var = this.pag.makeGlobalVarNode(objNode, objNode.getType());
            this.pag.addEdge((Node) lhs, var);
        } else {
            var = (VarNode) lhs;
        }
        this.pag.addEdge(objNode, var);
    }

    @Override // soot.jimple.toolkits.pointer.util.NativeHelper
    protected void throwExceptionImpl(AbstractObject obj) {
        AllocNode objNode = this.pag.makeAllocNode(new Pair("AbstractObject", obj.getType()), obj.getType(), null);
        this.pag.addEdge(objNode, this.pag.nodeFactory().caseThrow());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.jimple.toolkits.pointer.util.NativeHelper
    protected ReferenceVariable arrayElementOfImpl(ReferenceVariable base) {
        VarNode l;
        if (base instanceof VarNode) {
            l = (VarNode) base;
        } else {
            FieldRefNode b = (FieldRefNode) base;
            l = this.pag.makeGlobalVarNode(b, b.getType());
            this.pag.addEdge(b, l);
        }
        return this.pag.makeFieldRefNode(l, ArrayElement.v());
    }

    @Override // soot.jimple.toolkits.pointer.util.NativeHelper
    protected ReferenceVariable cloneObjectImpl(ReferenceVariable source) {
        return source;
    }

    @Override // soot.jimple.toolkits.pointer.util.NativeHelper
    protected ReferenceVariable newInstanceOfImpl(ReferenceVariable cls) {
        return this.pag.nodeFactory().caseNewInstance((VarNode) cls);
    }

    @Override // soot.jimple.toolkits.pointer.util.NativeHelper
    protected ReferenceVariable staticFieldImpl(String className, String fieldName) {
        SootClass c = RefType.v(className).getSootClass();
        SootField f = c.getFieldByName(fieldName);
        return this.pag.makeGlobalVarNode(f, f.getType());
    }

    @Override // soot.jimple.toolkits.pointer.util.NativeHelper
    protected ReferenceVariable tempFieldImpl(String fieldsig) {
        return this.pag.makeGlobalVarNode(new Pair("tempField", fieldsig), Scene.v().getObjectType());
    }

    @Override // soot.jimple.toolkits.pointer.util.NativeHelper
    protected ReferenceVariable tempVariableImpl() {
        PAG pag = this.pag;
        G v = G.v();
        int i = v.SparkNativeHelper_tempVar + 1;
        v.SparkNativeHelper_tempVar = i;
        return pag.makeGlobalVarNode(new Pair("TempVar", new Integer(i)), Scene.v().getObjectType());
    }

    @Override // soot.jimple.toolkits.pointer.util.NativeHelper
    protected ReferenceVariable tempLocalVariableImpl(SootMethod method) {
        PAG pag = this.pag;
        G v = G.v();
        int i = v.SparkNativeHelper_tempVar + 1;
        v.SparkNativeHelper_tempVar = i;
        return pag.makeLocalVarNode(new Pair("TempVar", new Integer(i)), Scene.v().getObjectType(), method);
    }
}
