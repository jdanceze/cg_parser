package soot.jimple.infoflow.results.xml;

import java.util.Arrays;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/results/xml/SerializedAccessPath.class */
public class SerializedAccessPath {
    private final String base;
    private final String baseType;
    private final boolean taintSubFields;
    private final String[] fields;
    private final String[] types;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SerializedAccessPath(String base, String baseType, boolean taintSubFields, String[] fields, String[] types) {
        this.base = base;
        this.baseType = baseType;
        this.taintSubFields = taintSubFields;
        this.fields = fields;
        this.types = types;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.base == null ? 0 : this.base.hashCode());
        return (31 * ((31 * ((31 * ((31 * result) + (this.baseType == null ? 0 : this.baseType.hashCode()))) + Arrays.hashCode(this.fields))) + (this.taintSubFields ? 1231 : 1237))) + Arrays.hashCode(this.types);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SerializedAccessPath other = (SerializedAccessPath) obj;
        if (this.base == null) {
            if (other.base != null) {
                return false;
            }
        } else if (!this.base.equals(other.base)) {
            return false;
        }
        if (this.baseType == null) {
            if (other.baseType != null) {
                return false;
            }
        } else if (!this.baseType.equals(other.baseType)) {
            return false;
        }
        if (!Arrays.equals(this.fields, other.fields) || this.taintSubFields != other.taintSubFields || !Arrays.equals(this.types, other.types)) {
            return false;
        }
        return true;
    }

    public String getBase() {
        return this.base;
    }

    public String getBaseType() {
        return this.baseType;
    }

    public boolean getTaintSubFields() {
        return this.taintSubFields;
    }

    public String[] getFields() {
        return this.fields;
    }

    public String[] getTypes() {
        return this.types;
    }
}
