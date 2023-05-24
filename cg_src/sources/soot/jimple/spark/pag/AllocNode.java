package soot.jimple.spark.pag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import soot.Context;
import soot.PhaseOptions;
import soot.RefType;
import soot.SootMethod;
import soot.Type;
import soot.coffi.Instruction;
import soot.options.CGOptions;
import soot.util.ArrayNumberer;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/AllocNode.class */
public class AllocNode extends Node implements Context {
    protected Object newExpr;
    protected Map<SparkField, AllocDotField> fields;
    private SootMethod method;

    public Object getNewExpr() {
        return this.newExpr;
    }

    public Collection<AllocDotField> getAllFieldRefs() {
        if (this.fields == null) {
            return Collections.emptySet();
        }
        return this.fields.values();
    }

    public AllocDotField dot(SparkField field) {
        if (this.fields == null) {
            return null;
        }
        return this.fields.get(field);
    }

    public String toString() {
        return "AllocNode " + getNumber() + Instruction.argsep + this.newExpr + " in method " + this.method;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AllocNode(PAG pag, Object newExpr, Type t, SootMethod m) {
        super(pag, t);
        this.method = m;
        if (t instanceof RefType) {
            RefType rt = (RefType) t;
            if (rt.getSootClass().isAbstract()) {
                boolean usesReflectionLog = new CGOptions(PhaseOptions.v().getPhaseOptions("cg")).reflection_log() != null;
                if (!usesReflectionLog) {
                    throw new RuntimeException("Attempt to create allocnode with abstract type " + t);
                }
            }
        }
        this.newExpr = newExpr;
        if (newExpr instanceof ContextVarNode) {
            throw new RuntimeException();
        }
        pag.getAllocNodeNumberer().add((ArrayNumberer<AllocNode>) this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addField(AllocDotField adf, SparkField field) {
        if (this.fields == null) {
            this.fields = new HashMap();
        }
        this.fields.put(field, adf);
    }

    public Set<AllocDotField> getFields() {
        if (this.fields == null) {
            return Collections.emptySet();
        }
        return new HashSet(this.fields.values());
    }

    public SootMethod getMethod() {
        return this.method;
    }
}
