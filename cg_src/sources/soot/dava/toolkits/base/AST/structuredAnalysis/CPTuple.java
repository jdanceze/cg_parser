package soot.dava.toolkits.base.AST.structuredAnalysis;

import soot.dava.DavaFlowAnalysisException;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/structuredAnalysis/CPTuple.class */
public class CPTuple {
    private String sootClass;
    private CPVariable variable;
    private Object constant;
    private Boolean TOP;

    /* renamed from: clone */
    public CPTuple m2535clone() {
        if (isTop()) {
            return new CPTuple(this.sootClass, this.variable, true);
        }
        if (isValueADouble()) {
            return new CPTuple(this.sootClass, this.variable, new Double(((Double) this.constant).doubleValue()));
        }
        if (isValueAFloat()) {
            return new CPTuple(this.sootClass, this.variable, new Float(((Float) this.constant).floatValue()));
        }
        if (isValueALong()) {
            return new CPTuple(this.sootClass, this.variable, new Long(((Long) this.constant).longValue()));
        }
        if (isValueABoolean()) {
            return new CPTuple(this.sootClass, this.variable, new Boolean(((Boolean) this.constant).booleanValue()));
        }
        if (isValueAInteger()) {
            return new CPTuple(this.sootClass, this.variable, new Integer(((Integer) this.constant).intValue()));
        }
        throw new RuntimeException("illegal Constant Type...report to developer" + this.constant);
    }

    public CPTuple(String sootClass, CPVariable variable, Object constant) {
        this.TOP = new Boolean(false);
        if (!(constant instanceof Float) && !(constant instanceof Double) && !(constant instanceof Long) && !(constant instanceof Boolean) && !(constant instanceof Integer)) {
            throw new DavaFlowAnalysisException("Third argument of VariableValuePair not an acceptable constant value...report to developer");
        }
        this.sootClass = sootClass;
        this.variable = variable;
        this.constant = constant;
        this.TOP = new Boolean(false);
    }

    public CPTuple(String sootClass, CPVariable variable, boolean top) {
        this.TOP = new Boolean(false);
        this.sootClass = sootClass;
        this.variable = variable;
        setTop();
    }

    public boolean containsLocal() {
        return this.variable.containsLocal();
    }

    public boolean containsField() {
        return this.variable.containsSootField();
    }

    public boolean isTop() {
        return this.TOP.booleanValue();
    }

    public void setTop() {
        this.constant = null;
        this.TOP = new Boolean(true);
    }

    public boolean isValueADouble() {
        return this.constant instanceof Double;
    }

    public boolean isValueAFloat() {
        return this.constant instanceof Float;
    }

    public boolean isValueALong() {
        return this.constant instanceof Long;
    }

    public boolean isValueABoolean() {
        return this.constant instanceof Boolean;
    }

    public boolean isValueAInteger() {
        return this.constant instanceof Integer;
    }

    public Object getValue() {
        return this.constant;
    }

    public void setValue(Object constant) {
        if (!(constant instanceof Float) && !(constant instanceof Double) && !(constant instanceof Long) && !(constant instanceof Boolean) && !(constant instanceof Integer)) {
            throw new DavaFlowAnalysisException("argument to setValue not an acceptable constant value...report to developer");
        }
        this.constant = constant;
        this.TOP = new Boolean(false);
    }

    public String getSootClassName() {
        return this.sootClass;
    }

    public CPVariable getVariable() {
        return this.variable;
    }

    public boolean equals(Object other) {
        if (other instanceof CPTuple) {
            CPTuple var = (CPTuple) other;
            if (this.sootClass.equals(var.getSootClassName()) && this.variable.equals(var.getVariable()) && (isTop() & var.isTop())) {
                return true;
            }
            if (!isTop() && !var.isTop() && this.sootClass.equals(var.getSootClassName()) && this.variable.equals(var.getVariable()) && this.constant.equals(var.getValue())) {
                return true;
            }
            return false;
        }
        return false;
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        if (isTop()) {
            b.append("<" + this.sootClass + ", " + this.variable.toString() + ", TOP>");
        } else {
            b.append("<" + this.sootClass + ", " + this.variable.toString() + "," + this.constant.toString() + ">");
        }
        return b.toString();
    }
}
