package soot.dava.toolkits.base.AST.structuredAnalysis;

import soot.Local;
import soot.PrimType;
import soot.SootField;
import soot.dava.DavaFlowAnalysisException;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/structuredAnalysis/CPVariable.class */
public class CPVariable {
    private Local local;
    private SootField field;

    public CPVariable(SootField field) {
        this.field = field;
        this.local = null;
        if (!(field.getType() instanceof PrimType)) {
            throw new DavaFlowAnalysisException("Variables managed for CP should only be primitives");
        }
    }

    public CPVariable(Local local) {
        this.field = null;
        this.local = local;
        if (!(local.getType() instanceof PrimType)) {
            throw new DavaFlowAnalysisException("Variables managed for CP should only be primitives");
        }
    }

    public boolean containsLocal() {
        return this.local != null;
    }

    public boolean containsSootField() {
        return this.field != null;
    }

    public SootField getSootField() {
        if (containsSootField()) {
            return this.field;
        }
        throw new DavaFlowAnalysisException("getsootField invoked when variable is not a sootfield!!!");
    }

    public Local getLocal() {
        if (containsLocal()) {
            return this.local;
        }
        throw new DavaFlowAnalysisException("getLocal invoked when variable is not a local");
    }

    public boolean equals(CPVariable var) {
        if (containsLocal() && var.containsLocal() && getLocal().getName().equals(var.getLocal().getName())) {
            return true;
        }
        if (containsSootField() && var.containsSootField() && getSootField().getName().equals(var.getSootField().getName())) {
            return true;
        }
        return false;
    }

    public String toString() {
        if (containsLocal()) {
            return "Local: " + getLocal().getName();
        }
        if (containsSootField()) {
            return "SootField: " + getSootField().getName();
        }
        return "UNKNOWN CONSTANT_PROPAGATION_VARIABLE";
    }
}
