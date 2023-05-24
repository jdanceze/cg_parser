package soot.jimple.infoflow.data;

import soot.coffi.Instruction;
import soot.jimple.infoflow.sourcesSinks.definitions.SourceSinkType;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/SootFieldAndClass.class */
public class SootFieldAndClass {
    private final String fieldName;
    private final String className;
    private final String fieldType;
    private Boolean isStatic;
    private SourceSinkType sourceSinkType;
    private int hashCode = 0;
    private String signature = null;

    public SootFieldAndClass(String fieldName, String className, String fieldType, SourceSinkType sourceSinkType) {
        this.sourceSinkType = SourceSinkType.Undefined;
        this.fieldName = fieldName;
        this.className = className;
        this.fieldType = fieldType;
        this.sourceSinkType = sourceSinkType;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public String getClassName() {
        return this.className;
    }

    public String getFieldType() {
        return this.fieldType;
    }

    public SourceSinkType getSourceSinkType() {
        return this.sourceSinkType;
    }

    public void setSourceSinkType(SourceSinkType sourceSinkType) {
        this.sourceSinkType = sourceSinkType;
    }

    public Boolean getStatic() {
        return this.isStatic;
    }

    public void setStatic(Boolean aStatic) {
        this.isStatic = aStatic;
    }

    public String getSignature() {
        if (this.signature != null) {
            return this.signature;
        }
        StringBuilder sb = new StringBuilder(10 + this.className.length() + this.fieldType.length() + this.fieldName.length());
        sb.append("<");
        sb.append(this.className);
        sb.append(": ");
        if (!this.fieldType.isEmpty()) {
            sb.append(this.fieldType);
            sb.append(Instruction.argsep);
        }
        sb.append(this.fieldName);
        sb.append(">");
        this.signature = sb.toString();
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = this.fieldName.hashCode() + (this.className.hashCode() * 5);
        }
        return this.hashCode;
    }

    public boolean equals(Object another) {
        if (!(another instanceof SootFieldAndClass)) {
            return false;
        }
        SootFieldAndClass otherMethod = (SootFieldAndClass) another;
        if (!this.fieldName.equals(otherMethod.fieldName) || !this.className.equals(otherMethod.className) || !this.fieldType.equals(otherMethod.fieldType)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "<" + this.className + ": " + this.fieldType + Instruction.argsep + this.fieldName + ">";
    }
}
